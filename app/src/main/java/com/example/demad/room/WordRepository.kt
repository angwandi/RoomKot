package com.example.demad.room

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

/*A Repository class abstracts access to multiple data sources. The Repository is not part of the Architecture Components libraries, but is a suggested best practice for code separation and architecture. A Repository class provides a clean API for data access to the rest of the application.

Usage:A Repository manages queries and allows you to use multiple backend. In the most common example, the Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database.*/

//1. Create a public class called WordRepository.
//2. Declare the DAO as a private property in the constructor
//3. Add the list of words as a public property and initialize it. Room executes all queries on a separate thread. Observed LiveData will notify the observer when the data has changed.
//4. Add a wrapper for the insert() method. You must call this on a non-UI thread or your app will crash. Room ensures that you don't do any long-running operations on the main thread, blocking the UI. Add the @WorkerThread annotation, to mark that this method needs to be called from a non-UI thread. Add the suspend modifier to tell the compiler that this needs to be called from a coroutine or another suspending function.

//1 and 2
class WordRepository(private val wordDao: WordDao) {
    //3.
    val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    //4
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}