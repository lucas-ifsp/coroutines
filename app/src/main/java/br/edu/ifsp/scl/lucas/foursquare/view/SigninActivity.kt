package br.edu.ifsp.scl.lucas.foursquare.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import br.edu.ifsp.scl.lucas.foursquare.R
import com.parse.ParseAnalytics
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        ParseAnalytics.trackAppOpenedInBackground(intent)


    }

    fun signIn(view: View){
        ParseUser.logInInBackground(usernameText.text.toString(), passwordText.text.toString()) {user, e ->
            if(e != null){
                Log.e("lucas", usernameText.text.toString() + " --- "+ passwordText.text.toString())
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                //Toast.makeText(applicationContext, "Welcome ${user.username.toString()}", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, LocationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun singUp (view:View){
        val user = ParseUser()
        user.username = usernameText.text.toString()
        user.setPassword(passwordText.text.toString())

        user.signUpInBackground{e: ParseException? ->
            if(e != null) {
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
                Log.e("lucas",e.localizedMessage)
            }
            else {
                Toast.makeText(applicationContext, "User signed up!", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, LocationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
