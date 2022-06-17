package com.tinkoff.travelapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.tinkoff.travelapp.database.model.TripDataModel
import com.tinkoff.travelapp.model.route.Route

class DBManager(context: Context) {
    private val dbHelper = DBHelper(context)
    private var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    fun writeTripDbData(title: String, date: String, route: Route, owner: String) {
        val values = ContentValues().apply {
            put(DBDeclaration.TRIPS_COLUMN_NAME_TITLE, title)
            put(DBDeclaration.TRIPS_COLUMN_NAME_DATE, date)
            put(DBDeclaration.TRIPS_COLUMN_NAME_ROUTE, Gson().toJson(route))
            put(DBDeclaration.TRIPS_COLUMN_NAME_OWNER, getUserIdByLoginPair(owner))
        }
        db?.insert(DBDeclaration.TRIPS_TABLE_NAME, null, values)
    }

    fun getUserIdByLoginPair(loginPair: String): Int {
        val cursor = db?.rawQuery(
            "SELECT ${DBDeclaration.USERS_COLUMN_NAME_ID} FROM ${DBDeclaration.USERS_TABLE_NAME} WHERE ${DBDeclaration.USERS_TABLE_NAME}.${DBDeclaration.USERS_COLUMN_NAME_LOGIN_PAIR} = \'$loginPair\'",
            null
        )
        var index = 0
        cursor?.let {
            while (cursor.moveToNext()) {
                index = it.getInt(it.getColumnIndex(DBDeclaration.USERS_COLUMN_NAME_ID))
            }
            cursor.close()
        }
        return index
    }

    fun writeUserDbData(loginPair: String) {
        val values = ContentValues().apply {
            put(DBDeclaration.USERS_COLUMN_NAME_LOGIN_PAIR, loginPair)
        }
        if (!isUserExists(loginPair)) {
            db?.insert(DBDeclaration.USERS_TABLE_NAME, null, values)
        }
    }

    private fun isUserExists(loginPair: String): Boolean {
        val cursor = db?.rawQuery(
            "SELECT ${DBDeclaration.USERS_COLUMN_NAME_ID} FROM ${DBDeclaration.USERS_TABLE_NAME} WHERE ${DBDeclaration.USERS_TABLE_NAME}.${DBDeclaration.USERS_COLUMN_NAME_LOGIN_PAIR} = \'$loginPair\'",
            null
        )
        var index = 0
        cursor?.let {
            while (cursor.moveToNext()) {
                index = it.getInt(it.getColumnIndex(DBDeclaration.USERS_COLUMN_NAME_ID))
            }
            cursor.close()
        }
        if (index == 0) {
            return false
        }
        return true
    }

    fun readTripDbData(userId: Int): ArrayList<TripDataModel> {
        val dataList = ArrayList<TripDataModel>()
        val cursor = db?.rawQuery(
            "SELECT * FROM ${DBDeclaration.TRIPS_TABLE_NAME} JOIN ${DBDeclaration.USERS_TABLE_NAME} ON ${DBDeclaration.TRIPS_TABLE_NAME}.${DBDeclaration.TRIPS_COLUMN_NAME_OWNER} = ${DBDeclaration.USERS_TABLE_NAME}.${DBDeclaration.USERS_COLUMN_NAME_ID} WHERE ${DBDeclaration.USERS_TABLE_NAME}.${DBDeclaration.USERS_COLUMN_NAME_ID} = $userId",
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val tripId =
                    it.getInt(it.getColumnIndex(DBDeclaration.TRIPS_COLUMN_NAME_ID))
                val tripTitle =
                    it.getString(it.getColumnIndex(DBDeclaration.TRIPS_COLUMN_NAME_TITLE))
                val tripDate =
                    it.getString(it.getColumnIndex(DBDeclaration.TRIPS_COLUMN_NAME_DATE))
                val tripRoute =
                    it.getString(it.getColumnIndex(DBDeclaration.TRIPS_COLUMN_NAME_ROUTE))
                val tripUserId =
                    it.getInt(it.getColumnIndex(DBDeclaration.TRIPS_COLUMN_NAME_OWNER))
                val tripModel = TripDataModel(
                    tripId,
                    tripTitle.toString(),
                    tripDate.toString(),
                    Gson().fromJson(tripRoute, Route::class.java),
                    tripUserId
                )
                dataList.add(tripModel)
            }
            it.close()
        }
        return dataList
    }

    fun removeTripFromDb(userId: Int, tripIdToRemove: Int) {
        db?.execSQL("DELETE FROM ${DBDeclaration.TRIPS_TABLE_NAME} WHERE ${DBDeclaration.TRIPS_TABLE_NAME}.${DBDeclaration.TRIPS_COLUMN_NAME_ID} IN (SELECT ${DBDeclaration.TRIPS_TABLE_NAME}.${DBDeclaration.TRIPS_COLUMN_NAME_ID} FROM ${DBDeclaration.TRIPS_TABLE_NAME} JOIN ${DBDeclaration.USERS_TABLE_NAME} ON ${DBDeclaration.TRIPS_TABLE_NAME}.${DBDeclaration.TRIPS_COLUMN_NAME_OWNER} = ${DBDeclaration.USERS_TABLE_NAME}.${DBDeclaration.USERS_COLUMN_NAME_ID} WHERE ${DBDeclaration.TRIPS_TABLE_NAME}.${DBDeclaration.TRIPS_COLUMN_NAME_ID} = $tripIdToRemove AND ${DBDeclaration.USERS_TABLE_NAME}.${DBDeclaration.USERS_COLUMN_NAME_ID} = $userId)")
    }

    fun closeDb() {
        dbHelper.close()
    }
}
