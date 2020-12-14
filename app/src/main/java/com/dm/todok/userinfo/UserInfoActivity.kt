package com.dm.todok.userinfo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.load
import com.dm.todok.UserViewModel
import com.dm.todok.databinding.ActivityUserinfoBinding
import com.dm.todok.network.Api
import com.dm.todok.network.UserWebService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserInfoActivity: AppCompatActivity() {
    private val CAMERA_PERMISSION_CODE = 101
    private lateinit var binding: ActivityUserinfoBinding
    private val userViewModel: UserViewModel by viewModels()

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { picture ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            picture.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

        lifecycleScope.launch {
            handleImage(tmpFile.toUri())
        }
    }

    private fun openCamera() = takePicture.launch()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        lifecycleScope.launch {
            val userInfo = userViewModel.userInfo.value
            binding.imageView.load(userInfo?.avatar)
        }
    }

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    private suspend fun handleImage(photoUri: Uri) {
        userViewModel.updateAvatar(photoUri)
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

}