package com.mmachado53.simplemvvmapp

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.mmachado53.simplemvvmapp.data.local.dao.AppDataBase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(BlockJUnit4ClassRunner::class)
class RomMigrationsTest {
    companion object {
        private const val DB_NAME = "test_db"
    }

    private lateinit var migrationTestHelper: MigrationTestHelper

    @Before
    fun before() {
        migrationTestHelper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDataBase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
        )
    }

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        // Create earliest version of the database.
        migrationTestHelper.createDatabase(DB_NAME, 1).apply {
            close()
        }

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDataBase::class.java,
            DB_NAME
        ).addMigrations(*AppDataBase.MIGRATIONS)
            .build().apply {
                openHelper.writableDatabase.close()
            }
    }
}
