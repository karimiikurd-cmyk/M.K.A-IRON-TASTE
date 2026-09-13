package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GeneratedRecipeConcept
import com.example.data.model.RecipeEntity
import com.example.data.model.RecipeVersionEntity
import com.example.data.repository.RecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecipeRepository(application)

    val isPopulating: StateFlow<Boolean> = repository.isPopulating

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("همه")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedProtein = MutableStateFlow<String?>(null)
    val selectedProtein: StateFlow<String?> = _selectedProtein.asStateFlow()

    private val _selectedFlavor = MutableStateFlow<String?>(null)
    val selectedFlavor: StateFlow<String?> = _selectedFlavor.asStateFlow()

    private val _selectedCookingMethod = MutableStateFlow<String?>(null)
    val selectedCookingMethod: StateFlow<String?> = _selectedCookingMethod.asStateFlow()

    // Batch Weight Scaling (default: 1.0 kg)
    private val _batchWeightKg = MutableStateFlow(1.0)
    val batchWeightKg: StateFlow<Double> = _batchWeightKg.asStateFlow()

    // All recipes in Room Flow
    val allRecipesList: StateFlow<List<RecipeEntity>> = repository.getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Total Count in Database
    val recipeCount: StateFlow<Int> = allRecipesList
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Custom / Personal Recipes
    val customRecipes: StateFlow<List<RecipeEntity>> = repository.getCustomRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.initialize()
        }
    }

    // Filtered recipes stream
    val recipes: StateFlow<List<RecipeEntity>> = combine(
        _searchQuery,
        _selectedCategory,
        _selectedProtein,
        _selectedFlavor,
        _selectedCookingMethod
    ) { query, category, protein, flavor, cookingMethod ->
        FilterParams(query, category, protein, flavor, cookingMethod)
    }.flatMapLatest { params ->
        when (params.category) {
            "نشان‌شده‌ها" -> repository.getFavoriteRecipes().map { list ->
                applyLocalFilters(list, params.query, params.protein, params.flavor, params.cookingMethod)
            }
            "دستورهای من" -> repository.getCustomRecipes().map { list ->
                applyLocalFilters(list, params.query, params.protein, params.flavor, params.cookingMethod)
            }
            else -> {
                val catParam = if (params.category == "همه") null else params.category
                repository.filterRecipes(params.query, catParam, params.protein, params.flavor).map { list ->
                    if (params.cookingMethod != null) {
                        list.filter { it.cookingMethod.contains(params.cookingMethod, ignoreCase = true) }
                    } else list
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private fun applyLocalFilters(
        list: List<RecipeEntity>,
        query: String,
        protein: String?,
        flavor: String?,
        cookingMethod: String?
    ): List<RecipeEntity> {
        return list.filter { recipe ->
            val matchQuery = query.isBlank() ||
                    recipe.name.contains(query, ignoreCase = true) ||
                    recipe.englishName.contains(query, ignoreCase = true) ||
                    recipe.recommendedCut.contains(query, ignoreCase = true) ||
                    recipe.shortDescription.contains(query, ignoreCase = true) ||
                    recipe.ingredientsJson.contains(query, ignoreCase = true) ||
                    recipe.cookingMethod.contains(query, ignoreCase = true) ||
                    recipe.recommendedUse.contains(query, ignoreCase = true)
            val matchProtein = protein == null || recipe.baseProtein == protein || recipe.baseProteinFa == protein
            val matchFlavor = flavor == null || recipe.flavorProfile == flavor || recipe.flavorProfileFa == flavor
            val matchCooking = cookingMethod == null || recipe.cookingMethod.contains(cookingMethod, ignoreCase = true)
            matchQuery && matchProtein && matchFlavor && matchCooking
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun onProteinSelect(protein: String?) {
        _selectedProtein.value = protein
    }

    fun onFlavorSelect(flavor: String?) {
        _selectedFlavor.value = flavor
    }

    fun onCookingMethodSelect(method: String?) {
        _selectedCookingMethod.value = method
    }

    fun clearAllFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = "همه"
        _selectedProtein.value = null
        _selectedFlavor.value = null
        _selectedCookingMethod.value = null
    }

    fun onBatchWeightChange(weightKg: Double) {
        _batchWeightKg.value = weightKg
    }

    fun toggleFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe.id, recipe.isFavorite)
        }
    }

    fun updateUserNotes(recipeId: String, notes: String) {
        viewModelScope.launch {
            repository.updateUserNotes(recipeId, notes)
        }
    }

    fun saveRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.saveRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
        }
    }

    fun duplicateRecipe(recipe: RecipeEntity, onComplete: (RecipeEntity) -> Unit) {
        viewModelScope.launch {
            val duplicated = repository.duplicateRecipe(recipe)
            onComplete(duplicated)
        }
    }

    fun getRecipeFlow(id: String): Flow<RecipeEntity?> {
        return repository.getRecipeById(id)
    }

    fun getVersionsFlow(recipeId: String): Flow<List<RecipeVersionEntity>> {
        return repository.getVersionsForRecipe(recipeId)
    }

    fun saveNewVersion(
        parentRecipe: RecipeEntity,
        versionName: String,
        notes: String,
        updatedIngredientsJson: String? = null,
        updatedStepsJson: String? = null
    ) {
        viewModelScope.launch {
            val version = RecipeVersionEntity(
                parentRecipeId = parentRecipe.id,
                versionName = versionName.ifBlank { "نسخه جدید" },
                notes = notes,
                ingredientsJson = updatedIngredientsJson ?: parentRecipe.ingredientsJson,
                prepStepsJson = updatedStepsJson ?: parentRecipe.prepStepsJson,
                baseQuantityKg = parentRecipe.baseQuantityKg,
                marinationTime = parentRecipe.marinationTime,
                cookingMethod = parentRecipe.cookingMethod,
                flavorProfile = parentRecipe.flavorProfileFa
            )
            repository.saveVersion(version)
        }
    }

    fun deleteVersion(versionId: Long) {
        viewModelScope.launch {
            repository.deleteVersion(versionId)
        }
    }

    fun saveConceptAsPersonalRecipe(concept: GeneratedRecipeConcept, onSaved: (String) -> Unit) {
        viewModelScope.launch {
            val ingArray = JSONArray()
            concept.ingredients.forEach { ing ->
                val obj = JSONObject()
                obj.put("name", ing.name)
                obj.put("amount", ing.amount)
                obj.put("unit", ing.unit)
                obj.put("notes", ing.notes)
                ingArray.put(obj)
            }

            val stepArray = JSONArray()
            concept.prepSequence.forEach { step ->
                stepArray.put(step)
            }

            val newId = "custom_lab_${UUID.randomUUID().toString().take(8)}"
            val entity = RecipeEntity(
                id = newId,
                name = concept.title,
                englishName = concept.englishTitle,
                category = "CustomLab",
                categoryFa = "آزمایشگاه رسپی",
                shortDescription = "فرمول کارگاهی تولید شده در آزمایشگاه رسپی M.K.A بر مبنای ترکیبات در دسترس.",
                baseProtein = concept.baseProtein,
                baseProteinFa = concept.baseProteinFa,
                recommendedCut = "برش استاندارد کارگاهی",
                flavorProfile = "Custom",
                flavorProfileFa = concept.flavorProfileFa,
                baseQuantityKg = 1.0,
                ingredientsJson = ingArray.toString(),
                prepStepsJson = stepArray.toString(),
                marinationTime = concept.marinationTime,
                cookingMethod = concept.cookingMethod,
                cookingTemp = concept.cookingTemp,
                spiceLevel = "متعادل",
                recommendedUse = "عرضه ویترینی قصابی و پذیرایی رستورانی",
                storageNotes = "نگهداری در ظروف استیل درب‌دار در دمای ۱ تا ۳ درجه سانتی‌گراد",
                proTips = concept.proTips,
                commonMistakes = "خواباندن بیش از حد در اسید یا حرارت‌دهی بیش از اندازه",
                substitutions = "امکان جایگزینی بر اساس راهنمای نجات رسپی",
                isCustom = true,
                isFavorite = false,
                userNotes = "ایجاد شده در آزمایشگاه رسپی"
            )

            repository.saveRecipe(entity)
            onSaved(newId)
        }
    }

    private data class FilterParams(
        val query: String,
        val category: String,
        val protein: String?,
        val flavor: String?,
        val cookingMethod: String?
    )
}

