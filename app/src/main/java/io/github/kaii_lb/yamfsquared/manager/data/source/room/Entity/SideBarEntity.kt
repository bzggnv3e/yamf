package io.github.kaii_lb.yamfsquared.manager.data.source.room.Entity

import android.content.ComponentName
import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.RawValue

@Entity(tableName = "sideBarApp")
data class SideBarEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "icon")
    val icon: @RawValue Drawable,

    @ColumnInfo(name = "label")
    val label: String,

    @ColumnInfo(name = "componentName")
    val componentName: ComponentName,

    @ColumnInfo(name = "userId")
    val userId: Int
)
