package uz.myapps.alishernavoiy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.myapps.alishernavoiy.fragments.SplashFragment
import uz.myapps.alishernavoiy.fragments.TextFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.container,SplashFragment()).commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(TextFragment.mMediaplayer!=null)
        if(TextFragment.mMediaplayer.isPlaying)
            TextFragment.mMediaplayer.stop()
    }

}