package uz.alloma.behbudiy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.alloma.behbudiy.fragments.SplashFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SplashFragment())
                .commit()
    }

}