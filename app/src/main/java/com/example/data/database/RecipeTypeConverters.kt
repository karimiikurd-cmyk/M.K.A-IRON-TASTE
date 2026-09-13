package com.example.data.database

import androidx.room.TypeConverter
import com.example.data.model.Ingredient
import org.json.JSONArray
import org.json.JSONObject

class RecipeTypeConverters {

    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>?): String {
        if (ingredients.isNullOrEmpty()) return "[]"
        val array = JSONArray()
        for (ing in ingredients) {
            val obj = JSONObject()
            obj.put("name", ing.name)
            obj.put("amount", ing.amount)
            obj.put("unit", ing.unit)
            obj.put("notes", ing.notes)
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toIngredientsList(jsonString: String?): List<Ingredient> {
        if (jsonString.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<Ingredient>()
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Ingredient(
                        name = obj.optString("name", ""),
                        amount = obj.optDouble("amount", 0.0),
                        unit = obj.optString("unit", "گرم"),
                        notes = obj.optString("notes", "")
                    )
                )
            }
        } catch (_: Exception) {
        }
        return list
    }

    @TypeConverter
    fun fromStringList(steps: List<String>?): String {
        if (steps.isNullOrEmpty()) return "[]"
        val array = JSONArray()
        for (step in steps) {
            array.put(step)
        }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(jsonString: String?): List<String> {
        if (jsonString.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (_: Exception) {
        }
        return list
    }
}
