package com.dm.todok.activity

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
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.load
import com.dm.todok.databinding.ActivityUserinfoBinding
import com.dm.todok.network.Api
import com.dm.todok.ui.user.UserViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class UserInfoActivity: AppCompatActivity() {
    private val CAMERA_PERMISSION_CODE = 101
    private lateinit var binding: ActivityUserinfoBinding

    private val userViewModel: UserViewModel by viewModel()

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

        binding.lifecycleOwner = this

        binding.takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        // Todo (geoffrey): find a better way to avoid calling user service
        lifecycleScope.launch {

            binding.userViewModel = userViewModel
            //val userInfo = Api.userWebService.getInfo().body()
            //binding.imageView.load(userInfo?.avatar)
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

    private fun handleImage(photoUri: Uri) {
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