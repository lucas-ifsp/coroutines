package br.edu.ifsp.scl.lucas.foursquare.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.edu.ifsp.scl.lucas.foursquare.R
import br.edu.ifsp.scl.lucas.foursquare.dao.PlaceDAO
import br.edu.ifsp.scl.lucas.foursquare.model.placeParameter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationManager : LocationManager? = null
    private var locationListener : LocationListener? = null
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.save_place, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.save_place){
            try {
                val dao = PlaceDAO()
                dao.save(placeParameter)
            }catch (e:Exception){
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location?) {
                if(location != null) {
                    var userLocation = LatLng(location.latitude, location.longitude)
                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(userLocation).title("Your location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17f))
                }
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }else{
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2, 2f, locationListener)
            var lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            mMap.clear()
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.latitude, lastLocation.longitude),17f))
        }

        mMap.setOnMapLongClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it!!).title(globalsName))
            latitude = it.latitude
            longitude = it.longitude
            Toast.makeText(applicationContext, "Now save this location!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 1)
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2, 2f, locationListener)
            }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
