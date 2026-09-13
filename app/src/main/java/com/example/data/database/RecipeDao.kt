package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getRecipeCount(): Int

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    fun getRecipeById(id: String): Flow<RecipeEntity?>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE isCustom = 1 ORDER BY name ASC")
    fun getCustomRecipes(): Flow<List<RecipeEntity>>

    @Query("""
        SELECT * FROM recipes 
        WHERE (:category IS NULL OR category = :category OR categoryFa = :category)
        AND (:protein IS NULL OR baseProtein = :protein OR baseProteinFa = :protein)
        AND (:flavor IS NULL OR flavorProfile = :flavor OR flavorProfileFa = :flavor)
        AND (:query IS NULL OR :query = '' OR name LIKE '%' || :query || '%' OR englishName LIKE '%' || :query || '%' OR recommendedCut LIKE '%' || :query || '%' OR shortDescription LIKE '%' || :query || '%' OR ingredientsJson LIKE '%' || :query || '%')
        ORDER BY 
            CASE WHEN isFavorite = 1 THEN 0 ELSE 1 END,
            name ASC
    """)
    fun filterRecipes(
        query: String?,
        category: String?,
        protein: String?,
        flavor: String?
    ): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeEntity)

    @Update
    suspend fun update(recipe: RecipeEntity)

    @Delete
    suspend fun delete(recipe: RecipeEntity)

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE recipes SET userNotes = :notes WHERE id = :id")
    suspend fun updateUserNotes(id: String, notes: String)

    @Query("SELECT COUNT(*) FROM recipes WHERE categoryFa = :categoryFa")
    suspend fun getCountByCategory(categoryFa: String): Int

    @Query("DELETE FROM recipes WHERE isCustom = 0")
    suspend fun deleteNonCustomRecipes()

    @Query("SELECT id FROM recipes WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<String>
}
