package com.example.demad.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {

    //Define the DAOs that work with the database. Provide an abstract "getter" method for each @Dao.
    abstract fun wordDao(): WordDao

    //Make the WordRoomDatabase a singleton to prevent having multiple instances of the database opened at the same time.
    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        //To launch a coroutine we need a CoroutineScope. Update the getDatabase method of the RoomDatabase class, to also get a coroutine scope as parameter:
        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
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
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance


            }


        }
        //a custom implementation of the RoomDatabase.Callback(), that also gets a CoroutineScope as constructor parameter. Then, we override the onOpen method to populate the database.

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
                }
            }

            //function that deletes the contents of the database, then populates it with the two words "Hello" and "World"

            fun populateDatabase(wordDao: WordDao) {
                wordDao.deleteAll()

                var word = Word("Hello")
                wordDao.insert(word)
                word = Word("World!")
                wordDao.insert(word)
            }
        }


    }
}