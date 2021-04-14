### ITT 

*tr* - Yapay zeka yardımıyla fotoğraftaki yazıları almak.

*en* - Taking the texts in the photograph with the help of artificial intelligence.

### @ *Step 1*

 - build.gradle(app)
plugins { 

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
