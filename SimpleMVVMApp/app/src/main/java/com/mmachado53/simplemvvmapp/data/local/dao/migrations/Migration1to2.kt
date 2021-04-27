package com.mmachado53.simplemvvmapp.data.local.dao.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/*
* Added image to "items" table
* */
val Migration1to2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS items_new (" +
                "local_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "server_id INTEGER, " +
                "content TEXT NOT NULL, " +
                "image BLOB, " +
                "is_sync INTEGER NOT NULL, " +
                "deleted INTEGER NOT NULL" +
                ")"
        )

        database.execSQL(
            """
                INSERT INTO items_new (local_id, server_id, content, is_sync, deleted)
                SELECT local_id, server_id, content, is_sync, deleted FROM items
            """.trimIndent()
        )

        database.execSQL("DROP TABLE items")

        database.execSQL("ALTER TABLE items_new RENAME TO items")

        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_items_server_id ON items (server_id)")
    }
}
