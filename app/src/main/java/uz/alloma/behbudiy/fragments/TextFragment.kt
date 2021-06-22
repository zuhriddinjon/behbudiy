package uz.alloma.behbudiy.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_text.*
import uz.alloma.behbudiy.R
import uz.alloma.behbudiy.models.Plan


class TextFragment : Fragment(R.layout.fragment_text) {
    private var plan: Plan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        plan = requireArguments().getParcelable("text")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name_text.text = plan?.name

        var fullText = ""
        plan?.text?.forEach { i ->
            fullText += i.plus("\n\r\r")
        }
        text.text = fullText.replace("\\n", "\n")
        bar_text.visibility = View.GONE
        rl_text.visibility = View.VISIBLE
    }

}