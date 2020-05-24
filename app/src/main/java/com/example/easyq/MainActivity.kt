package com.example.easyq

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import android.R.attr.data
import android.content.pm.PackageManager
import android.widget.Toast
import com.google.zxing.integration.android.IntentResult
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.zxing.qrcode.encoder.QRCode
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.jar.Manifest
import com.mongodb.MongoClient
import com.mongodb.MongoException


class MainActivity : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        next.setOnClickListener {
            val i = Intent(this@MainActivity,CodeActivity::class.java)
            startActivity(i)
        }


        var mongoClient: MongoClient? = null
        try {
            mongoClient = MongoClient("mongodb+srv://swapnil:Swapnil2@cluster0-jyrpv.gcp.mongodb.net/test?retryWrites=true&w=majority", 27017)
            println("Connected to MongoDB!")

        } catch (e: MongoException) {
            e.printStackTrace()
        } finally {
            mongoClient!!.close()
        }

        if(ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
            ){
            askForCameraPermission()
        }
        else{
            setUpControls()
        }





    }
    private fun setUpControls(){
        detector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this,detector)
            .setAutoFocusEnabled(true)
            .build()

        cameraSurfaceView.holder.addCallback(surfaceCallBack)

        detector.setProcessor(processor)

    }
    private fun askForCameraPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                setUpControls()
            }
            else{
                Toast.makeText(applicationContext,"Permission Denied",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private val surfaceCallBack = object : SurfaceHolder.Callback{
        override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder?) {
            cameraSource.stop()
        }

        override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
            try {
                cameraSource.start(surfaceHolder)
            }
            catch (exception:Exception){
                Toast.makeText(applicationContext,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private val processor = object : Detector.Processor<Barcode>{

        override fun release() {

        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {

            if(detections!=null && detections.detectedItems.isNotEmpty()){
                val qrCodes : SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)
                //txt_scan.text = code.displayValue
                Toast.makeText(applicationContext,"scanned",Toast.LENGTH_LONG).show()

            }
            /*
            else{
                txt_scan.text = ""
            }
            */
        }
    }
}


