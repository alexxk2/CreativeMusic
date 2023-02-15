package com.example.layoutmake
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaLibraryButton = findViewById<Button>(R.id.media_library_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        val searchIntent = Intent(this, SearchActivity::class.java)

        val buttonClickListener: OnClickListener = object : OnClickListener {
            override fun onClick(p0: View?) {
               startActivity(searchIntent)
            }
        }
        searchButton.setOnClickListener(buttonClickListener)

        mediaLibraryButton.setOnClickListener {
            val mediaIntent  = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }


}