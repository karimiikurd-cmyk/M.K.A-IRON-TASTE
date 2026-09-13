package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.AddEditRecipeScreen
import com.example.ui.screens.ButcherToolsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBlack
import com.example.ui.viewmodel.RecipeViewModel

sealed interface Screen {
    data object Home : Screen
    data class Detail(val recipeId: String) : Screen
    data class AddEdit(val recipeId: String? = null) : Screen
    data object Tools : Screen
}

class MainActivity : ComponentActivity() {
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                // Professional RTL layout for Persian / Kurdish language
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = ObsidianBlack
                    ) {
                        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

                        val recipes by viewModel.recipes.collectAsStateWithLifecycle()
                        val totalCount by viewModel.recipeCount.collectAsStateWithLifecycle()
                        val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                        val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
                        val selectedProtein by viewModel.selectedProtein.collectAsStateWithLifecycle()
                        val selectedFlavor by viewModel.selectedFlavor.collectAsStateWithLifecycle()
                        val batchWeightKg by viewModel.batchWeightKg.collectAsStateWithLifecycle()
                        val isPopulating by viewModel.isPopulating.collectAsStateWithLifecycle()

                        // Handle back button for sub-screens
                        if (currentScreen !is Screen.Home) {
                            BackHandler {
                                currentScreen = Screen.Home
                            }
                        }

                        when (val screen = currentScreen) {
                            is Screen.Home -> {
                                HomeScreen(
                                    recipes = recipes,
                                    totalCount = totalCount,
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = viewModel::onSearchQueryChange,
                                    selectedCategory = selectedCategory,
                                    onCategorySelect = viewModel::onCategorySelect,
                                    selectedProtein = selectedProtein,
                                    onProteinSelect = viewModel::onProteinSelect,
                                    selectedFlavor = selectedFlavor,
                                    onFlavorSelect = viewModel::onFlavorSelect,
                                    onClearAllFilters = viewModel::clearAllFilters,
                                    isPopulating = isPopulating,
                                    onRecipeClick = { recipeId ->
                                        currentScreen = Screen.Detail(recipeId)
                                    },
                                    onToggleFavorite = viewModel::toggleFavorite,
                                    onOpenTools = {
                                        currentScreen = Screen.Tools
                                    },
                                    onAddNewRecipe = {
                                        currentScreen = Screen.AddEdit()
                                    }
                                )
                            }

                            is Screen.Detail -> {
                                val currentRecipe by viewModel.getRecipeFlow(screen.recipeId)
                                    .collectAsStateWithLifecycle(initialValue = null)

                                RecipeDetailScreen(
                                    recipe = currentRecipe,
                                    batchWeightKg = batchWeightKg,
                                    onWeightChange = viewModel::onBatchWeightChange,
                                    onToggleFavorite = viewModel::toggleFavorite,
                                    onSaveNotes = viewModel::updateUserNotes,
                                    onEdit = { recipe ->
                                        currentScreen = Screen.AddEdit(recipe.id)
                                    },
                                    onDelete = { recipe ->
                                        viewModel.deleteRecipe(recipe)
                                    },
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }

                            is Screen.AddEdit -> {
                                val recipeToEdit by if (screen.recipeId != null) {
                                    viewModel.getRecipeFlow(screen.recipeId)
                                        .collectAsStateWithLifecycle(initialValue = null)
                                } else {
                                    remember { mutableStateOf<com.example.data.model.RecipeEntity?>(null) }
                                }

                                AddEditRecipeScreen(
                                    initialRecipe = recipeToEdit,
                                    onSave = viewModel::saveRecipe,
                                    onBack = {
                                        currentScreen = if (screen.recipeId != null) Screen.Detail(screen.recipeId) else Screen.Home
                                    }
                                )
                            }

                            is Screen.Tools -> {
                                ButcherToolsScreen(
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
