package com.example.data.repository

import android.content.Context
import com.example.data.database.AppDatabase
import com.example.data.database.DatabaseInitializer
import com.example.data.model.RecipeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val dao = db.recipeDao()

    private val _isPopulating = MutableStateFlow(false)
    val isPopulating: StateFlow<Boolean> = _isPopulating.asStateFlow()

    suspend fun initialize() {
        _isPopulating.value = true
        DatabaseInitializer.populateIfNeeded(context, dao)
        _isPopulating.value = false
    }

    suspend fun getRecipeCount(): Int = dao.getRecipeCount()

    fun getAllRecipes(): Flow<List<RecipeEntity>> = dao.getAllRecipes()

    fun getRecipeById(id: String): Flow<RecipeEntity?> = dao.getRecipeById(id)

    fun getFavoriteRecipes(): Flow<List<RecipeEntity>> = dao.getFavoriteRecipes()

    fun getCustomRecipes(): Flow<List<RecipeEntity>> = dao.getCustomRecipes()

    fun filterRecipes(
        query: String?,
        category: String?,
        protein: String?,
        flavor: String?
    ): Flow<List<RecipeEntity>> {
        val q = if (query.isNullOrBlank()) null else query.trim()
        val cat = if (category.isNullOrBlank() || category == "همه" || category == "All") null else category
        val prot = if (protein.isNullOrBlank() || protein == "همه" || protein == "All") null else protein
        val flav = if (flavor.isNullOrBlank() || flavor == "همه" || flavor == "All") null else flavor
        return dao.filterRecipes(q, cat, prot, flav)
    }

    suspend fun toggleFavorite(id: String, currentFavorite: Boolean) {
        dao.setFavorite(id, !currentFavorite)
    }

    suspend fun updateUserNotes(id: String, notes: String) {
        dao.updateUserNotes(id, notes)
    }

    suspend fun saveRecipe(recipe: RecipeEntity) {
        dao.insert(recipe)
    }

    suspend fun deleteRecipe(recipe: RecipeEntity) {
        dao.delete(recipe)
    }
}
