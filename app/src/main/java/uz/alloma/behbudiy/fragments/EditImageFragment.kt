package uz.alloma.behbudiy.fragments

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import kotlinx.android.synthetic.main.fragment_edit_image.*
import uz.alloma.behbudiy.R
import uz.alloma.behbudiy.fragments.TextEditorDialogFragment.TextEditor
import java.io.File
import java.io.IOException

private const val ARG_PARAM1 = "editImage_fragment_param"

open class EditImageFragment : Fragment(R.layout.fragment_edit_image) {
    private var param1: Int? = null
    private var mSaveImageUri: Uri? = null
    private lateinit var mPhotoEditor: PhotoEditor
    private lateinit var mPhotoEditorView: PhotoEditorView
    private var inputtext: String = ""
    private val READ_WRITE_STORAGE = 52
    private val FILE_PROVIDER_AUTHORITY = "uz.alloma.behbudiy.fileprovider"
    private lateinit var mProgressDialog: ProgressDialog

    @DrawableRes
    private var drawable: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            param1 = it.getInt("drawable")
        }
        when (param1) {
            1 ->
                drawable = R.drawable.img1
            2 ->
                drawable = R.drawable.img3
            3 ->
                drawable = R.drawable.img4
            4 ->
                drawable = R.drawable.img8
            5 ->
                drawable = R.drawable.img6
            6 ->
                drawable = R.drawable.img7
            else ->
                drawable = R.drawable.img10
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoEditorView.source.setImageResource(drawable!!)
        val mTextRobotoTf = ResourcesCompat.getFont(requireContext(), R.font.ralewaymedium)

        mPhotoEditorView = photoEditorView
        mPhotoEditor = PhotoEditor.Builder(requireContext(), mPhotoEditorView)
            .setPinchTextScalable(true)
            .setDefaultTextTypeface(mTextRobotoTf)
            .build()


        val textEditorDialogFragment: TextEditorDialogFragment = TextEditorDialogFragment().show(
            requireActivity() as AppCompatActivity
        )!!
        textEditorDialogFragment.setOnTextEditorListener(object : TextEditor {
            override fun onDone(inputText: String?, colorCode: Int) {
                inputtext = inputText!!
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                mPhotoEditor.addText(inputText, styleBuilder)
                txtCurrentTool.setText(R.string.label_text)
            }
        })



        mPhotoEditor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onEditTextChangeListener(
                iew: View,
                text: String,
                colorCode: Int
            ) {
                val textEditorDialogFragment: TextEditorDialogFragment =
                    TextEditorDialogFragment().show(AppCompatActivity(), text, colorCode)!!
                textEditorDialogFragment.setOnTextEditorListener(object :
                    TextEditor {
                    override fun onDone(inputText: String?, colorCode: Int) {
                        val styleBuilder = TextStyleBuilder()
                        styleBuilder.withTextColor(colorCode)
                        mPhotoEditor.editText(iew, inputText, styleBuilder)
                    }
                })
            }

            override fun onStartViewChangeListener(viewType: ViewType?) {

            }

            override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

            }

            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

            }

            override fun onStopViewChangeListener(viewType: ViewType?) {

            }
        })

        val imgSave = imgSave
        imgSave.setOnClickListener {
            saveImage()
        }
        val imgShare = imgShare
        imgShare.setOnClickListener {
            shareImage()
        }
    }

    private fun shareImage() {
        if (mSaveImageUri == null) {
            showSnackbar(getString(R.string.msg_save_image_to_share))
            return
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(mSaveImageUri!!))
        intent.putExtra(Intent.EXTRA_TITLE, "Behbudiy")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "$inputtext #Behbudiy\nhttps://gitlab.com/zuhriddinjon"
        )
        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)))
    }


    private fun buildFileProviderUri(uri: Uri): Uri? {
        return FileProvider.getUriForFile(
            requireContext(),
            FILE_PROVIDER_AUTHORITY,
            File(uri.path!!)
        )
    }

    private fun saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading("Saving...")
            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + ""
                        + System.currentTimeMillis() + ".png"
            )
            try {
                file.createNewFile()
                val saveSettings = SaveSettings.Builder()
                    .setClearViewsEnabled(true)
                    .setTransparencyEnabled(true)
                    .build()
                mPhotoEditor.saveAsFile(
                    file.absolutePath,
                    saveSettings,
                    object : OnSaveListener {
                        override fun onSuccess(imagePath: String) {
                            hideLoading()
                            showSnackbar("Rasm yuklandi")
                            mSaveImageUri = Uri.fromFile(File(imagePath))
                            mPhotoEditorView.source.setImageURI(mSaveImageUri)
                        }

                        override fun onFailure(exception: Exception) {
                            hideLoading()
                            showSnackbar("Rasm yuklanmadi!")
                        }
                    })
            } catch (e: IOException) {
                e.printStackTrace()
                hideLoading()
                showSnackbar(e.message!!)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_WRITE_STORAGE -> isPermissionGranted(
                grantResults[0] == PackageManager.PERMISSION_GRANTED,
                permissions[0]
            )
        }
    }

    private fun isPermissionGranted(
        isGranted: Boolean,
        permission: String?
    ) {
        if (isGranted) {
            saveImage()
        }
    }

    private fun showLoading(message: String) {
        mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog.setMessage(message)
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()
    }

    private fun requestPermission(permission: String): Boolean {
        val isGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(permission),
                READ_WRITE_STORAGE
            )
        }
        return isGranted
    }

    fun hideLoading() {
        mProgressDialog.dismiss()
    }

    protected fun showSnackbar(message: String) {
        val view: View = requireActivity().findViewById(android.R.id.content)
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            EditImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}