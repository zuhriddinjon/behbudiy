package uz.myapps.alishernavoiy.fragments

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.fragment_text.view.*
import uz.myapps.alishernavoiy.R
import uz.myapps.alishernavoiy.models.Plan
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var pdfViewer: PDFView

/**
 * A simple [Fragment] subclass.
 * Use the [TextFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TextFragment : Fragment(), MediaPlayer.OnPreparedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var isPLay: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_text, container, false)
        mMediaplayer = MediaPlayer()
        mMediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        pdfViewer = root.findViewById(R.id.pdfView)
        bar = root.findViewById(R.id.bar_text)
        rl = root.findViewById(R.id.rl_text)
        bar.max = mMediaplayer.duration / 1000
        val plan = arguments?.getSerializable("text") as Plan
        root.name_text.text = plan.name

        if (plan.text != null) {
            root.pdfView.visibility = View.GONE
            var fullText = ""
            for (i in plan.text)
                fullText += i.replace("&&", "\n", false)
            root.text.text = fullText
            root.bar_text.visibility = View.GONE
            root.rl_text.visibility = View.VISIBLE
        } else
            if (plan.link != "") {
                root.text.visibility = View.GONE
                RetrivePdfStream().execute(plan.link)
            } else
                root.bar_text.visibility = View.GONE
        if (plan.mp3 == "")
            root.ll.visibility = View.INVISIBLE
        root.btn_play.setOnClickListener {
            if (!isPLay) {
                isPLay = true
                root.btn_play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                fetchAudioUrlFromFirebase(plan.mp3)

                val mHandler = Handler()
                requireActivity().runOnUiThread(object : Runnable {
                    override fun run() {
                        if (mMediaplayer != null) {
                            val mCurrentPosition: Int = mMediaplayer.currentPosition / 1000
                            root.bar.progress = mCurrentPosition
                            Log.d(
                                "TextFragment",
                                "onCreateView: ${mMediaplayer.currentPosition / 1000}"
                            )
                            if (mMediaplayer.duration - mMediaplayer.currentPosition < 500) {
                                mMediaplayer.stop()
                            }
                        }
                        mHandler.postDelayed(this, 1000)
                    }
                })

            } else {
                root.btn_play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                mMediaplayer.pause()
            }
        }

        root.bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 != mMediaplayer.currentPosition / 1000) {
                    mMediaplayer.seekTo(p1 * 1000)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        root.scroll.setOnTouchListener(object : OnTouchListener {
            var y0 = 0f
            var y1 = 0f
            override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
                if (!isPLay && plan.mp3 != "") {
                    if (motionEvent.getAction() === MotionEvent.ACTION_MOVE) {
                        y0 = motionEvent.y
                        if (y1 - y0 > 0) {
                            slideDown(root.ll)
                        } else if (y1 - y0 < 0) {
                            slideUp(root.ll)
                        }
                        y1 = motionEvent.y
                    }
                }
                return false
            }
        })
        return root
    }

    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f,
            0f,
            view.height.toFloat(),
            0f
        )
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    fun slideDown(view: View) {
        val animate = TranslateAnimation(
            0f,
            0f,  // toXDelta
            0f,  // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    private fun fetchAudioUrlFromFirebase(mp3: String) {
        try {
            mMediaplayer.setDataSource(mp3)
            mMediaplayer.setOnPreparedListener(this)
            mMediaplayer.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        lateinit var bar: ProgressBar
        lateinit var rl: RelativeLayout
        var mMediaplayer: MediaPlayer = MediaPlayer()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TextFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPrepared(p0: MediaPlayer?) {
        p0?.start();
    }
}

class RetrivePdfStream : AsyncTask<String, Void, InputStream>() {
    override fun doInBackground(vararg p0: String?): InputStream {
        var inputStream: InputStream? = null
        try {
            var url = URL(p0[0])
            var urlConnection = url.openConnection() as HttpURLConnection
            if (urlConnection.responseCode == 200) {
                inputStream = BufferedInputStream(urlConnection.inputStream)
            }
        } catch (e: IOException) {
            Log.d("TextFragment", "doInBackground: ${e.toString()}")
            return BufferedInputStream(null)
        }
        return inputStream!!
    }

    override fun onPostExecute(result: InputStream?) {
        pdfViewer.fromStream(result).load()
        TextFragment.bar.visibility = View.GONE
        TextFragment.rl.visibility = View.VISIBLE
    }

}
