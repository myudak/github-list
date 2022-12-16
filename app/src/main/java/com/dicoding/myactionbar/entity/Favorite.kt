package com.dicoding.myactionbar.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite (
    var id: Int = 0,
    var name: String? = null,
    var image: String? = null
    ) : Parcelable