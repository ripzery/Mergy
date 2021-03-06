package me.ripzery.mergy.ui.share

import android.net.Uri
import me.ripzery.mergy.network.DataProvider
import me.ripzery.mergy.network.Request
import me.ripzery.mergy.network.Response
import java.text.SimpleDateFormat
import java.util.*


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class SharePresenter(private val mView: ShareContract.View) : ShareContract.Presenter {
    override fun handleShare(user: Response.User, photo: Response.Photo, img: Uri) {
        val reqSendEmail = Request.SendEmail(
                user.email,
                user.userProfileId,
                "",
                user.firstName,
                user.lastName,
                photo.seasonId,
                photo.imageId
        )
        mView.encryptBase64 {
            val reqUpload = Request.Upload(it, 1)
            mView.showLoading()
            mView.updateThenSendEmail(reqUpload, reqSendEmail) {
                mView.hideLoading()
            }
        }
    }

    override fun fetchUsers() {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val request = Request.RetrieveUsers("2018-01-29")

        DataProvider.retrieveUsers(request) {
            mView.showUsers(it.message)
        }
    }
}