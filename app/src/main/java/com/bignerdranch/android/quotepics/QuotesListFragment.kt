package com.bignerdranch.android.quotepics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.quotepics.databinding.FragmentQuotesListBinding

private const val TAG = "QuotesListFragment"
class QuotesListFragment : Fragment() {
    private var _binding: FragmentQuotesListBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "FragmentQuotesListBinding was null" }

    private val quotesListViewModel: QuotesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuotesListBinding.inflate(inflater, container, false)

        binding.quotesRecyclerView.layoutManager = LinearLayoutManager(context)

        val quotes = quotesListViewModel.quotes
        val adapter = QuotesListAdapter(quotes)
        binding.quotesRecyclerView.adapter = adapter

        return binding.root
    }
}