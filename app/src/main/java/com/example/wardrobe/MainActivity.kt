package com.example.wardrobe

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.viewpager2.widget.ViewPager2
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST1 = 1
    private val PICK_IMAGE_REQUEST2 = 2
    private val REQUEST_IMAGE_CAPTURE = 3
    private var viewPager1: ViewPager2? = null
    private var viewPager2: ViewPager2? = null
    private var viewPagerItemArrayList1: ArrayList<ViewPagerItem>? = null
    private var viewPagerItemArrayList2: ArrayList<ViewPagerItem>? = null
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager1 = findViewById(R.id.viewpager1)
        viewPager2 = findViewById(R.id.viewpager2)

        viewPagerItemArrayList1 = ArrayList()
        viewPagerItemArrayList2 = ArrayList()

        val vpAdapter = ViewPagerAdapter(viewPagerItemArrayList1!!)
        viewPager1!!.adapter = vpAdapter
        viewPager1!!.clipToPadding = false
        viewPager1!!.clipChildren = false
        viewPager1!!.offscreenPageLimit = 2
        viewPager1!!.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER

        val vpAdapter2 = ViewPagerAdapter(viewPagerItemArrayList2!!)
        viewPager2!!.adapter = vpAdapter2
        viewPager2!!.clipToPadding = false
        viewPager2!!.clipChildren = false
        viewPager2!!.offscreenPageLimit = 2
        viewPager2!!.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER

        val pickImageButton = findViewById<ImageButton>(R.id.btn_pick_img_1)
        pickImageButton.setOnClickListener {
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Photo!")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> {
                        // Create a file to store the captured image
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            null
                        }
                        // Continue only if the file was successfully created
                        photoFile?.also {
                            // Get the URI of the file
                            selectedImageUri = FileProvider.getUriForFile(
                                this,
                                "com.example.android.fileprovider",
                                it
                            )
                            // Launch the camera app
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                    options[item] == "Choose from Gallery" -> {
                        // Launch the gallery app
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_REQUEST1)
                    }
                    options[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }

        val pickImageButton2 = findViewById<ImageButton>(R.id.btn_pick_img_2)
        pickImageButton2.setOnClickListener {
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Photo!")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> {
                        // Create a file to store the captured image
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            null
                        }
                        // Continue only if the file was successfully created
                        photoFile?.also {
                            // Get the URI of the file
                            selectedImageUri = FileProvider.getUriForFile(
                                this,
                                "com.example.android.fileprovider",
                                it
                            )
                            // Launch the camera app
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                    options[item] == "Choose from Gallery" -> {
                        // Launch the gallery app
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_REQUEST2)
                    }
                    options[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }

        val shuffle = findViewById<ImageButton>(R.id.btn_shuffle)
        shuffle.setOnClickListener {
            val positions1 = mutableListOf<Int>()
            for (i in 0 until vpAdapter.itemCount) {
                positions1.add(i)
            }
            positions1.shuffle()

            val positions2 = mutableListOf<Int>()
            for (i in 0 until vpAdapter2.itemCount) {
                positions2.add(i)
            }
            positions2.shuffle()

            vpAdapter.updatePositions(positions1)
            vpAdapter.notifyDataSetChanged()

            vpAdapter2.updatePositions(positions2)
            vpAdapter2.notifyDataSetChanged()
        }

}

    // Function to create a file to store the captured image
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                val viewPagerItem = ViewPagerItem(bitmap)
                viewPagerItemArrayList1!!.add(viewPagerItem)
                val vpAdapter = ViewPagerAdapter(viewPagerItemArrayList1!!)
                viewPager1!!.adapter = vpAdapter
            } catch (e: OutOfMemoryError) {
                // Handle the exception
                Toast.makeText(this, "Out of memory error occurred", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                val viewPagerItem = ViewPagerItem(bitmap)
                viewPagerItemArrayList2!!.add(viewPagerItem)
                val vpAdapter = ViewPagerAdapter(viewPagerItemArrayList2!!)
                viewPager2!!.adapter = vpAdapter
            } catch (e: OutOfMemoryError) {
                // Handle the exception
                Toast.makeText(this, "Out of memory error occurred", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                val viewPagerItem = ViewPagerItem(bitmap)
                viewPagerItemArrayList1!!.add(viewPagerItem)
                val vpAdapter = ViewPagerAdapter(viewPagerItemArrayList1!!)
                viewPager1!!.adapter = vpAdapter
            } catch (e: OutOfMemoryError) {
                // Handle the exception
                Toast.makeText(this, "Out of memory error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}