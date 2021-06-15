package uz.alloma.behbudiy.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_image.*
import uz.alloma.behbudiy.R

class ImageFragment : Fragment(R.layout.fragment_image), View.OnClickListener {

    private val editImageFragment: EditImageFragment by lazy { EditImageFragment() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img1.setOnClickListener(this)
        img2.setOnClickListener(this)
        img3.setOnClickListener(this)
        img4.setOnClickListener(this)
        img5.setOnClickListener(this)
        img6.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img1 -> replaceFragment(1)
            R.id.img2 -> replaceFragment(2)
            R.id.img3 -> replaceFragment(3)
            R.id.img4 -> replaceFragment(4)
            R.id.img5 -> replaceFragment(5)
            R.id.img6 -> replaceFragment(6)
        }

    }

    private fun replaceFragment(i: Int) {
        val bundleOf = bundleOf("drawable" to i)
        editImageFragment.arguments = bundleOf
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, editImageFragment)
            .addToBackStack("image")
            .commit()
    }

}