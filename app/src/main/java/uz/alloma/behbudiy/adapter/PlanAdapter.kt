package uz.alloma.behbudiy.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_rv2.view.*
import uz.alloma.behbudiy.R
import uz.alloma.behbudiy.fragments.InfoFragment
import uz.alloma.behbudiy.fragments.TextFragment
import uz.alloma.behbudiy.models.Plan

class PlanAdapter(
    var list: List<Plan>,
    var loadColors: List<Int>,
    var context: Context,
    var fragmentManager: FragmentManager
) : RecyclerView.Adapter<PlanAdapter.VH>() {
    var listFiltered: List<Plan> = list

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(plan: Plan, random: Int) {
            itemView.name_plan.text = plan.name
            var fullText: String = ""
            plan.text.forEach { i ->
                fullText += i.replace("&&", "\n", false)
            }
            itemView.text.text = fullText
            Log.d("color", "color: " + loadColors[random].toString() + "   " + random.toString())
            itemView.name_plan.setTextColor(context.resources.getColor(loadColors[random]))
            itemView.bg.setBackgroundColor(context.resources.getColor(loadColors[random]))
            itemView.setOnClickListener {
                val textFragment = TextFragment()
                val bundleOf = bundleOf("text" to plan)
                textFragment.arguments = bundleOf
                fragmentManager.beginTransaction().addToBackStack("text")
                    .replace(R.id.container, textFragment).commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_rv2, parent, false)
        return VH(inflate)
    }

    override fun getItemCount(): Int = listFiltered.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val random = position % 7
        holder.onBind(listFiltered[position], random)
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (p0 == null || p0.isEmpty()) {
                    filterResults.count = list.size
                    filterResults.values = list
                } else {
                    val resultsModel: ArrayList<Plan> = ArrayList()
                    val searchStr = p0.toString().toLowerCase()

                    list.forEach {
                        it.text.forEach { text ->
                            if (text.toLowerCase().contains(searchStr)) {
                                resultsModel.add(it)
                            }
                        }
                        filterResults.count = resultsModel.size
                        filterResults.values = resultsModel
                    }
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

                listFiltered = p1?.values as List<Plan>
                InfoFragment.list = p1.values as List<Plan>
                notifyDataSetChanged()

            }

        }
    }
}