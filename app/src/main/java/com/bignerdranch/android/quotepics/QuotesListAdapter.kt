package com.bignerdranch.android.quotepics

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.quotepics.databinding.ListItemQuoteBinding
import com.google.android.material.snackbar.Snackbar


class QuoteHolder(private val binding: ListItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(quote: Quote, onQuoteClicked: (quote: Quote) -> Unit) {
        binding.quoteText.text = quote.text
        binding.quoteAuthor.text = quote.author

        binding.root.setOnClickListener{
//            Snackbar.make(binding.root,
//                "${quote.text} clicked!",
//                Snackbar.LENGTH_SHORT).show()
            onQuoteClicked(quote)
        }

    }
}
class QuotesListAdapter(private val quotes: List<Quote>,
    private val onQuoteClicked: (quote: Quote) -> Unit
) : RecyclerView.Adapter<QuoteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteHolder {
        Log.d("QuotesListAdapter", "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)

        val binding = ListItemQuoteBinding.inflate(inflater, parent, false)
        return QuoteHolder(binding)
    }

    override fun getItemCount()= quotes.size

    override fun onBindViewHolder(holder: QuoteHolder, position: Int) {
        val quote = quotes[position]
        holder.bind(quote, onQuoteClicked)
    }
}