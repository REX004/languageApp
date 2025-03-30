package ru.madfinal.launguageapp.presentation.profile.resizePhoto

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.madfinal.launguageapp.databinding.FragmentResizePhotoBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import androidx.core.net.toUri

class ResizePhotoFragment :
    BaseFragment<FragmentResizePhotoBinding>(FragmentResizePhotoBinding::inflate) {

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUri = ResizePhotoFragmentArgs.fromBundle(it).imageUri.toUri()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageUri?.let { binding.cropImageView.setImageUriAsync(it) }

        binding.useImageButton.setOnClickListener {
            val croppedBitmap = binding.cropImageView.croppedImage
            Log.d("ResizePhotoFragment", "croppedBitmap: $croppedBitmap")

            if (croppedBitmap != null) {
                val croppedUri = saveBitmapToFile(requireContext(), croppedBitmap)
                Log.d("ResizePhotoFragment", "croppedUri: $croppedUri")

                if (croppedUri != null) {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "croppedImage",
                        croppedUri.toString()
                    )
                    Log.d(
                        "ResizePhotoFragment",
                        "Setting savedStateHandle with URI: $croppedUri"
                    )
                    findNavController().popBackStack()
                } else {
                    Log.e("ResizePhotoFragment", "Failed to save cropped image")
                }
            } else {
                Log.e("ResizePhotoFragment", "croppedImage is null!")
            }
        }
    }

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
        val fileName = "cropped_image_${UUID.randomUUID()}.jpg"
        val imagesDir =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imagesDir, fileName)

        try {
            FileOutputStream(imageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            val fileUri = Uri.fromFile(imageFile)
            Log.d("ResizePhotoFragment", "Saved image to: ${imageFile.absolutePath}")
            Log.d(
                "ResizePhotoFragment",
                "File exists: ${imageFile.exists()}, File size: ${imageFile.length()}"
            )
            return fileUri
        } catch (e: IOException) {
            Log.e("ResizePhotoFragment", "Error saving image: ${e.message}", e)
            return null
        }
    }
}
