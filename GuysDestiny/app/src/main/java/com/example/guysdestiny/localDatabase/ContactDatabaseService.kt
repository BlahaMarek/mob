package com.example.guysdestiny.localDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.guysdestiny.services.apiModels.contact.ContactListResponse

class ContactDatabaseService(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "GuysDestinyDatabase"
        private val TABLE_CONTACTS = "ContactsTable"
        private val KEY_ID = "uid"
        private val KEY_NAME = "name"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACT_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }


    //method to insert List of contacts data
    fun addContacts(contacts: List<ContactListResponse>){
        val db = this.writableDatabase
        for (contact in contacts)
        {
            val contentValues = ContentValues()
            contentValues.put(KEY_ID, contact.id)
            contentValues.put(KEY_NAME, contact.name)
            // Inserting Row
            val success = db.insert(TABLE_CONTACTS, null, contentValues)
        }

        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    //method to get contacts
    fun getContacts():ArrayList<ContactListResponse>{
        val contacts = ArrayList<ContactListResponse>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var contactUid: String
        var contactName: String
        if (cursor.moveToFirst()) {
            do {
                contactUid = cursor.getString(cursor.getColumnIndex(KEY_ID))
                contactName = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val contact = ContactListResponse()
                contact.id= contactUid
                contact.name = contactName
                contacts.add(contact)
            } while (cursor.moveToNext())
        }
        return contacts
    }
}