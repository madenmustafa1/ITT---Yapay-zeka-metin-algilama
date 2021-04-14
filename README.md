### ITT 

*tr* - Yapay zeka yardımıyla fotoğraftaki yazıları almak.

*en* - Taking the texts in the photograph with the help of artificial intelligence.

#  *Step 1*
 > *build.gradle(app)*

 - plugins {
    
        ...
        
        id 'kotlin-android-extensions' 
        }
- dependencies {

        ...
        ...
        
        def camerax_version = "1.1.0-alpha03"
        implementation "androidx.camera:camera-camera2:$camerax_version"
        implementation "androidx.camera:camera-lifecycle:$camerax_version"
        implementation "androidx.camera:camera-view:1.0.0-alpha23"
        implementation 'com.google.android.gms:play-services-mlkit-text-recognition:16.1.3'
        implementation 'com.squareup.picasso:picasso:2.71828'
        }
        
        
#  *Step 2*
 > *AndroidManifest*

 -  < uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 - < uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" / >
 - < uses-permission android:name="android.permission.INTERNET" /> 
 - < uses-permission android:name="android.permission.CAMERA" />
 -  < uses-feature android:name="android.hardware.camera.any" >



#  *Step 3*
 > *activity_main.xml*
 
 - Add button. -> id: takePicture
 - Add imageView.  -> id: imageView
 - Add EditText. -> id: textView


#  *Step 4*
 > *MainActivity.kt*
 
 - private val PERMISSION_CODE = 1000
 - private val IMAGE_CAPTURE_CODE = 100
//
 - var bitmap : Bitmap? = null
 - var image_uri: Uri? = null
 - var image : InputImage? = null
 //
 - var str = StringBuilder()
 - var stringb: String? = null
 //
 val values = ContentValues()
 
 
 - override fun onCreate(savedInstanceState: Bundle?) {
       
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

- override fun onRequestPermissionsResult(requestCode: Int,
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

-     private fun openCamera() {
     
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")

        image_uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

- override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
 
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            readText()
        }
    }

-  fun readText(){
  
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
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show()
                }

    }
 
 




