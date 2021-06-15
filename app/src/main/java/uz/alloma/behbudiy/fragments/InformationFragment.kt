package uz.alloma.behbudiy.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_information.*
import kotlinx.coroutines.launch
import uz.alloma.behbudiy.R
import uz.alloma.behbudiy.adapter.KindAdapter
import uz.alloma.behbudiy.viewmodel.BehbudiyViewModel

class InformationFragment : Fragment(R.layout.fragment_information) {
    private val viewModel: BehbudiyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getKindListData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchKinds()
    }

    private fun fetchKinds() {
        lifecycleScope.launch {
            try {
                viewModel.liveKind.observe(viewLifecycleOwner) {
                    pb_infor.visibility = View.GONE
                    rv1.layoutManager = GridLayoutManager(requireContext(), 2)
                    val kindAdapter = KindAdapter(it, parentFragmentManager, loadDrawable())
                    rv1.adapter = kindAdapter
                }
            } catch (e: Exception) {
                Log.d("TAG_Error", "fetchCharacters: ${e.message}")
            }
        }

    }

    private fun loadDrawable(): List<Int> {
        val list = ArrayList<Int>()
        list.add(R.drawable.ic_pg1)
        list.add(R.drawable.ic_pg2)
        list.add(R.drawable.ic_pg3)
        list.add(R.drawable.ic_pg4)
        return list
    }
}