package uz.myapps.alishernavoiy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_main2.view.*
import uz.myapps.alishernavoiy.R
import uz.myapps.alishernavoiy.adapter.KindAdapter
import uz.myapps.alishernavoiy.viewmodel.JavaViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Main2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Main2Fragment : Fragment() {
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
        val root = inflater.inflate(R.layout.fragment_main2, container, false)
        val javaViewModel = ViewModelProviders.of(this)[JavaViewModel::class.java]
        javaViewModel.getKindListData().observe(requireActivity(), Observer {
            root.bar_main.visibility=View.GONE
            root.rv1.visibility=View.VISIBLE
            val kindAdapter = KindAdapter(it,fragmentManager!!,loadDrawable())
            root.rv1.adapter=kindAdapter
        })

        return root
    }

    private fun loadDrawable():List<Int>{
        var list = ArrayList<Int>()
        list.add(R.drawable.ic_pg1)
        list.add(R.drawable.ic_pg2)
        list.add(R.drawable.ic_pg3)
        list.add(R.drawable.ic_pg4)
        return list
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Main2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}