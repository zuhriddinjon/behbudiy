package uz.myapps.alishernavoiy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.fragment_image.view.*
import uz.myapps.alishernavoiy.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val root = inflater.inflate(R.layout.fragment_image, container, false)
        val editImageFragment = EditImageFragment()
        root.img1.setOnClickListener {
            var bundleOf= bundleOf("drawable" to 1)
            editImageFragment.arguments= bundleOf
            fragmentManager?.beginTransaction()?.addToBackStack("image")?.replace(R.id.container,editImageFragment)?.commit()
        }
        root.img2.setOnClickListener {
            var bundleOf= bundleOf("drawable" to 2)
            editImageFragment.arguments= bundleOf
            fragmentManager?.beginTransaction()?.addToBackStack("image")?.replace(R.id.container,editImageFragment)?.commit()
        }
        root.img3.setOnClickListener {
            var bundleOf= bundleOf("drawable" to 3)
            editImageFragment.arguments= bundleOf
            fragmentManager?.beginTransaction()?.addToBackStack("image")?.replace(R.id.container,editImageFragment)?.commit()
        }
        root.img4.setOnClickListener {
            var bundleOf= bundleOf("drawable" to 4)
            editImageFragment.arguments= bundleOf
            fragmentManager?.beginTransaction()?.addToBackStack("image")?.replace(R.id.container,editImageFragment)?.commit()
        }
        root.img5.setOnClickListener {
            var bundleOf= bundleOf("drawable" to 5)
            editImageFragment.arguments= bundleOf
            fragmentManager?.beginTransaction()?.addToBackStack("image")?.replace(R.id.container,editImageFragment)?.commit()
        }
        root.img6.setOnClickListener {
            var bundleOf= bundleOf("drawable" to 6)
            editImageFragment.arguments= bundleOf
            fragmentManager?.beginTransaction()?.addToBackStack("image")?.replace(R.id.container,editImageFragment)?.commit()
        }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}