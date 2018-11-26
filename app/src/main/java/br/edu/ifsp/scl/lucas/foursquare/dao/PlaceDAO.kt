package br.edu.ifsp.scl.lucas.foursquare.dao

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import br.edu.ifsp.scl.lucas.foursquare.model.Place
import br.edu.ifsp.scl.lucas.foursquare.utils.whenNotNull
import com.google.android.gms.maps.model.LatLng
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import java.io.ByteArrayOutputStream

class PlaceDAO {

    fun save(dto : Place) {
        val parseObject = ParseObject("Locations")

        with(dto) {
            parseObject.put("name", name)
            parseObject.put("type", type)
            parseObject.put("atmosphere", atmosphere)
            parseObject.put("latitude", location.latitude)
            parseObject.put("longitude", location.longitude)

            //saving a Bitmap as a File
            whenNotNull(image){
                val byteArrayOutputStream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
                val bytes = byteArrayOutputStream.toByteArray()
                val parseFile = ParseFile("image.png", bytes)
                parseObject.put("image", parseFile)
            }
            parseObject.saveInBackground { e ->
                if(e != null)
                    throw Exception(e.localizedMessage)
                else
                    Log.e("myLog", "object {$this} saved in database.")
            }
        }
    }

    fun load(name: String) : Place? {
        val parseQuery = ParseQuery<ParseObject>("Locations")
        parseQuery.whereEqualTo("name", name)

        return try{
            parseToPlace(parseQuery.find()[0])
        }catch (e : ParseException){
            Log.e("myLog", e.localizedMessage)
            null
        }
    }

    // READS ALL DATA FROM FROM CLOUD (PARSE PLATFORM)
    // BECAUSE I USE A REMOTE CALL AS SYNC, THIS METHOD MUST BE CALLED ASYNC
    fun loadAll() : ArrayList<Place>{
        val parseQuery = ParseQuery<ParseObject>("Locations")
        val places = ArrayList<Place>()
        parseQuery.find().forEach { places.add(parseToPlace(it)) } // CALL THIS SLOW RESULT QUERY AS SYNC
        Log.e("myLog", "Two")
        return places
    }

    fun parseToPlace(obj: ParseObject):Place{
        lateinit var place: Place

        with(obj) {
            val ll = LatLng(get("latitude") as Double, get("longitude") as Double)
            val rawImage = get("image") as ParseFile
            val data = rawImage.data // it must be called async
            val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)

            place = Place(get("name") as String, get("type") as String, get("atmosphere") as String, bmp, ll)
        }
        return place
    }

    /*fun loadAll() : ArrayList<Place>{
        val parseQuery = ParseQuery<ParseObject>("Locations")
        val places = ArrayList<Place>()

        parseQuery.findInBackground { objects, e ->
            if (e != null)
                throw Exception(e.localizedMessage)
            else {
                for (o in objects) {
                    places.add(parseToPlace(o))
                    Log.e("myLog", "Size {${places.size}} - $o")
                }
            }
        }
        return places
    }
    */
    /*fun parseToPlace(obj: ParseObject):Place{
        lateinit var place: Place

        with(obj) {
            val ll = LatLng(get("latitude") as Double, get("longitude") as Double)
            val rawImage = get("image") as ParseFile

            rawImage.getDataInBackground { data, e ->
                if (e != null)
                    throw Exception(e.localizedMessage)
                else{
                    val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
                    place = Place(get("name") as String, get("type") as String, get("atmosphere") as String, bmp,ll)
                    Log.e("myLog", place.toString())
                }
            }
        }
        return place
    }*/

/*    fun load(name: String) : Place {
        val parseQuery = ParseQuery<ParseObject>("Locations")
        parseQuery.whereEqualTo("name", name)

        lateinit var place: Place

        parseQuery.findInBackground { objects, e ->
            if (e != null)
                throw Exception(e.localizedMessage)
            else if (objects.isNotEmpty()) {
                place = parseToPlace(objects[0])
            }
        }
        return place
    }*/
}
