package com.magma.miyyiyawmiyyi.android.utils

import android.content.Context
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
import java.util.*

object LocalHelper {
    var locale: Locale? = null
    fun onCreate(context: Context) {
        val sharedPreferences = context.getSharedPreferences(Const.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val defaultLang = Locale.getDefault().language
        val lang : String = sharedPreferences.getString(Const.PREF_LANG, defaultLang)!!
        setLocale(context, lang)
    }

    fun setLocale(context: Context, language: String) {
//        persist(language);
        try {
            val handler = Handler()
            handler.post {
                //offline change language
                try {
                    val sharedPreferences = context.getSharedPreferences(Const.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
                    sharedPreferences.edit().putString(Const.PREF_LANG, language).apply()
                } catch (ignored: Exception) {
                }
            }
            updateResources(context, language)
        } catch (ignored: Exception) {
        }
    }

    private fun updateResources(context: Context, language: String) {
        locale = Locale(language)
        Locale.setDefault(locale!!)
        try {
            val resources = context.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            context.createConfigurationContext(configuration)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        } catch (ignored: Exception) {
        }
    }
}