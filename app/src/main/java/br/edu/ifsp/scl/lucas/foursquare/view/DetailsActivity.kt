package br.edu.ifsp.scl.lucas.foursquare.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.edu.ifsp.scl.lucas.foursquare.R
import br.edu.ifsp.scl.lucas.foursquare.dao.PlaceDAO
import br.edu.ifsp.scl.lucas.foursquare.utils.whenNotNull
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        GlobalScope.launch (Dispatchers.Main){
            val dao = PlaceDAO()
            val place = dao.load(intent.getStringExtra("name"))

            val v: String = ""

            whenNotNull(place){
                imageDetail.setImageBitmap(it.image)
                txtName.text = it.name
                txtType.text = it.type
                txtAtmosphere.text = it.atmosphere

                mMap.addMarker(MarkerOptions().position(it.location).title(it.name))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it.location, 17f))
            }
        }
    }
}


