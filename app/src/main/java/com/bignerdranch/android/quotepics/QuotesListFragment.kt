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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

private const val TAG = "QuotesListFragment"
class QuotesListFragment : Fragment() {
    private var _binding: FragmentQuotesListBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "FragmentQuotesListBinding was null" }

    private val quotesListViewModel: QuotesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                quotesListViewModel.uiState.collect { uiState ->
                    binding.quotesRecyclerView.adapter = QuotesListAdapter(uiState.quotes) { quote ->
                        // Snackbar message of the quote author clicked
                        Snackbar.make(view, quote.author, Snackbar.LENGTH_SHORT).show()
                        val action = QuotesListFragmentDirections.showQuoteDetail(quote)
                        findNavController().navigate(action)
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_quotes_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                // refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
