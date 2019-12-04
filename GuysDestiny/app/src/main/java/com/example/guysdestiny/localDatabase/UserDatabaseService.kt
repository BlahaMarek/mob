package com.example.guysdestiny.localDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.guysdestiny.models.User

class UserDatabaseService(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "GuysDestinyDatabase"
        private val TABLE_USERS = "UserTable"
        private val KEY_ID = "uid"
        private val KEY_ACCESS_TOKEN = "accessToken"
        private val KEY_REFRESH_TOKEN = "refreshToken"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_ACCESS_TOKEN + " TEXT,"
                + KEY_REFRESH_TOKEN + " TEXT" + ")")
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
        onCreate(db)
    }


    //method to insert data
    fun addUser(user: User):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, user.uid)
        contentValues.put(KEY_ACCESS_TOKEN, user.access)
        contentValues.put(KEY_REFRESH_TOKEN,user.refresh )
        // Inserting Row
        val success = db.insert(TABLE_USERS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to get user data
    fun getUser(uid: String):ArrayList<User>{
        val users = ArrayList<User>()
        val selectQuery = "SELECT  * FROM $TABLE_USERS WHERE $KEY_ID = $uid"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userUid: String
        var userAccess: String
        var userRefresh: String
        if (cursor.moveToFirst()) {
            do {
                userUid = cursor.getString(cursor.getColumnIndex(KEY_ID))
                userAccess = cursor.getString(cursor.getColumnIndex(KEY_ACCESS_TOKEN))
                userRefresh = cursor.getString(cursor.getColumnIndex(KEY_REFRESH_TOKEN))
                val user = User()
                user.uid= userUid
                user.access = userAccess
                user.refresh = userRefresh
                users.add(user)
            } while (cursor.moveToNext())
        }
        return users
    }
    //method to update data
    fun updateUser(user: User): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, user.uid)
        contentValues.put(KEY_ACCESS_TOKEN, user.access)
        contentValues.put(KEY_REFRESH_TOKEN,user.refresh )

        // Updating Row
        val success = db.update(TABLE_USERS, contentValues,"$KEY_ID = " + user.uid,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}