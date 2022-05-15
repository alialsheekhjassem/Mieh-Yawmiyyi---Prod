package com.magma.miyyiyawmiyyi.android.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import java.util.*

object LocalHelper {

    private lateinit var sharedPreferences: SharedPreferences

    fun setLocale(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLayoutDirection(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.locale = locale
            configuration.setLocale(locale)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(configuration)
        } else {
            resources.updateConfiguration(configuration, resources.displayMetrics)
            context
        }
    }

    fun onAttach(context: Context): Context? {
        sharedPreferences = context.getSharedPreferences(Const.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return setLocale(
            context,
            sharedPreferences.getString(Const.PREF_LANG, "ar") ?: "ar"
        )
    }

    fun onResume(context: Context): Context? {
        sharedPreferences = context.getSharedPreferences(Const.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return setLocale(
            context,
            sharedPreferences.getString(Const.PREF_LANG, "ar") ?: "ar"
        )
    }
}