package com.wunder.challenge.model

import android.arch.persistence.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import java.util.*


class CoordinatesConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToDoubleList(data: String?): List<Double> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Double>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun doubleListToString(someObjects: List<Double>): String {
        return gson.toJson(someObjects)
    }
}