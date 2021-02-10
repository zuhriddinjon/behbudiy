package uz.myapps.alishernavoiy.fragments

import android.graphics.Color.red
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_main2.view.*
import uz.myapps.alishernavoiy.R
import uz.myapps.alishernavoiy.adapter.uz.myapps.alishernavoiy.adapter.PlanAdapter
import uz.myapps.alishernavoiy.models.Kind
import uz.myapps.alishernavoiy.models.Plan
import uz.myapps.alishernavoiy.viewmodel.JavaViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var planAdapter: PlanAdapter? = null
    var searchText=""
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
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val kind = arguments?.getSerializable("kind") as Kind
        val javaViewModel = ViewModelProviders.of(this)[JavaViewModel::class.java]
        javaViewModel.getPlanListData(kind.id).observe(requireActivity(), Observer { it ->
            root.name_kind.text = kind.name
            if (searchText != "" && planAdapter != null) {
                planAdapter = PlanAdapter(list, loadColors(), requireContext(), fragmentManager!!)
            }else
            planAdapter = PlanAdapter(it, loadColors(), requireContext(), fragmentManager!!)
            root.rv2.adapter = planAdapter
        })

        root.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchText=p0.toString()
                planAdapter?.getFilter()?.filter(p0)
                planAdapter?.notifyDataSetChanged()

            }

        })

        root.btn_message.setOnClickListener {
            fragmentManager?.beginTransaction()?.addToBackStack("message")?.replace(R.id.container,ImageFragment())?.commit()
        }
        return root
    }

    fun loadColors(): List<Int> {
        var listColor = ArrayList<Int>()
        listColor.add(R.color.red_orange_color_picker)
        listColor.add(R.color.blue_color_picker)
        listColor.add(R.color.green_color_picker)
        listColor.add(R.color.yellow_color_picker)
        listColor.add(R.color.sky_blue_color_picker)
        listColor.add(R.color.orange_color_picker)
        listColor.add(R.color.violet_color_picker)
        return listColor
    }

    companion object {
        var list: List<Plan> = ArrayList()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}