package com.bintang.bangkitcapstoneproject.ui.food_detector

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bintang.bangkitcapstoneproject.data.impl.FoodDetectorCameraRepositoryImpl
import com.bintang.bangkitcapstoneproject.databinding.ActivityFoodDetectorCameraBinding
import com.bintang.bangkitcapstoneproject.ui.view_model.FoodDetectorCameraViewModel
import com.bintang.bangkitcapstoneproject.ui.view_model.FoodDetectorCameraViewModelFactory
import com.bintang.bangkitcapstoneproject.utils.createFile
import com.bintang.bangkitcapstoneproject.tflite.Classifier

class FoodDetectorCamera : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDetectorCameraBinding

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private val foodDetectorCameraViewModel: FoodDetectorCameraViewModel by viewModels {
        FoodDetectorCameraViewModelFactory(
            FoodDetectorCameraRepositoryImpl()
        )
    }

    private val mInputSize = 128
    private val mModelPath = "adhaar.tflite"
    private val mLabelPath = "label.txt"
    private lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetectorCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.captureImage.setOnClickListener { takePhoto() }
        binding.switchCamera.setOnClickListener { switchCamera() }
        binding.searchButton.setOnClickListener { searchButtonAction() }

        initClassifier()
    }

    private fun initClassifier(){
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
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

                    Toast.makeText(
                        this@FoodDetectorCamera,
                        result.get(0).title,
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun switchCamera() {
        foodDetectorCameraViewModel.switchCameraOCR()
    }

    private fun searchButtonAction() {
        val intent = Intent(this@FoodDetectorCamera, FoodValidatorText::class.java)
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
}
