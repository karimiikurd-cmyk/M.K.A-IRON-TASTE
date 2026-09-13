package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.RecipeTypeConverters
import com.example.data.model.Ingredient
import com.example.data.model.RecipeEntity
import com.example.ui.components.ALL_CATEGORIES
import com.example.ui.theme.*
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    initialRecipe: RecipeEntity? = null,
    onSave: (RecipeEntity) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isEditing = initialRecipe != null

    var name by remember { mutableStateOf(initialRecipe?.name ?: "") }
    var englishName by remember { mutableStateOf(initialRecipe?.englishName ?: "") }
    var categoryFa by remember { mutableStateOf(initialRecipe?.categoryFa ?: "جوجه کباب و مرغ آماده") }
    var recommendedCut by remember { mutableStateOf(initialRecipe?.recommendedCut ?: "") }
    var flavorProfileFa by remember { mutableStateOf(initialRecipe?.flavorProfileFa ?: "زعفرانی") }
    var spiceLevel by remember { mutableStateOf(initialRecipe?.spiceLevel ?: "ملایم") }
    var shortDescription by remember { mutableStateOf(initialRecipe?.shortDescription ?: "") }
    var marinationTime by remember { mutableStateOf(initialRecipe?.marinationTime ?: "۴ تا ۸ ساعت") }
    var cookingMethod by remember { mutableStateOf(initialRecipe?.cookingMethod ?: "منقل زغالی یا گریل") }
    var cookingTemp by remember { mutableStateOf(initialRecipe?.cookingTemp ?: "حرارت متوسط") }
    var proTips by remember { mutableStateOf(initialRecipe?.proTips ?: "") }
    var commonMistakes by remember { mutableStateOf(initialRecipe?.commonMistakes ?: "") }

    val converters = remember { RecipeTypeConverters() }

    // Ingredients list
    val initialIngs = remember(initialRecipe) {
        if (initialRecipe != null) {
            converters.toIngredientsList(initialRecipe.ingredientsJson)
        } else {
            listOf(
                Ingredient("پروتئین اصلی خالص", 1000.0, "گرم"),
                Ingredient("زعفران دم‌کرده یا چاشنی اصلی", 30.0, "میلی‌لیتر"),
                Ingredient("نمک تصفیه‌شده", 14.0, "گرم")
            )
        }
    }
    val ingredients = remember { mutableStateListOf<Ingredient>().apply { addAll(initialIngs) } }

    // Steps list
    val initialSteps = remember(initialRecipe) {
        if (initialRecipe != null) {
            converters.toStringList(initialRecipe.prepStepsJson)
        } else {
            listOf(
                "گوشت را به قطعات منظم و هم‌اندازه برش دهید.",
                "چاشنی‌ها و مواد مرینیت را کاملاً با گوشت مخلوط کرده و ماساژ دهید.",
                "در ظرف دربسته در یخچال بگذارید تا کاملاً طعم بگیرد."
            )
        }
    }
    val steps = remember { mutableStateListOf<String>().apply { addAll(initialSteps) } }

    var newIngName by remember { mutableStateOf("") }
    var newIngAmount by remember { mutableStateOf("") }
    var newIngUnit by remember { mutableStateOf("گرم") }

    var newStepText by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ObsidianBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "ویرایش دستور کارگاهی" else "ثبت دستور کارگاهی جدید",
                        fontWeight = FontWeight.Bold,
                        color = WarmCream,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("button_add_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = CopperFlame
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                Toast.makeText(context, "لطفاً نام دستور را وارد کنید", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val recipeToSave = if (initialRecipe != null) {
                                initialRecipe.copy(
                                    name = name.trim(),
                                    englishName = englishName.trim(),
                                    categoryFa = categoryFa,
                                    recommendedCut = if (recommendedCut.isBlank()) "برش انتخابی" else recommendedCut.trim(),
                                    flavorProfileFa = flavorProfileFa,
                                    spiceLevel = spiceLevel,
                                    shortDescription = if (shortDescription.isBlank()) "دستور تولید کارگاهی اختصاصی قصاب." else shortDescription.trim(),
                                    marinationTime = marinationTime,
                                    cookingMethod = cookingMethod,
                                    cookingTemp = cookingTemp,
                                    proTips = proTips.trim(),
                                    commonMistakes = commonMistakes.trim(),
                                    ingredientsJson = converters.fromIngredientsList(ingredients.toList()),
                                    prepStepsJson = converters.fromStringList(steps.toList())
                                )
                            } else {
                                RecipeEntity(
                                    id = "custom_" + UUID.randomUUID().toString().take(8),
                                    name = name.trim(),
                                    englishName = englishName.trim(),
                                    category = "My Recipes",
                                    categoryFa = categoryFa,
                                    shortDescription = if (shortDescription.isBlank()) "دستور تولید کارگاهی اختصاصی قصاب." else shortDescription.trim(),
                                    baseProtein = "Custom",
                                    baseProteinFa = "گوشت اختصاصی",
                                    recommendedCut = if (recommendedCut.isBlank()) "برش انتخابی" else recommendedCut.trim(),
                                    flavorProfile = "Custom",
                                    flavorProfileFa = flavorProfileFa,
                                    baseQuantityKg = 1.0,
                                    ingredientsJson = converters.fromIngredientsList(ingredients.toList()),
                                    prepStepsJson = converters.fromStringList(steps.toList()),
                                    marinationTime = marinationTime,
                                    cookingMethod = cookingMethod,
                                    cookingTemp = cookingTemp,
                                    spiceLevel = spiceLevel,
                                    recommendedUse = "عرضه در ویترین پروتئینی",
                                    storageNotes = "نگهداری در دمای ۱ تا ۴ درجه سانتی‌گراد",
                                    proTips = proTips.trim(),
                                    commonMistakes = commonMistakes.trim(),
                                    substitutions = "متناسب با سفارش مشتری قابل تغییر است.",
                                    isTraditional = false,
                                    isFusion = true,
                                    country = "ایران",
                                    region = "شخصی",
                                    isFavorite = true,
                                    isCustom = true,
                                    userNotes = ""
                                )
                            }
                            onSave(recipeToSave)
                            Toast.makeText(
                                context,
                                if (isEditing) "تغییرات دستور ذخیره شد" else "دستور جدید با موفقیت ذخیره شد",
                                Toast.LENGTH_SHORT
                            ).show()
                            onBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CopperFlame,
                            contentColor = ObsidianBlack
                        ),
                        modifier = Modifier.padding(end = 8.dp).testTag("button_submit_recipe")
                    ) {
                        Text(if (isEditing) "ذخیره تغییرات" else "ذخیره دستور", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CharcoalDark
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // General Info Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "مشخصات کلی محصول",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CopperFlame
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("نام دستور به فارسی *") },
                        placeholder = { Text("مثال: جوجه کباب انار و گردو مخصوص") },
                        modifier = Modifier.fillMaxWidth().testTag("input_recipe_name"),
                        colors = butcherTextFieldColors(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = englishName,
                        onValueChange = { englishName = it },
                        label = { Text("نام انگلیسی (اختیاری)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = butcherTextFieldColors(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = recommendedCut,
                        onValueChange = { recommendedCut = it },
                        label = { Text("برش پیشنهادی گوشت") },
                        placeholder = { Text("مثال: سینه مرغ بدون استخوان، راسته گوساله...") },
                        modifier = Modifier.fillMaxWidth().testTag("input_recipe_cut"),
                        colors = butcherTextFieldColors(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = shortDescription,
                        onValueChange = { shortDescription = it },
                        label = { Text("توضیحات کوتاه یا فرمول") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = butcherTextFieldColors(),
                        minLines = 2
                    )
                }
            }

            // Ingredients for 1kg Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مواد اولیه برای ۱ کیلوگرم پروتئین",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CopperFlame
                        )
                        Text(
                            text = "${ingredients.size} قلم",
                            fontSize = 12.sp,
                            color = SilverMuted
                        )
                    }

                    // Existing Ingredients
                    ingredients.forEachIndexed { idx, ing ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CharcoalDark)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${ing.name}: ${ing.amount.toInt()} ${ing.unit}",
                                fontSize = 13.sp,
                                color = WarmCream
                            )

                            if (idx > 0) { // Don't delete base protein
                                IconButton(
                                    onClick = { ingredients.removeAt(idx) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "حذف", tint = CrimsonBright, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Add Ingredient row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newIngName,
                            onValueChange = { newIngName = it },
                            placeholder = { Text("نام چاشنی", fontSize = 12.sp) },
                            modifier = Modifier.weight(1.5f).testTag("input_new_ing_name"),
                            colors = butcherTextFieldColors(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = newIngAmount,
                            onValueChange = { newIngAmount = it },
                            placeholder = { Text("مقدار", fontSize = 12.sp) },
                            modifier = Modifier.weight(0.8f).testTag("input_new_ing_amount"),
                            colors = butcherTextFieldColors(),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                val amount = newIngAmount.toDoubleOrNull() ?: 0.0
                                if (newIngName.isNotBlank() && amount > 0) {
                                    ingredients.add(Ingredient(newIngName.trim(), amount, newIngUnit))
                                    newIngName = ""
                                    newIngAmount = ""
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CopperFlame)
                                .testTag("button_add_ingredient")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "افزودن", tint = ObsidianBlack)
                        }
                    }
                }
            }

            // Steps Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "مراحل آماده‌سازی کارگاهی",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CopperFlame
                    )

                    steps.forEachIndexed { index, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CharcoalDark)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}. $step",
                                fontSize = 12.sp,
                                color = AntiqueParchment,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { steps.removeAt(index) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "حذف", tint = CrimsonBright, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newStepText,
                            onValueChange = { newStepText = it },
                            placeholder = { Text("شرح مرحله جدید...", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f).testTag("input_new_step"),
                            colors = butcherTextFieldColors(),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                if (newStepText.isNotBlank()) {
                                    steps.add(newStepText.trim())
                                    newStepText = ""
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CopperFlame)
                                .testTag("button_add_step")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "افزودن مرحله", tint = ObsidianBlack)
                        }
                    }
                }
            }

            // Pro Tips & Notes
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "نکات طلایی و پرهیزها",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CopperFlame
                    )

                    OutlinedTextField(
                        value = proTips,
                        onValueChange = { proTips = it },
                        label = { Text("نکته طلایی سرآشپز") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = butcherTextFieldColors(),
                        minLines = 2
                    )

                    OutlinedTextField(
                        value = commonMistakes,
                        onValueChange = { commonMistakes = it },
                        label = { Text("اشتباه رایج که باید پرهیز شود") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = butcherTextFieldColors(),
                        minLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun butcherTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = CharcoalDark,
    unfocusedContainerColor = CharcoalDark,
    focusedBorderColor = CopperFlame,
    unfocusedBorderColor = MetalBorder,
    focusedTextColor = WarmCream,
    unfocusedTextColor = WarmCream,
    focusedLabelColor = CopperLight,
    unfocusedLabelColor = SilverMuted
)
