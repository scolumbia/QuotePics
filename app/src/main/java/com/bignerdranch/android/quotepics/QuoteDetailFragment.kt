package com.bignerdranch.android.quotepics

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import android.text.format.DateFormat
import android.util.Log
import com.bignerdranch.android.quotepics.databinding.FragmentQuoteDetailBinding
import java.util.Date
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.quotepics.databinding.ListItemPhotoBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class QuoteDetailFragment : Fragment() {
    private var _binding: FragmentQuoteDetailBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "FragmentQuoteDetailBinding was null" }


    private val args: QuoteDetailFragmentArgs by navArgs()

    private var photoFile: File? = null

    private val quoteDetailViewModel: QuoteDetailViewModel by viewModels()
//    private val quoteDetailViewModel: QuoteDetailViewModel by viewModels {
//        MomentDetailViewModelFactory(args.quoteId)
//    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        if (didTakePhoto) {
            viewLifecycleOwner.lifecycleScope.launch {
                QuoteRepository().addQuoteImage(
                    args.quote.id,
                    photoFile!!
                )
                updatePhotos()
            }
        }
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
        Log.d("argument passed", args.quote.text)
        binding.dailyQuote.text = args.quote.text
        binding.quoteAuthor.text = args.quote.author

        val captureImageIntent = takePhoto.contract.createIntent(
            requireContext(),
            Uri.parse("")
        )

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
            this.photoFile = photoFile
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.bignerdranch.android.quotepics.fileprovider",
                photoFile
            )
            Log.d("photoURI", photoURI.toString())
            takePhoto.launch(photoURI)

        })

        viewLifecycleOwner.lifecycleScope.launch {
            updatePhotos()
        }
    }

    private suspend fun updatePhotos() {
        val quoteId = args.quote.id

        binding.quotePictures.adapter = PhotoAdapter(
            QuoteRepository().getQuoteImages(quoteId)
        )

    }

    class PhotoHolder(
        private val binding: ListItemPhotoBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoFileBase64: String) {
            // Load the file from the base64 string
            val imageBytes = android.util.Base64.decode(
                photoFileBase64, android.util.Base64.DEFAULT
            )
            val decodedImage = android.graphics.BitmapFactory.decodeByteArray(
                imageBytes, 0, imageBytes.size
            )
            binding.itemImageView.setImageBitmap(decodedImage)
        }
    }

    class PhotoAdapter(
        private val photoFiles: List<String>
    ): RecyclerView.Adapter<PhotoHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemPhotoBinding.inflate(inflater, parent, false)
            return PhotoHolder(binding)
        }

        override fun getItemCount() = photoFiles.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val photoFile = photoFiles[position]
            holder.bind(photoFile)
        }
    }

}