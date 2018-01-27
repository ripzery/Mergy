package me.ripzery.mergy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

@Parcelize
data class BackgroundData(val bitmap: Int, val caption: String) : Parcelable