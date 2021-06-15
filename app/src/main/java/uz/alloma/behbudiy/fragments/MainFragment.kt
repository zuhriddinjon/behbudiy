package uz.alloma.behbudiy.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import uz.alloma.behbudiy.R

class MainFragment : Fragment(R.layout.fragment_main), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        tv_networks.setOnClickListener(this)
        tv_books.setOnClickListener(this)
        tv_video.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_networks -> {
                replaceFragment(NetworkFragment())
            }
            R.id.tv_books -> {
                replaceFragment(InformationFragment())
            }
            R.id.tv_video -> {
                replaceFragment(VideoFragment())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack("main")
            .commit()
    }
}