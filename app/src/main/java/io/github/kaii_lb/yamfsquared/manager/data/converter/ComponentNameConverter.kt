package io.github.kaii_lb.yamfsquared.manager.data.converter

import android.content.ComponentName
import androidx.room.TypeConverter

class ComponentNameConverter {

    @TypeConverter
    fun fromComponentName(componentName: ComponentName?): String? {
        return componentName?.flattenToString()
    }

    @TypeConverter
    fun toComponentName(flattenedString: String?): ComponentName? {
        return flattenedString?.let { ComponentName.unflattenFromString(it) }
    }
}