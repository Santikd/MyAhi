package com.dicoding.myahi.view.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.myahi.R
import com.dicoding.myahi.databinding.ActivityAddStoryBinding
import com.dicoding.myahi.data.utils.generateImageUri
import com.dicoding.myahi.data.utils.compressImage
import com.dicoding.myahi.data.utils.convertUriToFile
import com.dicoding.myahi.view.ViewModelFactory
import com.dicoding.myahi.view.home.MainActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var selectedImageUri: Uri? = null
    private val viewModel by viewModels<AddStoryViewModel> { ViewModelFactory.getInstance(this) }

    private val openGalleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            displaySelectedImage()
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private val openCameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        if (isSuccessful) displaySelectedImage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.galleryButton.setOnClickListener { launchGallery() }
        binding.cameraButton.setOnClickListener { launchCamera() }
        binding.uploadButton.setOnClickListener { processImageUpload() }
    }

    private fun launchGallery() {
        openGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun launchCamera() {
        selectedImageUri = generateImageUri(this)
        openCameraLauncher.launch(selectedImageUri!!)
    }

    private fun displaySelectedImage() {
        selectedImageUri?.let {
            Log.d("Image URI", "Selected image: $it")
            Glide.with(this)
                .load(it)
                .into(binding.previewImageView)
        }
    }

    private fun processImageUpload() {
        selectedImageUri?.let { uri ->
            val compressedImageFile = convertUriToFile(uri, this).compressImage()
            val descriptionText = binding.descriptionEditText.text.toString()

            viewModel.loadingStatus.observe(this) { isLoading ->
                isLoading?.let { toggleLoadingIndicator(it) }
            }

            val textRequestBody = descriptionText.toRequestBody("text/plain".toMediaType())
            val imageRequestBody = compressedImageFile.asRequestBody("image/jpeg".toMediaType())
            val imagePart = MultipartBody.Part.createFormData("photo", compressedImageFile.name, imageRequestBody)

            lifecycleScope.launch { viewModel.uploadStory(imagePart, textRequestBody) }

            viewModel.loadingStatus.observe(this) { showToastMessage(getString(R.string.empty_image_warning)) }

            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(mainIntent)
        } ?: showToastMessage(getString(R.string.empty_image_warning))
    }


    private fun toggleLoadingIndicator(isVisible: Boolean) {
        binding.progressIndicator.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showToastMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
