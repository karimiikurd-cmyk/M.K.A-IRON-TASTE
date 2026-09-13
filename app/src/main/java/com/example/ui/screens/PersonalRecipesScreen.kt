package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.RecipeEntity
import com.example.data.model.RecipeVersionEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.RecipeViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalRecipesScreen(
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToRecipe: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("رسپی‌های من و نسخه‌بندی", "مقایسه دو رسپی")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "رسپی‌های شخصی و مقایسه",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCopper
                        )
                        Text(
                            "M.K.A IRON TASTE • سیستم نسخه‌بندی و آنالیز تطبیقی",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("personal_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت", tint = PrimaryCopper)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        },
        containerColor = DarkBackground,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurfaceVariant,
                contentColor = PrimaryCopper,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimaryCopper,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) PrimaryCopper else TextMuted
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> MyRecipesManagementTab(viewModel = viewModel, onNavigateToRecipe = onNavigateToRecipe)
                1 -> RecipeComparisonTab(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun MyRecipesManagementTab(
    viewModel: RecipeViewModel,
    onNavigateToRecipe: (String) -> Unit
) {
    val context = LocalContext.current
    val customRecipes by viewModel.customRecipes.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var recipeForVersionDialog by remember { mutableStateOf<RecipeEntity?>(null) }
    var recipeForHistoryDialog by remember { mutableStateOf<RecipeEntity?>(null) }
    var recipeToDelete by remember { mutableStateOf<RecipeEntity?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryCopper, FlameAccent))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "مدیریت فرمول‌ها و نسخه‌های شخصی",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryCopper
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                "امکان ثبت تغییرات (نسخه ۱، ۲، ۳)، ویرایش، کپی‌برداری و یادداشت‌گذاری بدون پاک‌شدن نسخه‌های پیشین.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            if (customRecipes.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.EditNote, contentDescription = null, tint = PrimaryCopper, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("هنوز رسپی شخصی ثبت نکرده‌اید", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("با لمس دکمه زیر می‌توانید فرمول مرینیت اختصاصی خود را بسازید یا از بخش آزمایشگاه اضافه کنید.", style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                items(customRecipes, key = { it.id }) { recipe ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(12.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(DarkBorder, PrimaryCopper.copy(alpha = 0.3f)))),
                        modifier = Modifier.fillMaxWidth().testTag("custom_recipe_card_${recipe.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(recipe.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text("پروتئین: ${recipe.baseProteinFa} • پروفایل: ${recipe.flavorProfileFa}", style = MaterialTheme.typography.labelSmall, color = PrimaryCopper)
                                }
                                IconButton(onClick = { viewModel.toggleFavorite(recipe) }) {
                                    Icon(
                                        if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "علاقه‌مندی",
                                        tint = if (recipe.isFavorite) FlameAccent else TextMuted
                                    )
                                }
                            }

                            if (recipe.userNotes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("یادداشت: ${recipe.userNotes}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = DarkBorder)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Action Buttons Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onNavigateToRecipe(recipe.id) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryCopper),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(vertical = 4.dp)
                                ) {
                                    Text("مشاهده", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { recipeForVersionDialog = recipe },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryCopper),
                                    modifier = Modifier.weight(1.2f),
                                    contentPadding = PaddingValues(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("ثبت نسخه جدید", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { recipeForHistoryDialog = recipe },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryCopper),
                                    modifier = Modifier.weight(1.1f),
                                    contentPadding = PaddingValues(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("تاریخچه نسخه‌ها", fontSize = 11.sp)
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.duplicateRecipe(recipe) {
                                            Toast.makeText(context, "کپی دستور ایجاد شد", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "کپی", tint = TextMuted, modifier = Modifier.size(18.dp))
                                }

                                IconButton(onClick = { recipeToDelete = recipe }) {
                                    Icon(Icons.Default.Delete, contentDescription = "حذف", tint = FlameAccent, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // FAB to create new recipe
        FloatingActionButton(
            onClick = { showCreateDialog = true },
            containerColor = PrimaryCopper,
            contentColor = DarkBackground,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_create_personal_recipe")
        ) {
            Icon(Icons.Default.Add, contentDescription = "افزودن دستور جدید")
        }
    }

    // Dialog: Create New Personal Recipe
    if (showCreateDialog) {
        CreateRecipeDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { entity ->
                viewModel.saveRecipe(entity)
                showCreateDialog = false
                Toast.makeText(context, "دستور جدید با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Dialog: Create New Version
    recipeForVersionDialog?.let { parentRecipe ->
        CreateNewVersionDialog(
            parentRecipe = parentRecipe,
            onDismiss = { recipeForVersionDialog = null },
            onSaveVersion = { versionName, notes ->
                viewModel.saveNewVersion(parentRecipe, versionName, notes)
                recipeForVersionDialog = null
                Toast.makeText(context, "نسخه جدید «$versionName» ذخیره شد", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Dialog: Version History
    recipeForHistoryDialog?.let { recipe ->
        VersionHistoryDialog(
            recipe = recipe,
            viewModel = viewModel,
            onDismiss = { recipeForHistoryDialog = null }
        )
    }

    // Confirm Delete Dialog
    recipeToDelete?.let { recipe ->
        AlertDialog(
            onDismissRequest = { recipeToDelete = null },
            title = { Text("حذف دستور شخصی", color = TextPrimary) },
            text = { Text("آیا از حذف دستور «${recipe.name}» و تمام نسخه‌های آن اطمینان دارید؟", color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteRecipe(recipe)
                        recipeToDelete = null
                        Toast.makeText(context, "دستور حذف شد", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("حذف", color = FlameAccent, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { recipeToDelete = null }) {
                    Text("انصراف", color = TextMuted)
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
private fun RecipeComparisonTab(viewModel: RecipeViewModel) {
    val allRecipes by viewModel.allRecipesList.collectAsState()
    var recipe1 by remember { mutableStateOf<RecipeEntity?>(null) }
    var recipe2 by remember { mutableStateOf<RecipeEntity?>(null) }
    var showPicker1 by remember { mutableStateOf(false) }
    var showPicker2 by remember { mutableStateOf(false) }

    // Defaults if null and available
    LaunchedEffect(allRecipes) {
        if (recipe1 == null && allRecipes.isNotEmpty()) recipe1 = allRecipes.firstOrNull()
        if (recipe2 == null && allRecipes.size > 1) recipe2 = allRecipes.getOrNull(1)
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryCopper, FlameAccent))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("⚖️ موتور مقایسه تطبیقی دو فرمولاسیون", fontWeight = FontWeight.Bold, color = PrimaryCopper, style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text("مقایسه علمی و کارگاهی مقادیر نمک، اسیدیته، روغن، ادویه‌جات، زمان خواباندن، روش پخت و پروفایل طعمی.", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Selection Cards Row
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Recipe 1 Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryCopper, DarkBorder))),
                    modifier = Modifier.weight(1f).clickable { showPicker1 = true }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("دستور اول (لمس برای انتخاب):", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(recipe1?.name ?: "انتخاب دستور...", fontWeight = FontWeight.Bold, color = PrimaryCopper, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                    }
                }

                // Recipe 2 Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FlameAccent, DarkBorder))),
                    modifier = Modifier.weight(1f).clickable { showPicker2 = true }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("دستور دوم (لمس برای انتخاب):", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(recipe2?.name ?: "انتخاب دستور...", fontWeight = FontWeight.Bold, color = FlameAccent, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                    }
                }
            }
        }

        // Comparison Matrix
        if (recipe1 != null && recipe2 != null) {
            val r1 = recipe1!!
            val r2 = recipe2!!

            item {
                Text("ماتریس تفاوت‌ها و مشخصات:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, color = PrimaryCopper)
            }

            item { ComparisonRow("پروتئین پایه", r1.baseProteinFa, r2.baseProteinFa) }
            item { ComparisonRow("پروفایل طعمی", r1.flavorProfileFa, r2.flavorProfileFa) }
            item { ComparisonRow("برش پیشنهادی", r1.recommendedCut, r2.recommendedCut) }
            item { ComparisonRow("زمان مرینیت", r1.marinationTime, r2.marinationTime) }
            item { ComparisonRow("روش و تکنیک پخت", r1.cookingMethod, r2.cookingMethod) }
            item { ComparisonRow("دمای پخت", r1.cookingTemp, r2.cookingTemp) }
            item { ComparisonRow("میزان تندی", r1.spiceLevel, r2.spiceLevel) }
            item { ComparisonRow("کاربرد توصیه شده", r1.recommendedUse, r2.recommendedUse) }
        }
    }

    if (showPicker1) {
        RecipePickerSheet(allRecipes = allRecipes, onDismiss = { showPicker1 = false }, onSelect = { recipe1 = it; showPicker1 = false })
    }

    if (showPicker2) {
        RecipePickerSheet(allRecipes = allRecipes, onDismiss = { showPicker2 = false }, onSelect = { recipe2 = it; showPicker2 = false })
    }
}

@Composable
private fun ComparisonRow(label: String, val1: String, val2: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PrimaryCopper)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(val1, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                Divider(modifier = Modifier.width(1.dp).height(20.dp), color = DarkBorder)
                Text(val2, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = FlameAccent)
            }
        }
    }
}

@Composable
private fun RecipePickerSheet(
    allRecipes: List<RecipeEntity>,
    onDismiss: () -> Unit,
    onSelect: (RecipeEntity) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, allRecipes) {
        if (query.isBlank()) allRecipes.take(50)
        else allRecipes.filter { it.name.contains(query, ignoreCase = true) || it.englishName.contains(query, ignoreCase = true) }.take(50)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                Text("انتخاب رسپی برای مقایسه", fontWeight = FontWeight.Bold, color = PrimaryCopper, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("جستجو...", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = PrimaryCopper,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filtered) { r ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(r) }
                                .padding(vertical = 10.dp)
                        ) {
                            Text(r.name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text(r.categoryFa, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Divider(color = DarkBorder.copy(alpha = 0.5f), modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateRecipeDialog(
    onDismiss: () -> Unit,
    onSave: (RecipeEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("مرغ") }
    var flavor by remember { mutableStateOf("زعفرانی و لیمو") }
    var cut by remember { mutableStateOf("سینه و فیله") }
    var marinationTime by remember { mutableStateOf("۴ تا ۶ ساعت") }
    var cookingMethod by remember { mutableStateOf("گریل زغالی") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("ثبت رسپی شخصی جدید", fontWeight = FontWeight.Bold, color = PrimaryCopper, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("نام رسپی (مثلاً جوجه کباب کره‌ای مخصوص)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = protein,
                            onValueChange = { protein = it },
                            label = { Text("پروتئین پایه (مرغ، گوساله، بوقلمون...)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = flavor,
                            onValueChange = { flavor = it },
                            label = { Text("پروفایل طعمی") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = cut,
                            onValueChange = { cut = it },
                            label = { Text("برش گوشت") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = marinationTime,
                            onValueChange = { marinationTime = it },
                            label = { Text("زمان استراحت") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = cookingMethod,
                            onValueChange = { cookingMethod = it },
                            label = { Text("روش پخت") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("یادداشت‌های اختصاصی") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("انصراف", color = TextMuted) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                val newId = "custom_${UUID.randomUUID().toString().take(8)}"
                                val entity = RecipeEntity(
                                    id = newId,
                                    name = name,
                                    englishName = name,
                                    category = "Custom",
                                    categoryFa = "دستورهای من",
                                    shortDescription = "فرمول اختصاصی ایجاد شده توسط کاربر در M.K.A IRON TASTE.",
                                    baseProtein = protein,
                                    baseProteinFa = protein,
                                    recommendedCut = cut,
                                    flavorProfile = "Custom",
                                    flavorProfileFa = flavor,
                                    baseQuantityKg = 1.0,
                                    ingredientsJson = "[{\"name\":\"پروتئین خالص\",\"amount\":1000.0,\"unit\":\"گرم\",\"notes\":\"\"}]",
                                    prepStepsJson = "[\"۱. آماده‌سازی و شستشو\",\"۲. اضافه کردن مرینیت\",\"۳. خواباندن در یخچال\"]",
                                    marinationTime = marinationTime,
                                    cookingMethod = cookingMethod,
                                    cookingTemp = "حرارت استاندارد",
                                    spiceLevel = "متعادل",
                                    recommendedUse = "پذیرایی و قصابی",
                                    storageNotes = "دمای ۱ تا ۳ درجه",
                                    proTips = "رعایت نسبت نمک ۱.۴٪",
                                    commonMistakes = "افزودن روغن قبل از نمک",
                                    substitutions = "-",
                                    isCustom = true,
                                    isFavorite = false,
                                    userNotes = notes
                                )
                                onSave(entity)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryCopper)
                    ) {
                        Text("ذخیره رسپی", color = DarkBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateNewVersionDialog(
    parentRecipe: RecipeEntity,
    onDismiss: () -> Unit,
    onSaveVersion: (String, String) -> Unit
) {
    var versionName by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ثبت نسخه جدید (Versioning)", fontWeight = FontWeight.Bold, color = PrimaryCopper, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("دستور مبنا: ${parentRecipe.name}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = versionName,
                    onValueChange = { versionName = it },
                    label = { Text("نام نسخه (مثلاً نسخه ۲: کاهش نمک و افزودن رزماری)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("توضیحات و تغییرات اعمال شده نسبت به نسخه قبل") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = PrimaryCopper),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("انصراف", color = TextMuted) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (versionName.isNotBlank()) {
                                onSaveVersion(versionName, notes)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryCopper)
                    ) {
                        Text("ذخیره نسخه", color = DarkBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun VersionHistoryDialog(
    recipe: RecipeEntity,
    viewModel: RecipeViewModel,
    onDismiss: () -> Unit
) {
    val versionsFlow = remember(recipe.id) { viewModel.getVersionsFlow(recipe.id) }
    val versions by versionsFlow.collectAsState(initial = emptyList())

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("تاریخچه نسخه‌های ${recipe.name}", fontWeight = FontWeight.Bold, color = PrimaryCopper, style = MaterialTheme.typography.titleSmall)
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "بستن", tint = TextMuted) }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (versions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("هنوز نسخه مجزایی برای این دستور ثبت نشده است.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
                        items(versions) { ver ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(ver.versionName, fontWeight = FontWeight.Bold, color = PrimaryCopper, style = MaterialTheme.typography.bodyMedium)
                                        IconButton(onClick = { viewModel.deleteVersion(ver.versionId) }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Default.Delete, contentDescription = "حذف نسخه", tint = FlameAccent, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                    if (ver.notes.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(ver.notes, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
