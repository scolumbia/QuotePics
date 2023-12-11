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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
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
import android.util.Log
import com.bignerdranch.android.quotepics.databinding.FragmentQuoteDetailBinding
import java.util.Date
import androidx.recyclerview.widget.GridLayoutManager

class QuoteDetailFragment : Fragment() {
    private var _binding: FragmentQuoteDetailBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "FragmentQuoteDetailBinding was null" }


    private val args: QuoteDetailFragmentArgs by navArgs()

    private val quoteDetailViewModel: QuoteDetailViewModel by viewModels()
//    private val quoteDetailViewModel: QuoteDetailViewModel by viewModels {
//        MomentDetailViewModelFactory(args.quoteId)
//    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        //TODO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuoteDetailBinding.inflate(inflater, container, false)
        binding.quotePictures.layoutManager = GridLayoutManager(context, 3)
        val galleryItems = mutableListOf<GalleryItem>()
//        val momentFiles = getMomentFiles(id)
//        for (file in quoteFiles) {
//            val uri = FileProvider.getUriForFile(
//                requireContext(),
//                "edu.appstate.cs.moments.fileprovider",
//                file
//            )
//            galleryItems.add(GalleryItem(uri))
//        }
        Log.d("argument passed", args.quote.text)
        binding.dailyQuote.text = args.quote.text
        binding.quoteAuthor.text = args.quote.author

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set onclick listener for the camera button
        binding.takePicture.setOnClickListener(View.OnClickListener {
            val photoName = "IMG_${DateFormat.format("yyyyMMdd_hhmmss", Date())}.jpg"
            val photoFile = File(
                requireContext().filesDir,
                photoName
            )
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.bignerdranch.android.quotepics.fileprovider",
                photoFile
            )
            takePhoto.launch(photoURI)
        })
    }
}