package com.amalwin.favdishapplication.views.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.application.FavDishApplication
import com.amalwin.favdishapplication.databinding.ActivityAddUpdateDishBinding
import com.amalwin.favdishapplication.databinding.DialogCustomImageSelectionBinding
import com.amalwin.favdishapplication.databinding.DialogCustomListBinding
import com.amalwin.favdishapplication.model.entities.FavDish
import com.amalwin.favdishapplication.utils.Constants
import com.amalwin.favdishapplication.viewmodel.FavDishAddUpdateViewModel
import com.amalwin.favdishapplication.viewmodel.FavDishAddUpdateViewModelFactory
import com.amalwin.favdishapplication.views.adapters.CustomListItemAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var activityAddUpdateDishBinding: ActivityAddUpdateDishBinding
    private var imagePath: String? = ""
    private lateinit var customListDialog: Dialog
    private val favDishViewModel: FavDishAddUpdateViewModel by viewModels {
        FavDishAddUpdateViewModelFactory((application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddUpdateDishBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(activityAddUpdateDishBinding.root)
        setActionBar()



        activityAddUpdateDishBinding.apply {
            this.ivTakePicture.setOnClickListener(this@AddUpdateDishActivity)
            this.etType.setOnClickListener(this@AddUpdateDishActivity)
            this.etCategory.setOnClickListener(this@AddUpdateDishActivity)
            this.etCookingTime.setOnClickListener(this@AddUpdateDishActivity)
            this.btnAddDish.setOnClickListener(this@AddUpdateDishActivity)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_take_picture -> showCustomDialogForImageSelection()
            R.id.et_type -> showCustomListDialog(
                resources.getString(R.string.type_select_title),
                Constants.dishTypes(),
                Constants.DISH_TYPE
            )
            R.id.et_cooking_time -> showCustomListDialog(
                resources.getString(R.string.cooking_time_select_title),
                Constants.dishCookTime(),
                Constants.DISH_COOKING_TIMELINE
            )
            R.id.et_category -> showCustomListDialog(
                resources.getString(R.string.category_select_title),
                Constants.dishCategories(),
                Constants.DISH_CATEGORY
            )
            R.id.btn_add_dish -> {
                val title = activityAddUpdateDishBinding.etTitle.text.toString().trim { it <= ' ' }
                val type = activityAddUpdateDishBinding.etType.text.toString().trim { it <= ' ' }
                val category = activityAddUpdateDishBinding.etCategory.text.toString().trim { it <= ' ' }
                val ingredients =
                    activityAddUpdateDishBinding.etIngredients.text.toString().trim { it <= ' ' }
                val cookingTime =
                    activityAddUpdateDishBinding.etCookingTime.text.toString().trim { it <= ' ' }
                val directionsToCook =
                    activityAddUpdateDishBinding.etDirectionToCook.text.toString().trim { it <= ' ' }

                when {
                    TextUtils.isEmpty(imagePath) -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_message_image),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    TextUtils.isEmpty(title) ->
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_message_title),
                            Toast.LENGTH_LONG
                        ).show()

                    TextUtils.isEmpty(type) ->
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_message_type),
                            Toast.LENGTH_LONG
                        ).show()

                    TextUtils.isEmpty(category) ->
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_message_category),
                            Toast.LENGTH_LONG
                        ).show()

                    TextUtils.isEmpty(ingredients) ->
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_message_ingredients),
                            Toast.LENGTH_LONG
                        ).show()

                    TextUtils.isEmpty(cookingTime) ->
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_message_cookingtime),
                            Toast.LENGTH_LONG
                        ).show()

                    TextUtils.isEmpty(directionsToCook) ->
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_message_direction_to_cook),
                            Toast.LENGTH_LONG
                        ).show()

                    else -> {
                        favDishViewModel.insertFavDishDetails(
                            FavDish(
                                0,
                                imagePath!!,
                                Constants.DISH_IMAGE_SOURCE_LOCAL,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTime,
                                directionsToCook,
                                false
                            )
                        )
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_message_validation_success),
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("Insertion", "Success !")
                        finish()
                    }


                }
            }


        }
    }

    fun setActionBar() {
        setSupportActionBar(activityAddUpdateDishBinding.toolbarAddUpdateDish)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activityAddUpdateDishBinding.toolbarAddUpdateDish.setNavigationOnClickListener() {
            onBackPressed()
        }
    }

    fun showCustomDialogForImageSelection() {
        val customDialog = Dialog(this@AddUpdateDishActivity)
        val customDialogBinding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        customDialog.setContentView(customDialogBinding.root)
        customDialog.show()

        @Suppress("DEPRECATION")
        customDialogBinding.tvCamera.setOnClickListener() {
            Dexter.withContext(this@AddUpdateDishActivity).withPermissions(
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report.let {
                        if (report!!.areAllPermissionsGranted()) {
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                        } else {
                            showRationalDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: MutableList<PermissionRequest>?,
                    permissionToken: PermissionToken?
                ) {
                    showRationalDialog()
                }
            }).onSameThread().check()
            customDialog.dismiss()
        }

        customDialogBinding.tvGallery.setOnClickListener() {
            //Toast.makeText(this@AddUpdateDishActivity, "Gallery", Toast.LENGTH_LONG).show()
            Dexter.withContext(this@AddUpdateDishActivity).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@AddUpdateDishActivity,
                        "User denied permission !",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequested: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    showRationalDialog()
                }
            }).onSameThread().check()
            customDialog.dismiss()
        }
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1
        const val GALLERY_REQUEST_CODE = 2
        const val FAV_DISH_IMAGE_DIRECTORY = "FAV_DISH_DIRECTORY"
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                data?.extras?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                    //activityAddUpdateDishBinding.ivPicture.setImageBitmap(thumbnail)

                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(activityAddUpdateDishBinding.ivPicture)

                    imagePath = saveImageToInternalStorage(thumbnail)
                    imagePath.let {
                        Log.i("TAG", it!!)
                    }

                    activityAddUpdateDishBinding.ivTakePicture.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.ic_vector_edit
                        )
                    )
                }
            } else {
                if (requestCode == GALLERY_REQUEST_CODE) {
                    val uri: Uri? = data?.data
                    //activityAddUpdateDishBinding.ivPicture.setImageURI(uri)

                    Glide.with(this).load(uri).listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource.let { it ->
                                val bitmap: Bitmap = it?.toBitmap()!!
                                imagePath = saveImageToInternalStorage(bitmap)
                                imagePath.let {
                                    Log.i("TAG", it!!)
                                }
                            }
                            return false
                        }

                    }).centerCrop()
                        .into(activityAddUpdateDishBinding.ivPicture)

                    activityAddUpdateDishBinding.ivTakePicture.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.ic_vector_edit
                        )
                    )
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(
                this@AddUpdateDishActivity,
                "User cancelled the operation !",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun showRationalDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage("Moving to settings to enabling the permissions as the requested permission are not provided !")
            .setPositiveButton("Go to settings") { _, _ ->
                val settingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                settingIntent.data = uri
                startActivity(settingIntent)
            }.setNeutralButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        alertDialog.show()
    }

    fun saveImageToInternalStorage(bitmap: Bitmap): String? {
        // ContextWrapper with the application context constructor identifies the
        // bitmap belonging to which application
        val wrapper: ContextWrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(FAV_DISH_IMAGE_DIRECTORY, MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()?.toString()}.jpg")
        try {
            val outputStream: FileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        return file.absolutePath
    }

    fun showCustomListDialog(
        title: String,
        listOfElements: ArrayList<String>,
        selectedList: String
    ) {
        customListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        customListDialog.setContentView(binding.root)

        binding.tvDlgCustomTitle.text = title
        binding.rvListDialog.layoutManager = LinearLayoutManager(this)

        val customListItemAdapter: CustomListItemAdapter =
            CustomListItemAdapter(
                listOfElements,
                selectedList
            ) { selectedItem: String, String -> onListItemSelected(selectedItem, selectedList) }
        binding.rvListDialog.adapter = customListItemAdapter

        customListDialog.show()

    }

    private fun onListItemSelected(itemSelected: String, itemSelectedCategory: String) {
        when (itemSelectedCategory) {
            Constants.DISH_TYPE -> activityAddUpdateDishBinding.etType.setText(itemSelected)
            Constants.DISH_CATEGORY -> activityAddUpdateDishBinding.etCategory.setText(itemSelected)
            Constants.DISH_COOKING_TIMELINE -> activityAddUpdateDishBinding.etCookingTime.setText(
                itemSelected
            )
        }
        Toast.makeText(
            this,
            "Selected Item : $itemSelected and Selected Category : $itemSelectedCategory",
            Toast.LENGTH_LONG
        ).show()

        if (customListDialog.isShowing) customListDialog.dismiss()
    }
}


