package com.example.demad.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface WordDao {

    //method to get all the words and have it return a List of Words.
    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAllWords(): List<Word>

    //method to insert one word
    @Insert
    fun insert(word: Word)

    //method to delete all the words
    //There is no convenience annotation for deleting multiple entities, so annotate the method with the generic @Query.
    @Query("DELETE FROM word_table")
    fun deleteAll()


}