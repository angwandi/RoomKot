package com.example.demad.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {

    //Define the DAOs that work with the database. Provide an abstract "getter" method for each @Dao.
    abstract fun wordDao(): WordDao

    //Make the WordRoomDatabase a singleton to prevent having multiple instances of the database opened at the same time.
    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {

                //This code uses Room's database builder to create a RoomDatabase object in the application context from the WordRoomDatabase class and names it "word_database".
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "Word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}