package com.tinkoff.travelapp.database

import android.provider.BaseColumns

object DBDeclaration {
    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "TravelAppDB.db"

    const val TABLE_NAME = "trips"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_DATE = "date"
    const val COLUMN_NAME_ROUTE = "route"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_TITLE TEXT," +
            "$COLUMN_NAME_DATE TEXT," +
            "$COLUMN_NAME_ROUTE TEXT)"

    const val DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}
