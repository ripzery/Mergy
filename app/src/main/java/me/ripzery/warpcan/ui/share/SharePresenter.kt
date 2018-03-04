package me.ripzery.warpcan.ui.share

import me.ripzery.warpcan.network.DataProvider
import me.ripzery.warpcan.network.Request
import me.ripzery.warpcan.network.Response
import java.text.SimpleDateFormat
import java.util.*


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class SharePresenter(private val mView: ShareContract.View) : ShareContract.Presenter {
    override fun handleShare(user: Response.User, photo: Response.Photo, uploadResponse: Response.Upload) {
        val reqSendEmail = Request.SendEmail(
                user.email,
                user.userProfileId,
                uploadResponse.message.imageUrl,
                if (user.firstName == "") "-" else user.firstName,
                if (user.lastName == "") "-" else user.lastName,
                photo.seasonId,
                uploadResponse.message.log.photoId
        )
        mView.showLoading()
        DataProvider.sendEmail(reqSendEmail, {
            mView.hideLoading()
            mView.showShareFail(it)
        }) {
            mView.hideLoading()
            mView.showShareSuccess(reqSendEmail.email)
            mView.showSuccessDialog()
        }
    }

    override fun fetchUsers() {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val request = Request.RetrieveUsers(dateFormat.format(date))

        DataProvider.retrieveUsers(request) {
            mView.showUsers(it.message)
        }
    }
}