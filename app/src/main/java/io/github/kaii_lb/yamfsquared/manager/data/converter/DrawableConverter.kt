package io.github.kaii_lb.yamfsquared.manager.data.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class DrawableConverter {

    @TypeConverter
    fun fromDrawable(drawable: Drawable?): String? {
        if (drawable == null) {
            return null
        }
        val bitmap = (drawable as BitmapDrawable).bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    @TypeConverter
    fun toDrawable(encodedString: String?): Drawable? {
        if (encodedString == null) {
            return null
        }
        val byteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapDrawable(null, BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))
    }
}