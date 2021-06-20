package uz.alloma.behbudiy.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.alloma.behbudiy.R
import uz.alloma.behbudiy.adapter.ColorPickerAdapter

class TextEditorDialogFragment : DialogFragment(R.layout.fragment_edit_text) {
    private lateinit var mAddTextEditText: EditText
    private lateinit var mAddTextDoneTextView: TextView
    private lateinit var mInputMethodManager: InputMethodManager
    private var mColorCode = 0
    private lateinit var mTextEditor: TextEditor

    private val TAG: String = TextEditorDialogFragment::class.java.simpleName
    private val EXTRA_INPUT_TEXT = "extra_input_text"
    private val EXTRA_COLOR_CODE = "extra_color_code"

    interface TextEditor {
        fun onDone(inputText: String?, colorCode: Int)
    }


    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window!!.setLayout(width, height)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text)
        mInputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv)

        val addTextColorPickerRecyclerView: RecyclerView =
            view.findViewById(R.id.add_text_color_picker_recycler_view)
        val layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        addTextColorPickerRecyclerView.layoutManager = layoutManager
        addTextColorPickerRecyclerView.setHasFixedSize(true)
        val colorPickerAdapter =
            ColorPickerAdapter(requireActivity(), getDefaultColors(requireContext())!!)

        colorPickerAdapter.setOnColorPickerClickListener(object :
            ColorPickerAdapter.OnColorPickerClickListener {
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

        mAddTextDoneTextView.setOnClickListener { view ->
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            dismiss()
            val inputText = mAddTextEditText.text.toString()
            EditImageFragment.newInstance(inputText)
            if (!TextUtils.isEmpty(inputText)) {
                mTextEditor.onDone(inputText, mColorCode)
            }
        }
    }

    fun setOnTextEditorListener(textEditor: TextEditor) {
        mTextEditor = textEditor
    }

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

    private fun getDefaultColors(context: Context): List<Int>? {
        val colorPickerColors = ArrayList<Int>()
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

    fun show(appCompatActivity: AppCompatActivity): TextEditorDialogFragment? {
        return show(
            appCompatActivity,
            "", ContextCompat.getColor(appCompatActivity, R.color.white)
        )
    }
}