package com.segunfrancis.spacex.ui.view_more

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ViewMoreItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val onclick:() -> Unit
)
