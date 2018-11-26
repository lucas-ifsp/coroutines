package br.edu.ifsp.scl.lucas.foursquare.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import br.edu.ifsp.scl.lucas.foursquare.R
import br.edu.ifsp.scl.lucas.foursquare.dao.PlaceDAO
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LocationActivity : AppCompatActivity() {

    val namesList = ArrayList<String>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_place, menu)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, namesList)
        locationsListView.adapter = arrayAdapter

        Log.e("myLog", "One")
        loadListView(arrayAdapter)
        Log.e("myLog", "Four")

        locationsListView.setOnItemClickListener { parent, view, i, id ->
            val intent = Intent(applicationContext, DetailsActivity::class.java)
            intent.putExtra("name", namesList[i])
            startActivity(intent)
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun loadListView(adapter : ArrayAdapter<String>) = runBlocking{
        val dao = PlaceDAO()

        //CALL THIS FUCKING SLOW BLOCK ASYNC
        GlobalScope.launch(Dispatchers.Main){
            namesList.addAll(dao.loadAll().map{ it.name }) // TWO
            adapter.notifyDataSetChanged() // in this scope, this works as sync
        }
       Log.e("myLog", "Three")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.item_add){
            val intent = Intent(applicationContext, NewLocationActivity::class.java)
            startActivity(intent)
        }
         return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
    }
}
