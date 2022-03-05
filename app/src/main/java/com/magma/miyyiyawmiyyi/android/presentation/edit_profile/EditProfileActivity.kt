package com.magma.miyyiyawmiyyi.android.presentation.edit_profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.magma.miyyiyawmiyyi.android.utils.*
import dagger.android.AndroidInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.ActivityEditProfileBinding
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.profile.ProfileViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EditProfileActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var currentPhotoPath: String

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                binding.btnCamera.performClick()
            }
        }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showImage()
            }
        }

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        //lang
        LocalHelper.onCreate(this)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.stringRuleUtil = StringRuleUtil

        setUpAutoCompleteText()
        setUpListeners()

    }

    private fun setUpAutoCompleteText() {
        val sexArray = resources.getStringArray(R.array.sex)
        val autoCompleteText: AutoCompleteTextView = binding.spinnerSex
        val adapter = ArrayAdapter<CharSequence>(
            this,
            getAdapterItemLayout(), sexArray
        )
        autoCompleteText.setText(sexArray[0])
        autoCompleteText.setAdapter(adapter)
    }

    private fun setUpListeners() {
        binding.btnBack.setOnClickListener(this)
        binding.btnCamera.setOnClickListener(this)
        binding.txtBirthdateDay.setOnClickListener(this)
        binding.txtBirthdateMonth.setOnClickListener(this)
        binding.txtBirthdateYear.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    private fun getAdapterItemLayout(): Int {
        return R.layout.support_simple_spinner_dropdown_item
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                finish()
            }
            R.id.btn_camera -> {
                startCamera()
            }
            R.id.txt_birthdate_day -> {
                showDateDialog()
            }
            R.id.txt_birthdate_month -> {
                showDateDialog()
            }
            R.id.txt_birthdate_year -> {
                showDateDialog()
            }
            R.id.btn_save -> {

            }
            R.id.btn_cancel -> {
                finish()
            }
        }
    }

    private fun showImage() {
        Glide.with(this).load(currentPhotoPath)
            .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
            .into(binding.imgProfile)
    }

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(
                this@EditProfileActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this@EditProfileActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted!. take a picture
            dispatchTakePictureIntent()
        } else {
            // request a permission
            requestMultiplePermissions.launch(permissions)
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            try {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this@EditProfileActivity,
                        "magma.global.restaurant.utils.ExtendFileProvider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                }
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this, getString((R.string.application_not_found)), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun showDateDialog() {
        val datePicker: DialogFragment = DatePickerParentFragment()
        datePicker.show(supportFragmentManager, Const.TAG_DatePickerParentFragment)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month
        c[Calendar.DAY_OF_MONTH] = dayOfMonth
        //val currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.time)
        binding.txtBirthdateDay.text = dayOfMonth.toString()
        binding.txtBirthdateMonth.text = month.plus(1).toString()
        binding.txtBirthdateYear.text = year.toString()
    }

}