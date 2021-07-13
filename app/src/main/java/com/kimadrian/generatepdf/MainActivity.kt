package com.kimadrian.generatepdf

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.kimadrian.generatepdf.databinding.ActivityMainBinding
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val STORAGECODE: Int = 400

    private lateinit var binding: ActivityMainBinding
    private val list: MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //add data to list
        for (i in 1..1000){
            list.add("Value: $i")
        }




        binding.generate.setOnClickListener {
            //Handle runtime permission with marshmallow and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //System os >= Marshmallow(6.0), check if permission is enabled or not
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                    //Permission was not granted, request it
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(
                        permissions, STORAGECODE
                    )
                } else {
                    //Permission already granted, call save pdf method
                    savePdf()
                }
            } else {
                //system OS < Marshmallow, call save pdf method
                savePdf()
            }

        }

    }

    private fun savePdf() {
        val mDoc = Document()
        //Pdf file name
        val mFileName =
            "TestData-" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                System.currentTimeMillis()
            )
        //Storage path
        val mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            .toString() + "/" + mFileName + ".pdf"
        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mPath))
            mDoc.open()
            for (i in list.indices) {
                val testData: String = list[i]
                mDoc.add(Paragraph("TestData ID: $testData\n\n"))
            }
            mDoc.close()
            Toast.makeText(this, "Saved to: $mPath", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e(TAG, "savePdf: " + e.message)
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGECODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePdf()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}