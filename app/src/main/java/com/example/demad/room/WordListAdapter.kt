package com.example.demad.room

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class WordListAdapter internal constructor(context: Context) : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<Word>() //cached copy of words

    inner class WordViewHolder(itemVew: View) : RecyclerView.ViewHolder(itemVew) {
        val wordItemView: TextView = itemVew.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemVew = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemVew)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        holder.wordItemView.text = current.word
    }

    internal fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = words.size


}