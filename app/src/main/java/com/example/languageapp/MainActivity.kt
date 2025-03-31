package com.example.languageapp

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.multilangapp.R
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var flagImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        setContentView(
            R.layout
                .activity_main,)

        val helloText: TextView = findViewById(R.id.helloText)
        val languageSpinner: Spinner = findViewById(R.id.languageSpinner)
        flagImage = findViewById(R.id.flagImage)



        val languages = arrayOf(
            getString(R.string.ukrainian),
            getString(R.string.english),
            getString(R.string.german)
        )

        val languageCodes = arrayOf("uk", "en", "de")
        val flags = mapOf(
            "uk" to R.drawable.flag_ua,
            "en" to R.drawable.flag_usa,
            "de" to R.drawable.flag_de
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        languageSpinner.adapter = adapter

        val currentLanguage = Locale.getDefault().language
        languageSpinner.setSelection(languageCodes.indexOf(currentLanguage))
        flagImage.setImageResource(flags[currentLanguage] ?: R.drawable.flag_ua)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                if (Locale.getDefault().language != languageCodes[position]) {
                    setLocale(languageCodes[position])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val editor: SharedPreferences.Editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun loadLocale() {
        val prefs: SharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val language = prefs.getString("My_Lang", "en")
        if (language != null) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
}