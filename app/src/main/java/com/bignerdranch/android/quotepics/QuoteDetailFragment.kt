package com.bignerdranch.android.quotepics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import android.text.format.DateFormat
import com.bignerdranch.android.quotepics.databinding.FragmentQuoteDetailBinding
import java.util.Date

class QuoteDetailFragment : Fragment() {
    private var _binding: FragmentQuoteDetailBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "FragmentQuoteDetailBinding was null" }

    private val quoteDetailViewModel: QuoteDetailViewModel by viewModels()

    private val args: QuoteDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuoteDetailBinding.inflate(inflater, container, false)

        binding.quoteDetailViewModel = quoteDetailViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}