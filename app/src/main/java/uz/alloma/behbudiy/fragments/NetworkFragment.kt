package uz.alloma.behbudiy.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_network.*
import uz.alloma.behbudiy.R

class NetworkFragment : Fragment(R.layout.fragment_network) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView.settings.javaScriptEnabled = true

        webView.loadUrl("https://behbudiy.uz/index.php/en/")
    }
}