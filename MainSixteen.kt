package com.example.memorygame

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import java.util.*

class MainSixteen : AppCompatActivity() {

    lateinit var ad: AdView
    private lateinit var interstitialAd: InterstitialAd

    val rnd = Random()
    var btnarr = mutableListOf<ImageButton>()
    var opencard1: ImageButton? = null
    var opencard2: ImageButton? = null
    var count = 0
    var turn = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sixteen)
        arrangeGame()
        ad=findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        ad.loadAd(adRequest)

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        interstitialAd.loadAd(AdRequest.Builder().build())
        interstitialAd.adListener= object : AdListener(){
            override fun onAdClosed() {
                val adRequest = AdRequest.Builder().build()
                interstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

    }

    fun arrangeGame() {
        //function creates an array of image buttons

        btnarr.add(findViewById(R.id.imageButton1))
        btnarr.add(findViewById(R.id.imageButton2))
        btnarr.add(findViewById(R.id.imageButton3))
        btnarr.add(findViewById(R.id.imageButton4))
        btnarr.add(findViewById(R.id.imageButton5))
        btnarr.add(findViewById(R.id.imageButton6))
        btnarr.add(findViewById(R.id.imageButton7))
        btnarr.add(findViewById(R.id.imageButton8))
        btnarr.add(findViewById(R.id.imageButton9))
        btnarr.add(findViewById(R.id.imageButton10))
        btnarr.add(findViewById(R.id.imageButton11))
        btnarr.add(findViewById(R.id.imageButton12))
        btnarr.add(findViewById(R.id.imageButton13))
        btnarr.add(findViewById(R.id.imageButton14))
        btnarr.add(findViewById(R.id.imageButton15))
        btnarr.add(findViewById(R.id.imageButton16))

        for (btn in btnarr) {
            btn?.setImageResource(R.drawable.back)
            btn?.setTag("0")
            btn?.isClickable = true
            btn?.isEnabled = true
        }
        count = 0

        randomize(btnarr)
    }

    fun randomize(arr: List<ImageButton>) {
        //function takes an array of image buttons and joins tag (represent an image) to random pairs of imagebuttons
        var c = 8
        while (c != 0) {
            var n = rnd.nextInt(16)
            var m = rnd.nextInt(16)
            if (n != m && arr[n]?.getTag() == "0" && arr[m]?.getTag() == "0") {
                arr[n]?.setTag(c.toString())
                arr[m]?.setTag(c.toString())
                c--
            }
        }
    }

    fun Picked(view: View) {

        view.isEnabled=false
        view.isClickable=false
        if(turn==1) {
            opencard1=findViewById(view.id)
            setImage(opencard1)
            for(b in btnarr){
                if(b.id!=opencard1?.id)
                {
                    b.isEnabled=true
                    b.isClickable=true
                }
            }
            turn++
        }
        else {
            opencard2=findViewById(view.id)
            setImage(opencard2)
            setClickable(false)
            if(opencard1?.getTag()==opencard2?.getTag()) {
                toastMe("Good Job")
                count++
                btnarr.remove(opencard1)
                btnarr.remove(opencard2!!)
                opencard1=null
                opencard2=null
                if(count==8) finishGame()
                setClickable(true)
            }
            else {
                var time = timer(1000, 1000).start()
            }
            turn=1
        }

    }

    fun timer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {

                setImage(opencard1)
                setClickable(false)

            }

            override fun onFinish() {
                opencard1?.setImageResource(R.drawable.back)
                opencard2?.setImageResource(R.drawable.back)
                toastMe("Try Again")
                opencard1 = null
                opencard2 = null
                setClickable(true)
            }
        }

    }

    fun setClickable(bool: Boolean){
        for (b in btnarr){
            b.isClickable=bool
            b.isEnabled=bool
        }
    }

    fun setImage(btn: ImageButton?) {
        when (btn?.getTag().toString()) { //checking the tag and matching the image
            "1" -> btn?.setImageResource(R.drawable.lion)
            "2" -> btn?.setImageResource(R.drawable.pig)
            "3" -> btn?.setImageResource(R.drawable.tiger)
            "4" -> btn?.setImageResource(R.drawable.zebra)
            "5" -> btn?.setImageResource(R.drawable.frog)
            "6" -> btn?.setImageResource(R.drawable.panda)
            "7" -> btn?.setImageResource(R.drawable.monkey)
            "8" -> btn?.setImageResource(R.drawable.giraffe)
        }
    }



    fun toastMe(msg: String = "What?") {
        val myToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        myToast.show()
    }


    fun finishGame() {
        //function creates menu for user and execute user decision (new game or return to main menu)

        if(interstitialAd.isLoaded){
            interstitialAd.show()
        }
        var bld = AlertDialog.Builder(this)
        bld.setTitle("Good Job")
        bld.setMessage("Do you want to play again?")
        bld.setPositiveButton("Play Again", DialogInterface.OnClickListener { dialog, id ->
            arrangeGame()
        })
        bld.setNegativeButton("Quit", DialogInterface.OnClickListener { dialog, which ->
            var it = Intent(this, MainActivity::class.java)
            startActivity(it)

        })
        var alert = bld.create()
        alert.show()
    }
}
