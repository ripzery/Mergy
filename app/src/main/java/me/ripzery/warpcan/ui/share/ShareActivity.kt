package me.ripzery.warpcan.ui.share

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import me.ripzery.warpcan.R
import me.ripzery.warpcan.extensions.toast
import me.ripzery.warpcan.helpers.Base64Helper
import me.ripzery.warpcan.network.DataProvider
import me.ripzery.warpcan.network.Request
import me.ripzery.warpcan.network.Response
import java.util.*

class ShareActivity : AppCompatActivity(), ShareContract.View {
    private lateinit var mCurrentPhoto: Response.Photo
    private lateinit var mImageUri: Uri
    private lateinit var mUsers: ArrayList<Response.User>
    private val mSharePresenter: ShareContract.Presenter by lazy { SharePresenter(this) }
    private var mSelectedUser: Response.User? = null
    private val mSuccessDialog by lazy { IsetanDialog() }

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
        mSharePresenter.fetchUsers()

        spinUsers.setOnItemSelectedListener { view, position, id, item ->
            if (mUsers.size > 0) {
                mSelectedUser = mUsers[position]
                changeBtnName(mSelectedUser!!.email)
            }
        }

        previewImage()
        setupShareBtn()
    }

    private fun previewImage() {
        Glide.with(this)
                .load(mImageUri)
                .into(ivShare)
    }

    override fun showUsers(users: ArrayList<Response.User>) {
        mUsers = users
        spinUsers.setItems(mUsers.map { it.email })
        mSelectedUser = mUsers[0]
        changeBtnName(mSelectedUser!!.email)
    }

    private fun setupShareBtn() {
        btnShare.setOnClickListener {
            if (mSelectedUser != null) {
                mSharePresenter.handleShare(mSelectedUser!!, mCurrentPhoto, mImageUri)
            } else {
                toast("Please select the user first")
            }
        }
    }

    override fun updateThenSendEmail(reqUpload: Request.Upload, reqSendEmail: Request.SendEmail, onSuccess: (Response.SendEmail) -> Unit) {
        DataProvider.uploadThenSendEmail(reqUpload, reqSendEmail) {
            toast("The image is sent to ${reqSendEmail.email} successfully.")
            onSuccess(it)
        }
    }

    override fun encryptBase64(callback: (String) -> Unit) {
        Base64Helper.encrypt(this, mImageUri) {
            callback(it)
        }
    }

    override fun showLoading() {
        btnShare.alpha = 0.5f
        ivShare.alpha = 0.5f
        btnShare.isEnabled = false
        spinUsers.isEnabled = false
        ivShare.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        btnShare.alpha = 1.0f
        ivShare.alpha = 1.0f
        btnShare.isEnabled = true
        spinUsers.isEnabled = true
        ivShare.isEnabled = false
        progressBar.visibility = View.GONE
    }

    override fun showSuccessDialog() {
        mSuccessDialog.show(supportFragmentManager, "test")
    }

    @SuppressLint("SetTextI18n")
    override fun changeBtnName(name: String) {
        btnShare.text = "Send to $name"
    }

    override fun onStop() {
        super.onStop()
        DataProvider.unsubscribe()
    }
}
