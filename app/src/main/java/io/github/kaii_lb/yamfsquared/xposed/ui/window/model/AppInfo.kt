package io.github.kaii_lb.yamfsquared.xposed.ui.window.model

import android.content.ComponentName
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class AppInfo(
    val icon: @RawValue Drawable,
    val label: CharSequence,
    val componentName: ComponentName
) : Parcelable
