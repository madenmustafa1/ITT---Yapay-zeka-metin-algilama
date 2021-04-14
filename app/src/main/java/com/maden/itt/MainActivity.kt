package com.maden.itt


import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001

    var bitmap : Bitmap? = null

    var image_uri: Uri? = null
    var image : InputImage? = null

    var str = StringBuilder()
    var stringb: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        takePicture.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED ){
                    val permission = arrayOf(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    openCamera()

                } else {
                    Toast.makeText(applicationContext,
                            "Permission denied",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    val values = ContentValues()
    private fun openCamera() {

        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")

        image_uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)


        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            readText()
        }
    }

    fun readText(){
        if (Build.VERSION.SDK_INT < 28){
            bitmap = MediaStore.Images.Media.getBitmap(application.contentResolver, image_uri)
            imageView.setImageBitmap(bitmap)
            image = InputImage.fromBitmap(bitmap, 0)
        }
        else {
/*
            val source = ImageDecoder.createSource(this.contentResolver, image_uri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
            image = InputImage.fromBitmap(bitmap!!, 0)
 */
            bitmap = MediaStore.Images.Media.getBitmap(application.contentResolver, image_uri)
            image = InputImage.fromBitmap(bitmap, 0)
            imageView.setImageBitmap(bitmap)
        }

        val recognizer = TextRecognition.getClient()
        val result = recognizer.process(image!!)
                .addOnSuccessListener { visionText ->
                    val resultText = visionText.text

                    str = StringBuilder()
                    str.append(" $resultText")
                    stringb  = str.toString()
                    textView.setText(stringb)

                    copyButton.visibility = View.VISIBLE
                    copyButton.isClickable = true

                    for (block in visionText.textBlocks) {
                        val blockText = block.text
                        val blockCornerPoints = block.cornerPoints
                        val blockFrame = block.boundingBox
                        for (line in block.lines) {
                            val lineText = line.text
                            val lineCornerPoints = line.cornerPoints
                            val lineFrame = line.boundingBox
                            for (element in line.elements) {
                                val elementText = element.text
                                val elementCornerPoints = element.cornerPoints
                                val elementFrame = element.boundingBox

                                //sb.append(" $elementText")
                                //c = sb.toString()
                                //textView.text = c
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show()
                }

    }

    fun copyClick(view: View){
        copyText(stringb!!)
    }

    fun copyText(text: String){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", stringb)
        clipboard.setPrimaryClip(clip)
    }
}
