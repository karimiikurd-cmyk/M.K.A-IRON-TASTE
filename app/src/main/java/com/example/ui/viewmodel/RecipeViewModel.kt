package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.RecipeEntity
import com.example.data.repository.RecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    // Batch Weight Scaling (default: 1.0 kg)
    private val _batchWeightKg = MutableStateFlow(1.0)
    val batchWeightKg: StateFlow<Double> = _batchWeightKg.asStateFlow()

    // Total Count in Database (dynamically observed from Room Flow)
    val recipeCount: StateFlow<Int> = repository.getAllRecipes()
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
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
        _selectedFlavor
    ) { query, category, protein, flavor ->
        FilterParams(query, category, protein, flavor)
    }.flatMapLatest { params ->
        when (params.category) {
            "نشان‌شده‌ها" -> repository.getFavoriteRecipes().map { list ->
                applyLocalFilters(list, params.query, params.protein, params.flavor)
            }
            "دستورهای من" -> repository.getCustomRecipes().map { list ->
                applyLocalFilters(list, params.query, params.protein, params.flavor)
            }
            else -> {
                val catParam = if (params.category == "همه") null else params.category
                repository.filterRecipes(params.query, catParam, params.protein, params.flavor)
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
        flavor: String?
    ): List<RecipeEntity> {
        return list.filter { recipe ->
            val matchQuery = query.isBlank() ||
                    recipe.name.contains(query, ignoreCase = true) ||
                    recipe.englishName.contains(query, ignoreCase = true) ||
                    recipe.recommendedCut.contains(query, ignoreCase = true) ||
                    recipe.shortDescription.contains(query, ignoreCase = true) ||
                    recipe.ingredientsJson.contains(query, ignoreCase = true)
            val matchProtein = protein == null || recipe.baseProtein == protein || recipe.baseProteinFa == protein
            val matchFlavor = flavor == null || recipe.flavorProfile == flavor || recipe.flavorProfileFa == flavor
            matchQuery && matchProtein && matchFlavor
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

    fun clearAllFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = "همه"
        _selectedProtein.value = null
        _selectedFlavor.value = null
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

    fun getRecipeFlow(id: String): Flow<RecipeEntity?> {
        return repository.getRecipeById(id)
    }

    private data class FilterParams(
        val query: String,
        val category: String,
        val protein: String?,
        val flavor: String?
    )
}
