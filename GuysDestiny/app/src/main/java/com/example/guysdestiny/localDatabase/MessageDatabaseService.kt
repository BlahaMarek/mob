package com.example.guysdestiny.localDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.guysdestiny.services.apiModels.contact.ContactReadResponse

class MessageDatabaseService(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "GuysDestinyDatabase"
        private val TABLE_MESSAGES = "MessagesTable"
        private val KEY_ID = "uid"
        private val KEY_CONTACT = "contact"
        private val KEY_MESSAGE = "message"
        private val KEY_TIME = "time"
        private val KEY_UID_NAME = "uidName"
        private val KEY_CONTACT_NAME = "contactName"
        private val KEY_UID_FID = "uidFid"
        private val KEY_CONTACT_FID = "contactFid"
        private val CREATE_MESSAGE_TABLE = ("CREATE TABLE " + TABLE_MESSAGES +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_ID + " TEXT," + KEY_CONTACT + " TEXT," +
                    KEY_MESSAGE + " TEXT," + KEY_TIME + " TEXT," +
                    KEY_UID_NAME + " TEXT," + KEY_CONTACT_NAME + " TEXT," +
                    KEY_UID_FID + " TEXT," + KEY_CONTACT_FID + " TEXT" + ")")
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields

        db?.execSQL(CREATE_MESSAGE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES)
        onCreate(db)
    }


    //method to insert List of messages data
    fun addMessages(messages: List<ContactReadResponse>){
        val db = this.writableDatabase
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES)
        onCreate(db)
        for (message in messages)
        {
            val contentValues = ContentValues()
            contentValues.put(KEY_ID, message.uid)
            contentValues.put(KEY_CONTACT, message.contact)
            contentValues.put(KEY_CONTACT_FID, message.contact_fid)
            contentValues.put(KEY_CONTACT_NAME, message.contact_name)
            contentValues.put(KEY_UID_FID, message.uid_fid)
            contentValues.put(KEY_UID_NAME, message.uid_name)
            contentValues.put(KEY_MESSAGE, message.message)
            contentValues.put(KEY_TIME, message.time)
            // Inserting Row
            val success = db.insert(TABLE_MESSAGES, null, contentValues)
        }

        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    //method to get messages
    fun getMessages(contactUid: String, userUid: String):ArrayList<ContactReadResponse>{
        val messages = ArrayList<ContactReadResponse>()
        val selectQuery = "SELECT  * FROM $TABLE_MESSAGES WHERE ($KEY_ID = '$contactUid' AND $KEY_CONTACT = '$userUid')" +
                          " OR ($KEY_ID = '$userUid' AND $KEY_CONTACT = '$contactUid')"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(CREATE_MESSAGE_TABLE)
            return ArrayList()
        }
        var id: String
        var contact: String
        var contactFid: String
        var contactName: String
        var uidFid: String
        var uidName: String
        var mess: String
        var time: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndex(KEY_ID))
                contact = cursor.getString(cursor.getColumnIndex(KEY_CONTACT))
                contactFid = cursor.getString(cursor.getColumnIndex(KEY_CONTACT_FID))
                contactName = cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NAME))
                uidFid = cursor.getString(cursor.getColumnIndex(KEY_UID_FID))
                uidName = cursor.getString(cursor.getColumnIndex(KEY_UID_NAME))
                mess = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE))
                time = cursor.getString(cursor.getColumnIndex(KEY_TIME))

                val message = ContactReadResponse()
                message.uid = id
                message.contact = contact
                message.contact_fid = contactFid
                message.contact_name = contactName
                message.time = time
                message.message = mess
                message.uid_fid = uidFid
                message.uid_name = uidName
                messages.add(message)

            } while (cursor.moveToNext())
        }
        return messages
    }
}