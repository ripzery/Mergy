package me.ripzery.warpcan.ui.share

import android.net.Uri
import me.ripzery.warpcan.network.Request
import me.ripzery.warpcan.network.Response


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface ShareContract {
    interface View {
        fun updateThenSendEmail(reqUpload: Request.Upload, reqSendEmail: Request.SendEmail, onSuccess: (Response.SendEmail) -> Unit)
        fun encryptBase64(callback: (String) -> Unit)
        fun showLoading()
        fun hideLoading()
        fun showSuccessDialog()
        fun changeBtnName(name: String)
        fun showUsers(users: ArrayList<Response.User>)
    }

    interface Presenter {
        fun handleShare(user: Response.User, photo: Response.Photo, img: Uri)
        fun fetchUsers()
    }
}