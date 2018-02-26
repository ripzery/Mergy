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
    override fun handleShare(user: Response.User, photo: Response.Photo, imageURL: String) {
        val reqSendEmail = Request.SendEmail(
                user.email,
                user.userProfileId,
                imageURL,
                user.firstName,
                user.lastName,
                photo.seasonId,
                photo.imageId
        )
        mView.showLoading()
        mView.sendEmail(reqSendEmail) {
            mView.hideLoading()
            mView.showSuccessDialog()
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