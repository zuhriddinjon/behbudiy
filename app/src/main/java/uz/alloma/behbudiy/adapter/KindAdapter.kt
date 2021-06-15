package uz.alloma.behbudiy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_rv1.view.*
import uz.alloma.behbudiy.R
import uz.alloma.behbudiy.fragments.InfoFragment
import uz.alloma.behbudiy.models.Kind

class KindAdapter(
    var list: List<Kind>,
    var fragmentManager: FragmentManager,
    var loadDrawable: List<Int>
) :
    RecyclerView.Adapter<KindAdapter.VH>() {
    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(kind: Kind, i: Int) {
            itemView.name.text = kind.name
            itemView.img_bg.setImageResource(i)
            itemView.setOnClickListener {
                val mainFragment = InfoFragment()
                val bundleOf = bundleOf("kind" to kind)
                mainFragment.arguments = bundleOf
                fragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .addToBackStack("kind")
                    .commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_rv1, parent, false)
        return VH(inflate)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val i = position % 4
        holder.onBind(list[position], loadDrawable[i])
    }
}