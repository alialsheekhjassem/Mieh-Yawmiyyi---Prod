package com.magma.miyyiyawmiyyi.android.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.magma.miyyiyawmiyyi.android.R
import java.io.IOException
import java.nio.charset.StandardCharsets


object CommonUtils {
    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String?): String {
        val manager = context.assets
        val `is` = manager.open(jsonFileName!!)
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        Log.d("TAG", "loadJSONFromAsset: " + String(buffer, StandardCharsets.UTF_8))
        return String(buffer, StandardCharsets.UTF_8)
    }

    fun showLoadingDialog(context: Context?): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.dialog_loading)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }
}
