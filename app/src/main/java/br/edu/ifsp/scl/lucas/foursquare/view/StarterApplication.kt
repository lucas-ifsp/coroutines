package br.edu.ifsp.scl.lucas.foursquare.view

import android.app.Application
import com.parse.Parse
import com.parse.ParseACL
import com.parse.ParseUser

class StarterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //Enable Local Datastore
        Parse.enableLocalDatastore(this)

        // Add your initialization code here
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("593bc035d885e7ecc95c08ded5d4f64ade645b57")
                // if defined
                .clientKey("1f894707da2dba8069d8774ef3c8514c7fdac401")
                .server("http://18.191.249.113:80/parse")
                .build()
        )

        ParseUser.logOut()
        ParseUser.enableAutomaticUser()

        val defaultUser = ParseACL()
        defaultUser.publicReadAccess = true
        defaultUser.publicWriteAccess = true

        ParseACL.setDefaultACL(defaultUser, true)

    }
}