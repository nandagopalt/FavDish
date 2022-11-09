package com.amalwin.favdishapplication.model.entities


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "fav_dish_table")
@Parcelize
data class FavDish(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "image_storage") val imagePath: String,
    @ColumnInfo(name = "image_source") val imageSource: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "ingredients") val ingredients: String,
    @ColumnInfo(name = "cooking_time") val cookingTime: String,
    @ColumnInfo(name = "instructions") val instructions: String,
    @ColumnInfo(name = "is_fav_dish") val isFavDish: Boolean
): Parcelable

