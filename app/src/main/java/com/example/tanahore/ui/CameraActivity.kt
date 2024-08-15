package com.example.tanahore.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.tanahore.data.Results
import com.example.tanahore.data.response.ScanResponse
import com.example.tanahore.data.viewmodel.CameraVM
import com.example.tanahore.data.viewmodel.FactoryVM
import com.example.tanahore.databinding.ActivityCameraBinding
import com.example.tanahore.preference.UserManager
import com.example.tanahore.utils.createFile
import com.example.tanahore.utils.reduceFileImage
import com.example.tanahore.utils.showToast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.Serializable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var factory: FactoryVM
    private lateinit var preference: UserManager
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private val viewModel: CameraVM by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = FactoryVM.getInstance(this)
        preference = UserManager(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        setAction()
        binding.progressBar.visibility = View.GONE
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun setAction() {
        binding.exit.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.captureImage.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                showToast("Failed to Show Camera")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun switchCamera() {
        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
        startCamera()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    showToast("Failed To Take Image.")
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    postImage(photoFile)
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun postImage(image: File) {
        Log.d("postimage", "masuk")
        val file = reduceFileImage(image)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )
        val token = "Bearer " + preference.getToken().toString()
        viewModel.postImage(imageMultipart, token).observe(this) { response ->
            when (response) {
                is Results.Success -> {
                    val scanResponse = response.data
                    if (scanResponse is ScanResponse) {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra("picture", file)
                        intent.putExtra("data", scanResponse as Serializable)
                        startActivity(intent)
                        finish()
                    }
                }

                is Results.Error -> {
                    showToast("Soil Type Not Found")
                    binding.progressBar.visibility = View.GONE
                }

                is Results.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}