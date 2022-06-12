package com.bintang.bangkitcapstoneproject.ui.food_detector

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bintang.bangkitcapstoneproject.databinding.ActivityFoodDetectorCameraBinding

import com.bintang.bangkitcapstoneproject.ui.utils.createFile
import android.graphics.BitmapFactory
import android.view.View
import com.bintang.bangkitcapstoneproject.tflite.Classifier
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class FoodDetectorCamera : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityFoodDetectorCameraBinding

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val mInputSize = 128
    private val mModelPath = "adhaar.tflite"
    private val mLabelPath = "label.txt"
    private lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetectorCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //CHECKING IS THE APP HAS PERMISSION TO ACCESS CAMERA
        if (hasCameraPermission()) {
            initClassifier()
            binding.apply {
                btnBack.setOnClickListener { finish() }
                cameraShutter.setOnClickListener { takePhoto() }
                btnFoodSearch.setOnClickListener { searchButtonAction() }
            }

        } else {
            //IF THE APPLICATIONS DOESN'T HAVE PERMISSION,
            //THE APP WILL MAKE A PERMISSION REQUEST TO ACCESS THE CAMERA
            requestCameraPermission()
        }

    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    //CAMERA PERMISSION DENIED ACTION
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            requestCameraPermission()
        }
    }

    //CAMERA PERMISSION GRANTED ACTION
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            this,
            "Camera Granted Permission",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initClassifier(){
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
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
            } catch (exc: Exception) {
                Toast.makeText(
                    this@FoodDetectorCamera,
                    "Gagal Memunculkan Camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.path)
                    val result = classifier.recognizeImage(bitmap)

                    val intent = Intent(this@FoodDetectorCamera, FoodDetectorText::class.java)
                    intent.putExtra("label", result[0].title)
                    startActivity(intent)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@FoodDetectorCamera,
                        "Gagal mengambil gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun searchButtonAction() {
        val intent = Intent(this@FoodDetectorCamera, FoodDetectorText::class.java)
        startActivity(intent)
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    //CHECK CAMERA PERMISSION
    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }

    //REQUEST CAMERA PERMISSION
    private fun requestCameraPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application need to access your camera",
            CAMERA_PERMISSION_REQUEST_CODE,
            Manifest.permission.CAMERA
        )
    }

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}
