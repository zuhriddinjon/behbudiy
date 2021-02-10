package uz.myapps.alishernavoiy.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import kotlinx.android.synthetic.main.fragment_edit_image.view.*
import uz.myapps.alishernavoiy.R
import uz.myapps.alishernavoiy.fragments.TextEditorDialogFragment.TextEditor
import java.io.File
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [EditImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    var mSaveImageUri: Uri?=null
    lateinit var mPhotoEditor: PhotoEditor
    lateinit var mPhotoEditorView: PhotoEditorView
    var inputtext:String=""
    val READ_WRITE_STORAGE = 52
    val FILE_PROVIDER_AUTHORITY = "uz.myapps.alishernavoiy.fileprovider"
    lateinit var mProgressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_edit_image, container, false)
        var drawable = R.drawable.img1
        when(arguments?.getInt("drawable")!!){
            1->
                drawable = R.drawable.img1
            2->
                drawable = R.drawable.img3
            3->
                drawable = R.drawable.img4
            4->
                drawable = R.drawable.img8
            5->
                drawable = R.drawable.img6
            6->
                drawable = R.drawable.img7
        }

        root.photoEditorView.source.setImageResource(drawable!!)
        val mTextRobotoTf = ResourcesCompat.getFont(requireContext(), R.font.ralewaymedium)
        mPhotoEditorView = root.photoEditorView
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
                root.txtCurrentTool.setText(R.string.label_text)
            }
        })



        mPhotoEditor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onEditTextChangeListener(
                rootView: View,
                text: String,
                colorCode: Int
            ) {
                val textEditorDialogFragment: TextEditorDialogFragment =
                    TextEditorDialogFragment().show(AppCompatActivity(), text, colorCode)!!
                textEditorDialogFragment.setOnTextEditorListener(object :
                    TextEditorDialogFragment.TextEditor {
                    override fun onDone(inputText: String?, colorCode: Int) {
                        val styleBuilder = TextStyleBuilder()
                        styleBuilder.withTextColor(colorCode)
                        mPhotoEditor.editText(rootView, inputText, styleBuilder)
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

        var imgSave = root.imgSave
        imgSave.setOnClickListener {
            saveImage()
        }
        var imgShare = root.imgShare
        imgShare.setOnClickListener {
            shareImage()
        }
        return root
    }


    private fun shareImage() {
        if (mSaveImageUri == null) {
            showSnackbar(getString(R.string.msg_save_image_to_share))
            return
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(mSaveImageUri!!))
        intent.putExtra(Intent.EXTRA_TITLE,"Alisher Navoiy")
        intent.putExtra(Intent.EXTRA_TEXT,inputtext +" #AlisherNavoiy"+"\nhttps://gitlab.com/Robiya160198")
        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)))
    }


    private fun buildFileProviderUri(uri: Uri): Uri? {
        return FileProvider.getUriForFile(
            requireContext(),
            FILE_PROVIDER_AUTHORITY,
            File(uri.path)
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun saveImage() {
    if(requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
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
                        mPhotoEditorView.getSource().setImageURI(mSaveImageUri)
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
                grantResults[0] === PackageManager.PERMISSION_GRANTED,
                permissions[0]
            )
        }
    }
    fun isPermissionGranted(
        isGranted: Boolean,
        permission: String?
    ) {
        if (isGranted) {
            saveImage()
        }
    }
     fun showLoading(message: String) {
        mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog.setMessage(message)
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()
    }
    fun requestPermission(permission: String): Boolean {
        val isGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) === PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(permission),
                READ_WRITE_STORAGE
            )
        }
        return isGranted
    }

     fun hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss()
        }
    }

    protected fun showSnackbar(message: String) {
        val view: View = requireActivity().findViewById(android.R.id.content)
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            EditImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}