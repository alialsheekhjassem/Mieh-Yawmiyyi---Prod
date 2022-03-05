/*
 * Created by Ali Al-Sheekh Jassem on 4/21/21 9:16 PM
 * Copyright (c) 2021 . All rights reserved .
 * Last modified 4/21/21 8:37 PM
 */
package com.magma.miyyiyawmiyyi.android.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textfield.TextInputLayout
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.model.Title
import com.magma.miyyiyawmiyyi.android.utils.StringRuleUtil.StringRule
import java.util.*

object BindingUtils {
    private const val TAG = "BindingUtils"

    @JvmStatic
    @BindingAdapter("validation", "errorMsg")
    fun setErrorEnable(
        textInputLayout: TextInputLayout,
        stringRule: StringRule,
        errorMsg: String?
    ) {
        if (textInputLayout.editText != null) textInputLayout.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    val text = textInputLayout.editText!!.text.toString()
                    val notEmpty =
                        textInputLayout.context.getString(R.string.field_can_not_be_empty)
                    when {
                        text.isEmpty() -> textInputLayout.error =
                            notEmpty
                        stringRule.validate(
                            textInputLayout.editText!!.text
                        ) -> textInputLayout.error = errorMsg
                        else -> textInputLayout.error = null
                    }
                }
            })
    }

    @JvmStatic
    @BindingAdapter("text_translate")
    fun setTextTranslate(textView: TextView, title: Title?) {
        Log.d(TAG, "setTextTranslate: $title")
        if (title == null) return
        val lang = Locale.getDefault().language
        Log.d(TAG, "setTextTranslate: $lang")
        when (lang) {
            "en" -> textView.text = title.en
            "ar" -> textView.text = title.ar
        }
    }

    @JvmStatic
    @BindingAdapter("confirm_password", "errorMsg")
    fun setErrorEnable(
        textInputLayoutConfirmPassword: TextInputLayout,
        edtPassword: Editable,
        errorMsg: String?
    ) {
        if (textInputLayoutConfirmPassword.editText != null) textInputLayoutConfirmPassword.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    val text = editable.toString()
                    Log.d(TAG, "CCCP afterTextChanged: $text")
                    Log.d(TAG, "CCCP afterTextChanged: $errorMsg")
                    val notEmpty =
                        textInputLayoutConfirmPassword.context.getString(R.string.field_can_not_be_empty)
                    when {
                        text.isEmpty() -> textInputLayoutConfirmPassword.error = notEmpty
                        text != edtPassword.toString() ->
                            textInputLayoutConfirmPassword.error = errorMsg
                        else -> textInputLayoutConfirmPassword.error = null
                    }
                }
            })
    }

    @JvmStatic
    @BindingAdapter("image_url")
    fun setImageUrl(imageView: ImageView, imageUri: String?) {
        Log.d(TAG, "setImageHostUrl: $imageUri")
        Glide.with(imageView.context)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_logo)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("image_drawable")
    fun setImageRes(imageView: ImageView, imageDrawable: Drawable) {
        Log.d(TAG, "setImageRes: $imageDrawable")
        Glide.with(imageView)
            .load(imageDrawable)
            .fitCenter().into(imageView)
    }

    @JvmStatic
    @BindingAdapter("image_res")
    fun setImageRes(imageView: ImageView, imageRes: Int) {
        Log.d(TAG, "setImageRes: $imageRes")
        Glide.with(imageView)
            .load(imageRes)
            .fitCenter().into(imageView)
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}