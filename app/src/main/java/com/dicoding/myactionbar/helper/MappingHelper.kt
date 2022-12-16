package com.dicoding.myactionbar.helper

import android.database.Cursor
import com.dicoding.myactionbar.db.DatabaseContract
import com.dicoding.myactionbar.entity.Favorite

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Favorite> {
        val notesList = ArrayList<Favorite>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val image = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.IMAGE))
                notesList.add(Favorite(id, name, image))
            }
        }
        return notesList
    }
}