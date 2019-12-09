package com.example.guysdestiny.localDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.guysdestiny.services.apiModels.room.ReadResponse

class PostDatabaseService(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "GuysDestinyDatabase"
        private val TABLE_POSTS = "PostsTable"
        private val KEY_ID = "uid"
        private val KEY_ROOM_ID = "roomId"
        private val KEY_MESSAGE = "message"
        private val KEY_TIME = "time"
        private val KEY_NAME = "name"
        private val CREATE_POST_TABLE = ("CREATE TABLE " + TABLE_POSTS +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID + " TEXT," + KEY_ROOM_ID + " TEXT," +
                    KEY_MESSAGE + " TEXT," + KEY_TIME + " TEXT," +
                    KEY_NAME + " TEXT" +")")
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        db?.execSQL(CREATE_POST_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS)
        onCreate(db)
    }


    //method to insert List of posts data
    fun addPosts(posts: List<ReadResponse>){
        val db = this.writableDatabase
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS)
        onCreate(db)
        for (post in posts)
        {
            val contentValues = ContentValues()
            contentValues.put(KEY_ID, post.uid)
            contentValues.put(KEY_ROOM_ID, post.roomid)
            contentValues.put(KEY_MESSAGE, post.message)
            contentValues.put(KEY_NAME, post.name)
            contentValues.put(KEY_TIME, post.time)
            // Inserting Row
            val success = db.insert(TABLE_POSTS, null, contentValues)
        }

        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    //method to get posts
    fun getPosts(roomId : String):ArrayList<ReadResponse>{
        val posts = ArrayList<ReadResponse>()
        val selectQuery = "SELECT  * FROM $TABLE_POSTS WHERE $KEY_ROOM_ID = '$roomId'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(CREATE_POST_TABLE)
            return ArrayList()
        }
        var postUid: String
        var postRoomId: String
        var postMessage: String
        var postName: String
        var postTime: String
        if (cursor.moveToFirst()) {
            do {
                postUid = cursor.getString(cursor.getColumnIndex(KEY_ID))
                postRoomId = cursor.getString(cursor.getColumnIndex(KEY_ROOM_ID))
                postMessage = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE))
                postName = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                postTime = cursor.getString(cursor.getColumnIndex(KEY_TIME))

                val post = ReadResponse()
                post.uid = postUid
                post.roomid = postRoomId
                post.message = postMessage
                post.name = postName
                post.time = postTime
                posts.add(post)
            } while (cursor.moveToNext())
        }
        return posts
    }
}