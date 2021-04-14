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

