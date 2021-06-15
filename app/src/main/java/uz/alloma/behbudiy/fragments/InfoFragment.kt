package uz.alloma.behbudiy.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.launch
import uz.alloma.behbudiy.R
import uz.alloma.behbudiy.adapter.PlanAdapter
import uz.alloma.behbudiy.models.Kind
import uz.alloma.behbudiy.models.Plan
import uz.alloma.behbudiy.viewmodel.BehbudiyViewModel

class InfoFragment : Fragment(R.layout.fragment_info), View.OnClickListener {
    private var planAdapter: PlanAdapter? = null
    private var searchText = ""
    private val viewModel: BehbudiyViewModel by viewModels()
    private var kind: Kind? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kind = requireArguments().getParcelable("kind")
        if (kind != null) {
            viewModel.getPlanListData(kind!!.id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pb_info?.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                viewModel.livePlan.observe(viewLifecycleOwner) {
                    pb_info?.visibility = View.GONE
                    name_kind?.text = kind!!.name
                    planAdapter = if (searchText != "" && planAdapter != null) {
                        PlanAdapter(list, loadColors(), requireContext(), parentFragmentManager)
                    } else
                        PlanAdapter(it, loadColors(), requireContext(), parentFragmentManager)
                    rv2.adapter = planAdapter
                }
            } catch (e: Exception) {
                Log.d("TAG_Error", "fetchCharacters: ${e.message}")
            }
        }
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchText = p0.toString()
                planAdapter?.getFilter()?.filter(p0)
                planAdapter?.notifyDataSetChanged()

            }

        })

        btn_message.setOnClickListener(this)
    }

    private fun loadColors(): List<Int> {
        val listColor = ArrayList<Int>()
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
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_message -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ImageFragment())
                    .addToBackStack("info")
                    .commit()

            }
        }
    }
}