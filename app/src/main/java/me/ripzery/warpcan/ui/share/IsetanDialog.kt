package me.ripzery.warpcan.ui.share

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.layout_isetan_dialog.*
import me.ripzery.warpcan.R


/**
 * Ripzery
 *
 * Created by Phuchit Sirimongkolsathien on 25/2/2018 AD.
 * Copyright © 2018 Ripzery. All rights reserved.
 */

class IsetanDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_isetan_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        btnConfirm.setOnClickListener {
            dismiss()
        }
    }
}