package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Ingredient(
    val name: String,
    val amount: Double,
    val unit: String,
    val notes: String = ""
)

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val englishName: String,
    val category: String,
    val categoryFa: String,
    val shortDescription: String,
    val baseProtein: String,
    val baseProteinFa: String,
    val recommendedCut: String,
    val flavorProfile: String,
    val flavorProfileFa: String,
    val baseQuantityKg: Double = 1.0,
    val ingredientsJson: String,
    val prepStepsJson: String,
    val marinationTime: String,
    val cookingMethod: String,
    val cookingTemp: String,
    val spiceLevel: String,
    val recommendedUse: String,
    val storageNotes: String,
    val proTips: String,
    val commonMistakes: String,
    val substitutions: String,
    val isTraditional: Boolean = false,
    val isFusion: Boolean = false,
    val country: String = "ایران",
    val region: String = "عمومی",
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val userNotes: String = ""
)
