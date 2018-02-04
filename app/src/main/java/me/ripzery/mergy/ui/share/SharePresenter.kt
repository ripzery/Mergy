package me.ripzery.mergy.ui.share

import android.net.Uri
import me.ripzery.mergy.network.Request
import me.ripzery.mergy.network.Response


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class SharePresenter(private val mView: ShareContract.View) : ShareContract.Presenter {
    private val mockEmail = "ripzery@gmail.com"

    override fun handleShare(user: Response.User, photo: Response.Photo, img: Uri) {
        val reqSendEmail = Request.SendEmail(
                mockEmail,
                user.userProfileId,
                user.email,
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

}