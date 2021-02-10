package uz.myapps.alishernavoiy.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.myapps.alishernavoiy.R
import uz.myapps.alishernavoiy.adapter.ColorPickerAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditTextFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TextEditorDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mAddTextEditText: EditText
    lateinit var mAddTextDoneTextView: TextView
    lateinit var mInputMethodManager: InputMethodManager
    var mColorCode = 0
    lateinit var mTextEditor: TextEditor

    val TAG: String = TextEditorDialogFragment::class.java.getSimpleName()
    val EXTRA_INPUT_TEXT = "extra_input_text"
    val EXTRA_COLOR_CODE = "extra_color_code"

    interface TextEditor {
        fun onDone(inputText: String?, colorCode: Int)
    }


    override fun onStart() {
        super.onStart()
        //Make dialog full screen with transparent background
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window!!.setLayout(width, height)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_text, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text)
        mInputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv)

        //Setup the color picker for text color
        val addTextColorPickerRecyclerView: RecyclerView =
            view.findViewById(R.id.add_text_color_picker_recycler_view)
        val layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        addTextColorPickerRecyclerView.layoutManager = layoutManager
        addTextColorPickerRecyclerView.setHasFixedSize(true)
        val colorPickerAdapter = ColorPickerAdapter(requireActivity(), getDefaultColors(requireContext())!!)

        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(object : ColorPickerAdapter.OnColorPickerClickListener {
            override fun onColorPickerClickListener(colorCode: Int) {
                mColorCode = colorCode
                mAddTextEditText.setTextColor(colorCode)
            }
        })
        addTextColorPickerRecyclerView.adapter = colorPickerAdapter
        mAddTextEditText.setText(arguments!!.getString(EXTRA_INPUT_TEXT))
        mColorCode = arguments!!.getInt(EXTRA_COLOR_CODE)
        mAddTextEditText.setTextColor(mColorCode)
        mInputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            0
        )

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener(View.OnClickListener { view ->
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            dismiss()
            val inputText = mAddTextEditText.text.toString()
            EditImageFragment.newInstance(inputText)
            if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                mTextEditor.onDone(inputText, mColorCode)
            }
        })
    }

    fun setOnTextEditorListener(textEditor: TextEditor) {
        mTextEditor = textEditor
    }

    //Show dialog with provide text and text color
    fun show(
        appCompatActivity: AppCompatActivity,
        inputText: String,
        @ColorInt colorCode: Int
    ): TextEditorDialogFragment? {
        val args = Bundle()
        args.putString(EXTRA_INPUT_TEXT, inputText)
        args.putInt(EXTRA_COLOR_CODE, colorCode)
        val fragment = TextEditorDialogFragment()
        fragment.arguments = args
        fragment.show(appCompatActivity.supportFragmentManager, TAG)
        return fragment
    }
    fun getDefaultColors(context: Context): List<Int>? {
        var colorPickerColors = ArrayList<Int>()
        colorPickerColors.add(ContextCompat.getColor(context, R.color.blue_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.brown_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.green_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.orange_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.red_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.black))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.red_orange_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.sky_blue_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.violet_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.white))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.yellow_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.yellow_green_color_picker))
        return colorPickerColors
    }
    //Show dialog with default text input as empty and text color white
    public fun show(appCompatActivity: AppCompatActivity): TextEditorDialogFragment? {
        return show(
            appCompatActivity,
            "", ContextCompat.getColor(appCompatActivity, R.color.white)
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditTextFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TextEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}