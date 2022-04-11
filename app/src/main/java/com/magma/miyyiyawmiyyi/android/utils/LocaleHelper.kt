package com.magma.miyyiyawmiyyi.android.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import java.util.*


object LocalHelper {
    var locale: Locale? = null
    fun onCreate(context: Context) {
        val sharedPreferences = context.getSharedPreferences(Const.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val defaultLang = "ar"/*Locale.getDefault().language*/
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
            updateResourcesLegacy(context, language)
        } catch (ignored: Exception) {
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String) {
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