package me.ripzery.mergy.share

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import me.ripzery.mergy.R
import me.ripzery.mergy.extensions.logd
import me.ripzery.mergy.helpers.Base64Helper
import me.ripzery.mergy.network.DataProvider
import me.ripzery.mergy.network.Request
import me.ripzery.mergy.network.Response
import java.text.SimpleDateFormat
import java.util.*

class ShareActivity : AppCompatActivity() {

    lateinit var mCurrentPhoto: Response.Photo
    lateinit var mImageUri: Uri
    lateinit var mUsers: ArrayList<Response.User>
    var mSelectedUser: Response.User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        mImageUri = Uri.parse(intent.getStringExtra("result"))
        mCurrentPhoto = intent.getParcelableExtra("photo")
        initInstance()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Share"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fetchUsers()
        previewImage()
        setupShareBtn()
    }

    private fun previewImage() {
        Glide.with(this)
                .load(mImageUri)
                .into(ivShare)
    }

    private fun fetchUsers() {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//        val request = Request.RetrieveUsers(dateFormat.format(date))
        val request = Request.RetrieveUsers("2018-01-29")
        DataProvider.retrieveUsers(request) {
            mUsers = it.message
            spinUsers.setItems(it.message.map { it.email })
            mSelectedUser = mUsers[0]
        }

        spinUsers.setOnItemSelectedListener { view, position, id, item ->
            mSelectedUser = mUsers[position]
        }

    }

    private fun setupShareBtn() {
        btnShare.setOnClickListener {
            Base64Helper.encrypt(this, mImageUri) {
                with(mCurrentPhoto) {
                    val requestSendEmail = Request.SendEmail(
                            "ripzery@gmail.com",
                            mSelectedUser!!.userProfileId,
                            "",
                            mSelectedUser!!.firstName,
                            mSelectedUser!!.lastName,
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
