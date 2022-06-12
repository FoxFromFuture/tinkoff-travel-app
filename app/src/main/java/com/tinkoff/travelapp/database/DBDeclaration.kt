package com.tinkoff.travelapp.database

import android.provider.BaseColumns

object DBDeclaration {
    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "TravelAppDB.db"

    const val TRIPS_TABLE_NAME = "trips"
    const val TRIPS_COLUMN_NAME_ID = "trip_id"
    const val TRIPS_COLUMN_NAME_TITLE = "title"
    const val TRIPS_COLUMN_NAME_DATE = "date"
    const val TRIPS_COLUMN_NAME_ROUTE = "route"
    const val TRIPS_COLUMN_NAME_OWNER = "owner"

    const val USERS_TABLE_NAME = "users"
    const val USERS_COLUMN_NAME_ID = "user_id"
    const val USERS_COLUMN_NAME_LOGIN_PAIR = "login_pair"

    const val TRIPS_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TRIPS_TABLE_NAME (" +
            "$TRIPS_COLUMN_NAME_ID INTEGER PRIMARY KEY," +
            "$TRIPS_COLUMN_NAME_TITLE TEXT," +
            "$TRIPS_COLUMN_NAME_DATE TEXT," +
            "$TRIPS_COLUMN_NAME_ROUTE TEXT," +
            "$TRIPS_COLUMN_NAME_OWNER INTEGER," +
            "FOREIGN KEY ($TRIPS_COLUMN_NAME_OWNER) REFERENCES $USERS_TABLE_NAME(${BaseColumns._ID})" +
            ")"

    const val USERS_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $USERS_TABLE_NAME (" +
            "$USERS_COLUMN_NAME_ID INTEGER PRIMARY KEY," +
            "$USERS_COLUMN_NAME_LOGIN_PAIR TEXT" +
            ")"

    const val TRIPS_DELETE_TABLE = "DROP TABLE IF EXISTS $TRIPS_TABLE_NAME"

    const val USERS_DELETE_TABLE = "DROP TABLE IF EXISTS $USERS_TABLE_NAME"
}
