package com.example.tanahore.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanahore.R
import com.example.tanahore.adapter.ArticleAdapter
import com.example.tanahore.data.viewmodel.FactoryVM
import com.example.tanahore.data.viewmodel.MainVM
import com.example.tanahore.databinding.ActivityMainBinding
import com.example.tanahore.preference.UserManager

class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainVM
    private lateinit var preferences: UserManager
    private lateinit var factory: FactoryVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        factory = FactoryVM.getInstance(this)
        binding.rvTanahore.layoutManager = LinearLayoutManager(this)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainVM::class.java]
        mainViewModel.getAllArticles()
        mainViewModel.dataItem.observe(this) { dataItem ->
            setData(dataItem)
            Log.d("MainActivity", "setData called with data: $dataItem")
        }

        mainViewModel.articlesItem.observe(this) { articlesItem ->
            setData(articlesItem)
            Log.d("MainActivity", "setData called with data: $articlesItem")
        }
        mainViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        try {
            with(binding){
                searchView.setupWithSearchBar(searchBar)
                searchView
                    .editText
                    .setOnEditorActionListener{ textView, _, _ ->
                        searchView.hide()
                        val query= textView.text.toString()
                        if (query.isNotEmpty()){
                            mainViewModel.searchArticle(this@MainActivity, query)
                            binding.searchBar.setText(query)
                        } else{
                            Toast.makeText(this@MainActivity, "Cannot be Empty", Toast.LENGTH_SHORT).show()
                        }
                        false
                    }
            }
        } catch (e: Error) {
            Log.d("catch error", e.toString())
            Toast.makeText(this@MainActivity, "Error : $e", Toast.LENGTH_SHORT).show()
        }
        preferences = UserManager(this)
        if (preferences.getToken() == null) {
            binding.logout.visibility = View.GONE
        }else{
            binding.logout.visibility = View.VISIBLE
        }
        setAction()

        if (!permissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun permissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun setAction(){
        binding.fabCamera.setOnClickListener {
            if (preferences.getToken() == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else {
                startCameraX()
            }
        }
        binding.logout.setOnClickListener {
            dialogLogout()
        }
    }
    private fun startCameraX() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
        if (permissionGranted()) {
            startActivity(Intent(this, CameraActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setData(data: List<Any>){
        val adapter = ArticleAdapter().apply {
            submitList(data)
            notifyDataSetChanged()
        }
        binding.rvTanahore.adapter = adapter
    }

    private fun dialogLogout() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.logout)
            setMessage(R.string.logout2)
            setCancelable(false)
            setPositiveButton("YES") { _, _ ->
                preferences.setToken(null)
                preferences.setWelcome(false)
                startActivity(Intent(this@MainActivity, MainActivity::class.java))
                binding.logout.visibility = View.GONE
            }
            setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}