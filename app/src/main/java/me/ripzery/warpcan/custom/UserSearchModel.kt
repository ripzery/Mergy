package me.ripzery.warpcan.custom

import ir.mirrajabi.searchdialog.core.Searchable


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 10/3/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class UserSearchModel(val email: String) : Searchable {
    override fun getTitle(): String {
        return email
    }
}