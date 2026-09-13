package com.example.data.database

import android.content.Context
import android.util.Log
import com.example.data.model.RecipeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object DatabaseInitializer {
    private const val TAG = "DatabaseInitializer"

    suspend fun populateIfNeeded(context: Context, dao: RecipeDao) = withContext(Dispatchers.IO) {
        try {
            val count = dao.getRecipeCount()
            val seafoodCount = dao.getCountByCategory("مرینیت‌های ماهی و غذاهای دریایی")
            val sausageCount = dao.getCountByCategory("سوسیس و کالباس دست‌ساز")
            val saladCount = dao.getCountByCategory("سالاد و پیش‌غذا")
            val dessertCount = dao.getCountByCategory("دسر سرد")

            if (count >= 1120 && saladCount >= 30 && dessertCount >= 20) {
                Log.d(TAG, "Database fully up-to-date with $count recipes across all categories.")
                return@withContext
            }

            Log.d(TAG, "Refreshing database to 1120 recipes. Current: $count (Salad: $saladCount, Dessert: $dessertCount)...")
            val favoriteIds = try { dao.getFavoriteIds().toSet() } catch (e: Exception) { emptySet() }

            // Clear old non-custom recipes to prevent duplicates
            dao.deleteNonCustomRecipes()

            val jsonString = context.assets.open("recipes.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val recipes = ArrayList<RecipeEntity>(jsonArray.length())

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optString("id", "rec_${i + 1}")
                val ingredientsArray = obj.optJSONArray("ingredients") ?: JSONArray()
                val stepsArray = obj.optJSONArray("prepSteps") ?: JSONArray()

                recipes.add(
                    RecipeEntity(
                        id = id,
                        name = obj.optString("name", "دستور بدون نام"),
                        englishName = obj.optString("englishName", ""),
                        category = obj.optString("category", "General"),
                        categoryFa = obj.optString("categoryFa", "عمومی"),
                        shortDescription = obj.optString("shortDescription", ""),
                        baseProtein = obj.optString("baseProtein", "Mixed"),
                        baseProteinFa = obj.optString("baseProteinFa", "مخلوط"),
                        recommendedCut = obj.optString("recommendedCut", "برش استاندارد"),
                        flavorProfile = obj.optString("flavorProfile", "Mild"),
                        flavorProfileFa = obj.optString("flavorProfileFa", "ملایم"),
                        baseQuantityKg = obj.optDouble("baseQuantityKg", 1.0),
                        ingredientsJson = ingredientsArray.toString(),
                        prepStepsJson = stepsArray.toString(),
                        marinationTime = obj.optString("marinationTime", "۴ تا ۸ ساعت"),
                        cookingMethod = obj.optString("cookingMethod", "منقل زغالی یا گریل"),
                        cookingTemp = obj.optString("cookingTemp", "حرارت متوسط"),
                        spiceLevel = obj.optString("spiceLevel", "ملایم"),
                        recommendedUse = obj.optString("recommendedUse", "عرضه در ویترین پروتئینی"),
                        storageNotes = obj.optString("storageNotes", "نگهداری در دمای ۱ تا ۴ درجه سانتی‌گراد"),
                        proTips = obj.optString("proTips", ""),
                        commonMistakes = obj.optString("commonMistakes", ""),
                        substitutions = obj.optString("substitutions", ""),
                        isTraditional = obj.optBoolean("isTraditional", false),
                        isFusion = obj.optBoolean("isFusion", false),
                        country = obj.optString("country", "ایران"),
                        region = obj.optString("region", "عمومی"),
                        isFavorite = favoriteIds.contains(id),
                        isCustom = false,
                        userNotes = ""
                    )
                )
            }

            // Batch insert in chunks of 200 to prevent SQLite statement limits
            recipes.chunked(200).forEach { chunk ->
                dao.insertAll(chunk)
            }
            Log.d(TAG, "Successfully populated ${recipes.size} recipes into Room DB.")
        } catch (e: Exception) {
            Log.e(TAG, "Error populating database", e)
        }
    }
}
