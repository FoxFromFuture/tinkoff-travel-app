package com.tinkoff.travelapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DBDeclaration.DATABASE_NAME, null, DBDeclaration.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            it.execSQL(DBDeclaration.TRIPS_CREATE_TABLE)
            it.execSQL(DBDeclaration.USERS_CREATE_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            it.execSQL(DBDeclaration.TRIPS_DELETE_TABLE)
            it.execSQL(DBDeclaration.USERS_DELETE_TABLE)
        }
        onCreate(db)
    }
}
