package io.intelligible.intelligentocr.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import io.intelligible.intelligentocr.R

class PhoneCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phonecall)
        requestAllPermissions()
    }

    private val multiPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
         if ( map.entries.size <3){
             Toast.makeText(this, "Please Accept all the permissions", Toast.LENGTH_SHORT).show()
         }

        }


    private fun requestAllPermissions(){
       multiPermissionCallback.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
    }

}