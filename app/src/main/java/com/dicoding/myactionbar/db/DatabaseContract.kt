package com.dicoding.myactionbar.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "note"
            const val _ID = "_id"
            const val NAME = "name"
            const val IMAGE = "image"
        }
    }
}