package me.ripzery.mergy.share

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import me.ripzery.mergy.R
import me.ripzery.mergy.extensions.logd
import me.ripzery.mergy.helpers.Base64Helper
import me.ripzery.mergy.network.DataProvider
import me.ripzery.mergy.network.Request
import me.ripzery.mergy.network.Response

class ShareActivity : AppCompatActivity() {

    lateinit var mCurrentPhoto: Response.Photo
    lateinit var mImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Share"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Load merged image into preview
        mImageUri = Uri.parse(intent.getStringExtra("result"))
        mCurrentPhoto = intent.getParcelableExtra("photo")
        Glide.with(this)
                .load(mImageUri)
                .into(ivShare)

        btnShare.setOnClickListener {
            Base64Helper.encrypt(this, mImageUri) {
                with(mCurrentPhoto) {
                    val requestSendEmail = Request.SendEmail(
                            "ripzery@gmail.com",
                            1,
                            "",
                            "Euro",
                            "Test",
                            mCurrentPhoto.seasonId,
                            mCurrentPhoto.imageId
                    )
                    val requestUpload = Request.Upload(it, 1)
                    DataProvider.uploadThenSendEmail(requestUpload, requestSendEmail) {
                        logd(it.toString())
                    }
                }
            }

        }
    }

    override fun onStop() {
        super.onStop()
        DataProvider.unsubscribe()
    }
}
