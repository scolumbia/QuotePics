package com.bignerdranch.android.quotepics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.quotepics.databinding.FragmentQuotesListBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

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

//        val quotes = quotesListViewModel.quotes
//        val adapter = QuotesListAdapter(quotes)
//        binding.quotesRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigate to QuoteDetailFragment when a Quote is clicked. Pass a Quote as an argument
        // to QuoteDetailFragment
        binding.quotesRecyclerView.adapter = QuotesListAdapter(quotesListViewModel.quotes) { quote ->
            val action = QuotesListFragmentDirections.showQuoteDetail(quote)
            findNavController().navigate(action)
        }



    }
}
