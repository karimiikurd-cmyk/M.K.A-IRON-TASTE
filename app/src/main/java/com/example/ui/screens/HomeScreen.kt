package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.RecipeEntity
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    recipes: List<RecipeEntity>,
    totalCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    selectedProtein: String?,
    onProteinSelect: (String?) -> Unit,
    selectedFlavor: String?,
    onFlavorSelect: (String?) -> Unit,
    onClearAllFilters: () -> Unit,
    isPopulating: Boolean,
    onRecipeClick: (String) -> Unit,
    onToggleFavorite: (RecipeEntity) -> Unit,
    onOpenTools: () -> Unit,
    onAddNewRecipe: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val hasActiveFilter = selectedProtein != null || selectedFlavor != null || selectedCategory != "همه"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ObsidianBlack,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNewRecipe,
                containerColor = CopperFlame,
                contentColor = ObsidianBlack,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("دستور کارگاهی جدید", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("fab_add_recipe")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // M.K.A IRON TASTE Official Header & Brand Wordmark
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                BurgundyDeep.copy(alpha = 0.85f),
                                CharcoalDark,
                                ObsidianBlack
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                CopperFlame.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Navigation / Action Row: Tools & About
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Brand Philosophy / About Button
                        OutlinedButton(
                            onClick = { showAboutDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = CopperLight
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CopperFlame.copy(alpha = 0.4f)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("button_open_about")
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "درباره برند",
                                tint = CopperFlame,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "درباره برند",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = WarmCream
                            )
                        }

                        // Chef's Signature Monogram / Kurdish Rhombus
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            KurdishRhombusAccent(size = 12.dp, color = CopperFlame)
                            Text(
                                text = "CHEF SIGNATURE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                color = CopperLight.copy(alpha = 0.75f)
                            )
                        }

                        // Kitchen Tools Button
                        IconButton(
                            onClick = onOpenTools,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CharcoalDark)
                                .border(1.dp, CopperFlame.copy(alpha = 0.6f), CircleShape)
                                .testTag("button_open_tools")
                        ) {
                            Icon(
                                Icons.Default.Calculate,
                                contentDescription = "ابزارهای قصاب و سرآشپز",
                                tint = CopperFlame,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Prominent "M.K.A" Signature Monogram
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(1.dp)
                                .width(36.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color.Transparent, CopperFlame)
                                    )
                                )
                        )

                        Text(
                            text = "M . K . A",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 6.sp,
                            color = WarmCream,
                            modifier = Modifier.testTag("header_mka_signature")
                        )

                        Box(
                            modifier = Modifier
                                .height(1.dp)
                                .width(36.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(CopperFlame, Color.Transparent)
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Main Brand Wordmark: "IRON TASTE"
                    Surface(
                        shape = androidx.compose.foundation.shape.CutCornerShape(4.dp),
                        color = ObsidianBlack.copy(alpha = 0.7f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CopperFlame.copy(alpha = 0.5f)),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "IRON TASTE",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 4.sp,
                            color = CopperFlame,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .testTag("header_iron_taste_wordmark")
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Subtitle & Total Recipes
                    Text(
                        text = if (totalCount > 0) "طعم قدرتمند با هویت صنعتی راک/متال • $totalCount دستور تخصصی" else "بارگذاری بانک دستورها...",
                        fontSize = 11.sp,
                        color = AntiqueParchment,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Search Bar & Filter Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("search_text_field"),
                    placeholder = {
                        Text(
                            text = "جستجوی نام، برش گوشت یا ادویه...",
                            fontSize = 13.sp,
                            color = SilverMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = CopperLight
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "پاک‌کردن", tint = SilverMuted)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MetalSurface,
                        unfocusedContainerColor = MetalSurface,
                        focusedBorderColor = CopperFlame,
                        unfocusedBorderColor = MetalBorder,
                        focusedTextColor = WarmCream,
                        unfocusedTextColor = WarmCream
                    )
                )

                // Filter Trigger Button
                IconButton(
                    onClick = { showFilterDialog = true },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (hasActiveFilter) BurgundyPrimary else MetalSurface)
                        .border(
                            1.dp,
                            if (hasActiveFilter) CrimsonBright else MetalBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .testTag("button_open_filters")
                ) {
                    BadgedBox(
                        badge = {
                            if (hasActiveFilter) {
                                Badge(
                                    containerColor = AmberGlow,
                                    contentColor = ObsidianBlack
                                ) {
                                    Text("!", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "فیلتر پیشرفته",
                            tint = if (hasActiveFilter) WarmCream else CopperLight
                        )
                    }
                }
            }

            // Category Chips Row
            CategoryChips(
                selectedCategory = selectedCategory,
                onCategorySelect = onCategorySelect
            )

            // Results count and active filter summary banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${recipes.size} دستور یافت شد",
                    fontSize = 12.sp,
                    color = SilverMuted,
                    fontWeight = FontWeight.Medium
                )

                if (hasActiveFilter || searchQuery.isNotBlank()) {
                    TextButton(
                        onClick = onClearAllFilters,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = "پاک‌کردن فیلترها",
                            color = CopperLight,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Main List or Loading / Empty State
            if (isPopulating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = CopperFlame)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "در حال بارگذاری بانک ۱۰۰۰+ دستور تخصصی در پایگاه داده محلی...",
                            color = WarmCream,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                    }
                }
            } else if (recipes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        when (selectedCategory) {
                            "نشان‌شده‌ها" -> {
                                Icon(
                                    Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    tint = CopperFlame,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = "هنوز دستوری را نشان نکرده‌اید",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmCream
                                )
                                Text(
                                    text = "با کلیک روی آیکون نشان در کنار هر دستور، آن را برای دسترسی سریع به این لیست اضافه کنید.",
                                    color = SilverMuted,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    onClick = onClearAllFilters,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BurgundyPrimary,
                                        contentColor = WarmCream
                                    )
                                ) {
                                    Text("مشاهده همه دستورها")
                                }
                            }
                            "دستورهای من" -> {
                                Icon(
                                    Icons.Default.RestaurantMenu,
                                    contentDescription = null,
                                    tint = CopperFlame,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = "هنوز دستور اختصاصی ثبت نکرده‌اید",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmCream
                                )
                                Text(
                                    text = "می‌توانید فرمول‌ها، ادویه‌جات و روش‌های اختصاصی خود را در این بخش ثبت و مقیاس‌بندی کنید.",
                                    color = SilverMuted,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    onClick = onAddNewRecipe,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CopperFlame,
                                        contentColor = ObsidianBlack
                                    )
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("ثبت اولین دستور کارگاهی", fontWeight = FontWeight.Bold)
                                }
                            }
                            else -> {
                                Icon(
                                    Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = SilverMuted,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = "دستوری با این مشخصات یافت نشد",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmCream
                                )
                                Text(
                                    text = "لطفاً عبارت جستجو یا فیلترهای انتخاب‌شده را تغییر دهید.",
                                    color = SilverMuted,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    onClick = onClearAllFilters,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BurgundyPrimary,
                                        contentColor = WarmCream
                                    )
                                ) {
                                    Text("مشاهده همه دستورها")
                                }
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("recipe_list"),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = recipes,
                        key = { it.id }
                    ) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { onRecipeClick(recipe.id) },
                            onToggleFavorite = { onToggleFavorite(recipe) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            selectedProtein = selectedProtein,
            selectedFlavor = selectedFlavor,
            onProteinSelect = onProteinSelect,
            onFlavorSelect = onFlavorSelect,
            onClearAll = onClearAllFilters,
            onDismiss = { showFilterDialog = false }
        )
    }

    if (showAboutDialog) {
        AboutMkaIronTasteDialog(
            onDismiss = { showAboutDialog = false }
        )
    }
}
