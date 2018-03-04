package me.ripzery.warpcan.ui.custom

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.layout_isetan_dialog.*
import kotlinx.android.synthetic.main.layout_isetan_retry_dialog.*
import me.ripzery.warpcan.R
import me.ripzery.warpcan.network.Request


/**
 * Ripzery
 *
 * Created by Phuchit Sirimongkolsathien on 25/2/2018 AD.
 * Copyright © 2018 Ripzery. All rights reserved.
 */

class IsetanDialog : DialogFragment() {

    private var mode: Int = MODE_SEND_EMAIL
    private val mRetryViewModel: RetryViewModel by lazy { ViewModelProviders.of(activity!!).get(RetryViewModel::class.java) }

    companion object {
        const val MODE_SEND_EMAIL = 1
        const val MODE_RETRY = 2

        const val PARAM_MODE = "mode"
        const val PARAM_REQUEST = "request"

        fun newInstance(mode: Int, request: Request.Retriable): IsetanDialog {
            val bundle = Bundle()
            bundle.putInt(PARAM_MODE, mode)
            bundle.putParcelable(PARAM_REQUEST, request)
            return IsetanDialog().also {
                it.arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mode = arguments?.getInt(PARAM_MODE) ?: 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = when (mode) {
            MODE_SEND_EMAIL -> R.layout.layout_isetan_dialog
            MODE_RETRY -> R.layout.layout_isetan_retry_dialog
            else -> R.layout.layout_isetan_dialog
        }
        return inflater.inflate(layout, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        val request = arguments?.getParcelable<Request.Retriable>(PARAM_REQUEST)
        when (request) {
            is Request.Retriable.Upload -> {
                tvTitle.text = getString(R.string.isetan_retry_dialog_upload_text_title)
            }
            is Request.Retriable.SendEmail -> {
                tvTitle.text = getString(R.string.isetan_retry_dialog_sendmail_text_title)
            }
        }

        when (mode) {
            MODE_RETRY -> {
                btnRetry.setOnClickListener {
                    if (arguments == null) return@setOnClickListener
                    mRetryViewModel.dispatchRequesting()
                    dismiss()
                }
            }
            MODE_SEND_EMAIL -> {
                btnConfirm.setOnClickListener {
                    dismiss()
                }
            }
        }
    }
}