package com.example.demad.room

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

//1. a class called WordViewModel that gets the Application as a parameter and extends AndroidViewModel.
//2. a private member variable to hold a reference to the repository.
//3. a private LiveData member variable to cache the list of words.
//4. an init block that gets a reference to the WordDao from the WordRoomDatabase and constructs the WordRepository based on it.
//5. In the init block, initialize the allWords property with the data from repository
//6. Define a parentJob, and a coroutineContext. The coroutineContext, by default, uses the parentJob and the main dispatcher to create a new instance of a CoroutineScope based on the coroutineContext.
//7. Override the onCleared method and cancel the parentJob. onCleared is called when the ViewModel is no longer used and will be destroyed so, now is the time to cancel any long running jobs done by the parentJob.
//8. Create a wrapper insert() method that calls the Repository's insert() method. In this way, the implementation of insert() is completely hidden from the UI. We want the insert() method to be called away from the main thread, so we're launching a new coroutine, based on the coroutine scope defined previously. Because we're doing a database operation, we're using the IO Dispatcher.


//1.
class WordViewModel(application: Application) : AndroidViewModel(application) {

    //6...
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext) //...6

    //2.
    private val repository: WordRepository
    //3.
    val allWords: LiveData<List<Word>>

    //4.
    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, scope).wordDao()
        repository = WordRepository(wordsDao)
        //5.
        allWords = repository.allWords
    }

    //8.
    fun insert(word: Word) = scope.launch(Dispatchers.IO) {
        repository.insert(word)
    }

    //7.
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
