package com.example.bookcare

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.bai2sqliteth4.account
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PreferrentHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "databasesq.db"
        private const val DATABASE_VERSION = 1
        private const val DB_PATH_SUFFIX = "/databases/"

        private const val TABLE_ACCOUNT = "tbaccount"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "ten"
        private const val COLUMN_SDT = "sdt"
    }

    init {
        copyDatabaseFromAssets()
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_ACCOUNT (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_SDT TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNT")
        onCreate(db)
    }

    private fun copyDatabaseFromAssets() {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (dbFile.exists()) {
            Log.d("DatabaseHelper", "Database đã tồn tại, không cần copy.")
            return
        }

        try {
            context.assets.open(DATABASE_NAME).use { input ->
                val outFileName = context.applicationInfo.dataDir + DB_PATH_SUFFIX + DATABASE_NAME
                val dbDir = File(context.applicationInfo.dataDir + DB_PATH_SUFFIX)
                if (!dbDir.exists()) dbDir.mkdirs()

                FileOutputStream(outFileName).use { output ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (input.read(buffer).also { length = it } > 0) {
                        output.write(buffer, 0, length)
                    }
                    output.flush()
                }
            }
            Log.d("DatabaseHelper", "✅ Database copied successfully!")
        } catch (e: IOException) {
            Log.e("DatabaseHelper", "❌ Copy database failed! ${e.message}")
        }
    }

    fun insertData(account: account): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, account.ten)
            put(COLUMN_SDT, account.sdt)
        }
        val result = db.insert(TABLE_ACCOUNT, null, values)
        db.close()
        return result
    }

    fun getAllData(): List<account> {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_ACCOUNT"
        val cursor = db.rawQuery(query, null)
        val accounts = mutableListOf<account>()

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val sdt = it.getString(it.getColumnIndexOrThrow(COLUMN_SDT))
                accounts.add(account(id, name, sdt))
            }
        }

        db.close()
        return accounts
    }

    fun deleteData(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_ACCOUNT, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun updateData(id: Int, account: account): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, account.ten)
            put(COLUMN_SDT, account.sdt)
        }
        val result = db.update(TABLE_ACCOUNT, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
}
