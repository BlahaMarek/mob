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
        private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
                    + KEY_ID + " TEXT PRIMARY KEY," + KEY_ACCESS_TOKEN + " TEXT,"
                    + KEY_REFRESH_TOKEN + " TEXT" + ")")
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
        onCreate(db)
    }


    //method to insert data
    fun addUser(user: User):Long{
        val existingUsers = getUser(user.uid)
        val db = this.writableDatabase
        db.disableWriteAheadLogging()
        var success = 0
        if(!existingUsers.any())
        {
            val contentValues = ContentValues()
            contentValues.put(KEY_ID, user.uid)
            contentValues.put(KEY_ACCESS_TOKEN, user.access)
            contentValues.put(KEY_REFRESH_TOKEN,user.refresh )
            // Inserting Row
            success = db.insert(TABLE_USERS, null, contentValues).toInt()
        }
        db.close() // Closing database connection
        return success.toLong()
    }
    //method to get user data
    fun getUser(uid: String):ArrayList<User>{
        val users = ArrayList<User>()
        val selectQuery = "SELECT  * FROM $TABLE_USERS WHERE $KEY_ID = '$uid'"
        val db = this.writableDatabase
        db.disableWriteAheadLogging()
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(CREATE_USER_TABLE)
            db.close() // Closing database connection
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
        db.close() // Closing database connection
        return users
    }
    //method to update data
    fun updateUser(user: User): Int{
        val existingUser = getUser(user.uid)
        val db = this.writableDatabase
        db.disableWriteAheadLogging()
        var success = 0
        if(existingUser.any())
        {
            val contentValues = ContentValues()
            contentValues.put(KEY_ID, user.uid)
            contentValues.put(KEY_ACCESS_TOKEN, user.access)
            contentValues.put(KEY_REFRESH_TOKEN,user.refresh )

            // Updating Row
            success = db.update(TABLE_USERS, contentValues,"$KEY_ID = " + user.uid,null)
        }else{
            success = addUser(user).toInt()
        }

        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}