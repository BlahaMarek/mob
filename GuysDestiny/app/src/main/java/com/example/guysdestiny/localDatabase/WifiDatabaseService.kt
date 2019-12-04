package com.example.guysdestiny.localDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.guysdestiny.services.apiModels.room.WifiListResponse

class WifiDatabaseService(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "GuysDestinyDatabase"
        private val TABLE_WIFIS = "WifisTable"
        private val KEY_ID = "uid"
        private val KEY_TIME = "time"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_WIFI_TABLE = ("CREATE TABLE " + TABLE_WIFIS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_TIME + " TEXT" + ")")
        db?.execSQL(CREATE_WIFI_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFIS)
        onCreate(db)
    }


    //method to insert List of wifis data
    fun addWifis(wifis: List<WifiListResponse>){
        val db = this.writableDatabase
        for (wifi in wifis)
        {
            val contentValues = ContentValues()
            contentValues.put(KEY_ID, wifi.roomid)
            contentValues.put(KEY_TIME, wifi.time)
            // Inserting Row
            val success = db.insert(TABLE_WIFIS, null, contentValues)
        }

        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    //method to get wifis
    fun getWifis():ArrayList<WifiListResponse>{
        val wifis = ArrayList<WifiListResponse>()
        val selectQuery = "SELECT  * FROM $TABLE_WIFIS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var wifiUid: String
        var wifiTime: String
        if (cursor.moveToFirst()) {
            do {
                wifiUid = cursor.getString(cursor.getColumnIndex(KEY_ID))
                wifiTime = cursor.getString(cursor.getColumnIndex(KEY_TIME))
                val wifi = WifiListResponse(wifiUid,wifiTime)
                wifis.add(wifi)
            } while (cursor.moveToNext())
        }
        return wifis
    }
}