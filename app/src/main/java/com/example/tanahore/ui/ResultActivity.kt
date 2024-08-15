package com.example.tanahore.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahore.R
import com.example.tanahore.data.Results
import com.example.tanahore.data.response.ScanResponse
import com.example.tanahore.data.viewmodel.FactoryVM
import com.example.tanahore.data.viewmodel.IotVM
import com.example.tanahore.databinding.ActivityResultBinding
import com.example.tanahore.preference.UserManager
import com.example.tanahore.utils.rotateBitmap
import com.example.tanahore.utils.showToast
import java.io.File

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var factory: FactoryVM
    private lateinit var preference: UserManager
    private val viewModel: IotVM by viewModels {
        factory
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = FactoryVM.getInstance(this)
        preference = UserManager(this)
        binding.btnPh.visibility = View.VISIBLE
        binding.cardView2.visibility = View.GONE

        val photoFile = intent.getSerializableExtra("picture") as File
        val data = intent.getSerializableExtra("data") as ScanResponse // Cast to ScanResponse

        // Display the image
        val bitmap = BitmapFactory.decodeFile(photoFile.path)
        val isBackCamera = intent.getBooleanExtra("isBackCamera", false) // Get the camera type
        val rotatedBitmap = rotateBitmap(bitmap, isBackCamera) // Rotate the bitmap
        binding.addImage.setImageBitmap(rotatedBitmap)

        // Display the data
        binding.txtJenisTanah.text = getString(R.string.jenis_tanah) + " " + data.data.predictedsoil

        binding.txtTanaman.text = getString(R.string.tanaman) + " " + data.data.plantrecommendations.joinToString(", ", limit = data.data.plantrecommendations.size)
        // Add more data bindings as needed

        // Hide the progress bar
        binding.progressBar.visibility = View.GONE
        setAction(data.data.predictedsoil)
    }

        private fun setAction(soil: String) {
            binding.back.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            binding.btnPh.setOnClickListener {
                iot(soil)
            }

        }

    @SuppressLint("SetTextI18n")
    private fun iot(jenisTanah: String) {
        Log.d("iot", "masuk")
        preference.getToken()?.let {
            viewModel.postPh("DT11", jenisTanah, it).observe(this) { response ->
                when (response) {
                    is Results.Success -> {
                        Log.d("iot", "success")
                        binding.cardView2.visibility = View.VISIBLE
                        binding.pH.text = resources.getString(R.string.phtanah) + " " + response.data.data.data.ph
                        binding.Rekomendasi.text = resources.getString(R.string.rekomendasi) + " " + response.data.data.data.plantRecommendation
                        binding.Kelembapan.text = resources.getString(R.string.kelembapan) + " " + response.data.data.data.kelembapan
                        binding.IntensitasCahaya.text = resources.getString(R.string.intensitascahaya) + " " + response.data.data.data.intensitasCahaya
                        binding.progressBar.visibility = View.GONE
                    }

                    is Results.Error -> {
                        Log.d("iot", "error")
                        showToast(response.error)
                    }

                    is Results.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}