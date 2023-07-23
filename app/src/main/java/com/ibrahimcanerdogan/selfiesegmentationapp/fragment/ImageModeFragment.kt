package com.ibrahimcanerdogan.selfiesegmentationapp.fragment

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibrahimcanerdogan.selfiesegmentationapp.R
import com.ibrahimcanerdogan.selfiesegmentationapp.databinding.FragmentImageModeBinding
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.GroundInstance
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.ImageUtils
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.ImageUtils.resizeBitmap
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.LoadingDialog
import com.ibrahimcanerdogan.selfiesegmentationapp.utils.PermissionCheckerUtils


class ImageModeFragment : Fragment(), OnClickListener {

    private var _binding : FragmentImageModeBinding? = null
    private val binding get() = _binding!!

    private lateinit var loading : LoadingDialog

    private lateinit var imageView : ImageView
    private lateinit var chip: Chip
    private lateinit var buttonSegment : Button
    private lateinit var buttonCamera : FloatingActionButton
    private lateinit var buttonGallery : FloatingActionButton
    private lateinit var buttonBackup : FloatingActionButton
    private lateinit var buttonDownload : FloatingActionButton

    private lateinit var imageViewUri : Uri
    private lateinit var selectedBitmap : Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageModeBinding.inflate(inflater, container, false)
        loading = LoadingDialog(requireActivity(), inflater)

        imageView = binding.imageView
        chip = binding.chip
        chip.isChecked = true
        chip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = resources.getString(R.string.foreground_mode)
                mode = !mode
            } else {
                buttonView.text = resources.getString(R.string.background_mode)
                mode = !mode
            }
        }
        buttonSegment = binding.buttonSegment
        buttonSegment.setOnClickListener(this)
        buttonCamera = binding.buttonCamera
        buttonCamera.setOnClickListener(this)
        buttonGallery = binding.buttonAdd
        buttonGallery.setOnClickListener(this)
        buttonBackup = binding.buttonBackup
        buttonBackup.setOnClickListener(this)
        buttonDownload = binding.buttonDownload
        buttonDownload.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.buttonSegment -> {
                loading.show()
                ImageAnalyzer().analyze(imageView.drawable.toBitmap())
                Handler(Looper.getMainLooper()).postDelayed({
                    imageView.setImageBitmap(GroundInstance.getInstance()?.foreground)
                    loading.dismiss()
                }, 2000)
            }
            R.id.buttonCamera -> {
                PermissionCheckerUtils.checkCameraPerm(requireContext(), requireActivity()) {
                    imageViewUri = ImageUtils.getImageUriFromBitmap(requireContext(), imageView.drawable.toBitmap())
                    takePhotoResult.launch(imageViewUri)
                }
            }
            R.id.buttonAdd -> {
                PermissionCheckerUtils.checkGalleryReadPerm(requireContext(), requireActivity()) {
                    selectImageFromGallery()
                }
            }
            R.id.buttonBackup -> {
                imageView.setImageResource(0)
                imageView.setImageBitmap(GroundInstance.getInstance()?.backupImage)
            }
            R.id.buttonDownload -> {
                PermissionCheckerUtils.checkGalleryWritePerm(requireContext(), requireActivity()) {
                    ImageUtils.download(imageView.drawable.toBitmap(), requireContext())
                }
            }
        }
    }

    // Camera

    private val takePhotoResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            imageViewUri.let {uri ->
                if (mode) {
                    imageView.setImageResource(0)
                    imageView.setImageURI(uri)
                    GroundInstance.getInstance()?.backupImage = imageView.drawable.toBitmap()
                    buttonBackup.visibility = View.VISIBLE
                } else {
                    loading.show()
                    imageView.setImageResource(0)
                    createSelfieWithBackground(uri)
                }
            }
        }
    }

    // Gallery
    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                if (mode) {
                    imageView.setImageResource(0)
                    imageView.setImageURI(uri)
                    GroundInstance.getInstance()?.backupImage = imageView.drawable.toBitmap()
                    buttonBackup.visibility = View.VISIBLE
                } else {
                    loading.show()
                    createSelfieWithBackground(uri)
                }
            }
        }

    private fun createSelfieWithBackground(uri: Uri?) {
        if (GroundInstance.getInstance()?.foreground != null) {
            imageView.setImageResource(0)
            selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            val resizedBitmap = resizeBitmap(
                selectedBitmap,
                GroundInstance.getInstance()?.foreground!!.width,
                GroundInstance.getInstance()?.foreground!!.height
            )

            ImageAnalyzer().analyzeWithBG(
                GroundInstance.getInstance()?.foreground!!,
                resizedBitmap
            )

            Handler(Looper.getMainLooper()).postDelayed({
                imageView.setImageBitmap(GroundInstance.getInstance()?.background)
                loading.dismiss()
            }, 2000)
        } else {
            Toast.makeText(requireContext(), "Make a segment selfie before choosing a background!", Toast.LENGTH_SHORT).show()
            loading.dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var mode : Boolean = true
    }

}