package me.ripzery.warpcan

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_agreement.*
import me.ripzery.warpcan.extensions.toast
import me.ripzery.warpcan.ui.main.MainActivity

class AgreementActivity : AppCompatActivity() {

    companion object {
        const val LANG_TH = "th"
        const val LANG_EN = "en"
    }

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

        btnChangeLanguage.setOnClickListener {
            changeLanguage(btnChangeLanguage.text.toString().toLowerCase())
        }
    }

    private fun changeLanguage(lang: String) {
        when (lang) {
            LANG_TH -> {
                btnChangeLanguage.text = LANG_EN.toUpperCase()
                tvAgreement.text = getString(R.string.agreement_th)
                tvAgreement.scrollTo(0, 0)
            }
            LANG_EN -> {
                btnChangeLanguage.text = LANG_TH.toUpperCase()
                tvAgreement.text = getString(R.string.agreement_en)
                tvAgreement.scrollTo(0, 0)
            }
        }
    }
}
