package br.edu.ifsp.scl.lucas.foursquare.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

data class Place (val name: String, val type : String, val atmosphere: String, val image : Bitmap? = null, val location : LatLng = LatLng(0.0,0.0))