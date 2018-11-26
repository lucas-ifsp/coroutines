package br.edu.ifsp.scl.lucas.foursquare.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import br.edu.ifsp.scl.lucas.foursquare.R
import br.edu.ifsp.scl.lucas.foursquare.model.Place
import br.edu.ifsp.scl.lucas.foursquare.model.placeParameter
import kotlinx.android.synthetic.main.activity_new_location.*

var globalsName : String = ""
var globalsType : String = ""
var globalsAtmosphere : String = ""
var globalsBitmap : Bitmap? = null

class NewLocationActivity : AppCompatActivity() {

    var chosenBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_location)
    }

    fun selectImage(view: View){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
        }else{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }
    }

    fun next(view:View) {
        if(chosenBitmap != null) {
            placeParameter = Place(
                nameText.text.toString(),
                typeText.text.toString(),
                atmosphereText.text.toString(),
                chosenBitmap!!
            )
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(applicationContext, "Please choose an image.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            val selected = data.data
            try {
                chosenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selected)
                imageView.setImageBitmap(chosenBitmap)
            }catch (e:Exception){
                e.printStackTrace()
                Log.e("myLog", e.printStackTrace().toString())
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 2)
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 1)
            }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
