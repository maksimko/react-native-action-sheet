package com.mabo.actionsheet.utils

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import java.util.*

fun <T> ReadableMap?.get(path: String): T? {
    return this.get(path, null)
}

fun <T> ReadableMap?.get(path: String, defaultValue: T): T {
    if (this == null) { return defaultValue }

    var field = path
    var workingSet = this

    if (path.contains(".")) {
        val fields = ArrayList(Arrays.asList(*path.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))

        field = fields.removeAt(fields.size - 1)

        for (pathField in fields) {
            workingSet = workingSet?.get(pathField)
        }
    }

    val value = extract(workingSet, field, defaultValue)

    return (value as? T?) ?: defaultValue
}

private fun extract(data: ReadableMap?, field: String, defaultValue: Any?): Any? {
    if (data == null || !data.hasKey(field)) {
        return defaultValue
    }

    val value = data.getDynamic(field)

    when (value.type) {
        ReadableType.Array -> return value.asArray().toArrayList()
        ReadableType.Map -> return value.asMap()
        ReadableType.Number -> return value.asInt()
        ReadableType.String -> return value.asString()
        ReadableType.Boolean -> return value.asBoolean()
        else -> return defaultValue
    }
}
