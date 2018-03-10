package me.ripzery.warpcan.ui.share

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import me.ripzery.warpcan.R
import me.ripzery.warpcan.custom.UserSearchModel
import me.ripzery.warpcan.extensions.toast
import me.ripzery.warpcan.helpers.Base64Helper
import me.ripzery.warpcan.network.DataProvider
import me.ripzery.warpcan.network.Request
import me.ripzery.warpcan.network.Response
import me.ripzery.warpcan.ui.custom.IsetanDialog
import me.ripzery.warpcan.ui.custom.RetryViewModel
import me.ripzery.warpcan.ui.main.MainActivity


class ShareActivity : AppCompatActivity(), ShareContract.View {
    private lateinit var mCurrentPhoto: Response.Photo
    private lateinit var mImageUri: Uri
    private lateinit var mUploadResponse: Response.Upload
    private val mRetryViewModel: RetryViewModel by lazy { ViewModelProviders.of(this).get(RetryViewModel::class.java) }
    private val mSharePresenter: ShareContract.Presenter by lazy { SharePresenter(this) }
    private var mSelectedUser: Response.User? = null
    private lateinit var mUsersDialog: SimpleSearchDialogCompat<UserSearchModel>
    private val mSuccessDialog by lazy { IsetanDialog() }

    private var mUsers: ArrayList<Response.User> = arrayListOf()
        set(value) {
            when (value.size) {
                0 -> {
                    btnEmail.isEnabled = false
                    btnEmail.text = "Empty Users"
                }
                else -> {
                    btnEmail.isEnabled = true
                    field = value
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        mImageUri = Uri.parse(intent.getStringExtra("result"))
        mCurrentPhoto = intent.getParcelableExtra("photo")
        mUploadResponse = intent.getParcelableExtra("uploadResponse")
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

        btnEmail.setOnClickListener {
            if (mUsers.isNotEmpty()) {
                mUsersDialog.show()
            } else {
                toast("Please wait the users list loading...")
            }
        }

        btnBack.setOnClickListener { finish() }
        btnTryAgain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        btnShare.setOnClickListener {
            if (mSelectedUser != null) {
                mSharePresenter.handleShare(mSelectedUser!!, mCurrentPhoto, mUploadResponse)
            } else {
                toast("Please select the user first")
            }
        }

        mRetryViewModel.subscribeRequesting().observe(this, android.arch.lifecycle.Observer {
            if (mSelectedUser != null) {
                mSharePresenter.handleShare(mSelectedUser!!, mCurrentPhoto, mUploadResponse)
            } else {
                toast("Please select the user first")
            }
        })

        previewImage()
    }

    private fun previewImage() {
        Glide.with(this)
                .load(mImageUri)
                .into(ivShare)
    }

    override fun showUsers(users: ArrayList<Response.User>) {
        mUsers = users
        val items = ArrayList(mUsers.map { UserSearchModel(it.email) })
        mUsersDialog = SimpleSearchDialogCompat<UserSearchModel>(this,
                "Search...",
                "What is your email?",
                null,
                items,
                SearchResultListener<UserSearchModel> { dialog, item, position ->
                    btnEmail.text = "Send to ${item.email}"
                    mSelectedUser = mUsers.findLast { it.email == item.email }
                    dialog.dismiss()
                }
        )
    }

    override fun showShareSuccess(email: String) {
        toast("The image is sent to $email successfully.")
    }

    override fun showShareFail(error: Throwable, request: Request.Retriable.SendEmail) {
        val dialogRetry = IsetanDialog.newInstance(IsetanDialog.MODE_RETRY, request)
        dialogRetry.show(supportFragmentManager, "retry")
        toast("Cannot sent an email. $error")
    }

    override fun sendEmail(reqSendEmail: Request.Retriable.SendEmail, onSuccess: (Response.SendEmail) -> Unit) {
        DataProvider.sendEmail(reqSendEmail, {
            toast("Cannot sent an email. ${it.message}")
        }) {
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
        btnEmail.isEnabled = false
        ivShare.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        btnShare.alpha = 1.0f
        ivShare.alpha = 1.0f
        btnShare.isEnabled = true
        btnEmail.isEnabled = true
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
