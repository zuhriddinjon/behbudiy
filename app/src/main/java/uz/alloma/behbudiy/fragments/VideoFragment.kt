package uz.alloma.behbudiy.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_videos.*
import uz.alloma.behbudiy.R

class VideoFragment : Fragment(R.layout.fragment_videos) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(youtube_player_view)
    }
}