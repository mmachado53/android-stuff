package com.mmachado53.simplemvvmapp.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mmachado53.simplemvvmapp.data.model.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {

        private const val DATABASE_NAME = "SimpleMVVMAppDatabase"

        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: build(context).also { instance = it }
            }
        }

        private fun build(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                .addMigrations() // Add migrations here
                .build()
        }
    }
}
