package me.ripzery.mergy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_agreement.*
import me.ripzery.mergy.extensions.toast
import me.ripzery.mergy.ui.main.MainActivity

class AgreementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        tvAgreement.movementMethod = ScrollingMovementMethod()

        btnAgree.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        btnCancel.setOnClickListener {
            toast("Please press \"Agree\" to use the app")
            finish()
        }
    }
}
