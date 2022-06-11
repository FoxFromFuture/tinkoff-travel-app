package com.tinkoff.travelapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.tinkoff.travelapp.database.model.TripDataModel
import com.tinkoff.travelapp.model.route.Route

class DBManager(context: Context) {
    val dbHelper = DBHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    fun writeDbData(title: String, date: String, route: Route) {
        val values = ContentValues().apply {
            put(DBDeclaration.COLUMN_NAME_TITLE, title)
            put(DBDeclaration.COLUMN_NAME_DATE, date)
            put(DBDeclaration.COLUMN_NAME_ROUTE, Gson().toJson(route))
        }
        db?.insert(DBDeclaration.TABLE_NAME, null, values)
    }

    fun readDbData(): ArrayList<TripDataModel> {
        val dataList = ArrayList<TripDataModel>()
        val cursor = db?.query(DBDeclaration.TABLE_NAME, null, null, null, null, null, null)
        with(cursor) {
            while (this?.moveToNext()!!) {
                val tripTitle =
                    cursor?.getString(cursor.getColumnIndex(DBDeclaration.COLUMN_NAME_TITLE))
                val tripDate =
                    cursor?.getString(cursor.getColumnIndex(DBDeclaration.COLUMN_NAME_DATE))
                val tripRoute =
                    cursor?.getString(cursor.getColumnIndex(DBDeclaration.COLUMN_NAME_ROUTE))
                val tripModel = TripDataModel(
                    tripTitle.toString(),
                    tripDate.toString(),
                    Gson().fromJson(tripRoute, Route::class.java)
                )

                dataList.add(tripModel)
            }
        }
        cursor?.close()
        return dataList
    }

    fun closeDb() {
        dbHelper.close()
    }
}