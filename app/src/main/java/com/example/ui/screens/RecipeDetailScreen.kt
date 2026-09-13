package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.RecipeTypeConverters
import com.example.data.model.RecipeEntity
import com.example.ui.components.BatchScaleCalculator
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipe: RecipeEntity?,
    batchWeightKg: Double,
    onWeightChange: (Double) -> Unit,
    onToggleFavorite: (RecipeEntity) -> Unit,
    onSaveNotes: (String, String) -> Unit,
    onEdit: (RecipeEntity) -> Unit = {},
    onDelete: (RecipeEntity) -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (recipe == null) {
        Box(
            modifier = modifier.fillMaxSize().background(ObsidianBlack),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = CopperFlame)
        }
        return
    }

    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    val converters = remember { RecipeTypeConverters() }
    val ingredients = remember(recipe.ingredientsJson) {
        converters.toIngredientsList(recipe.ingredientsJson)
    }
    val prepSteps = remember(recipe.prepStepsJson) {
        converters.toStringList(recipe.prepStepsJson)
    }

    var userNotesText by remember(recipe.userNotes) { mutableStateOf(recipe.userNotes) }
    var checkedSteps by remember { mutableStateOf(setOf<Int>()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ObsidianBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = recipe.name,
                        maxLines = 1,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmCream
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("button_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = CopperFlame
                        )
                    }
                },
                actions = {
                    // Edit button
                    IconButton(
                        onClick = { onEdit(recipe) },
                        modifier = Modifier.testTag("button_detail_edit")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "ویرایش دستور",
                            tint = CopperLight
                        )
                    }

                    // Delete button (for custom recipes)
                    if (recipe.isCustom) {
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.testTag("button_detail_delete")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "حذف دستور",
                                tint = CrimsonBright
                            )
                        }
                    }

                    // Copy formula to clipboard
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val sb = StringBuilder()
                            sb.append("📋 فرمول تخصصی M.K.A IRON TASTE: ${recipe.name}\n")
                            sb.append("⚖️ وزن بچ: $batchWeightKg کیلوگرم\n\n")
                            sb.append("🛒 مواد اولیه:\n")
                            for (ing in ingredients) {
                                val scaled = ing.amount * (batchWeightKg / recipe.baseQuantityKg)
                                sb.append("• ${ing.name}: ${formatAmount(scaled, ing.unit)}\n")
                            }
                            sb.append("\n⏱️ زمان مرینیت: ${recipe.marinationTime}\n")
                            sb.append("🔥 روش پخت: ${recipe.cookingMethod} (${recipe.cookingTemp})\n")
                            clipboard.setPrimaryClip(ClipData.newPlainText("Recipe Formula", sb.toString()))
                            Toast.makeText(context, "فرمول با مقادیر مقیاس‌شده کپی شد", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("button_copy_formula")
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "کپی فرمول برای پرسنل",
                            tint = SilverMuted
                        )
                    }

                    // Favorite button
                    IconButton(
                        onClick = { onToggleFavorite(recipe) },
                        modifier = Modifier.testTag("button_detail_favorite")
                    ) {
                        Icon(
                            imageVector = if (recipe.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "نشان کردن",
                            tint = if (recipe.isFavorite) CopperFlame else SilverMuted
                        )
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
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(
                            text = "حذف دستور کارگاهی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonBright
                        )
                    },
                    text = {
                        Text(
                            text = "آیا از حذف دستور «${recipe.name}» اطمینان دارید؟ این عملیات قابل بازگشت نیست.",
                            color = WarmCream,
                            fontSize = 14.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDeleteDialog = false
                                onDelete(recipe)
                                onBack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CrimsonBright,
                                contentColor = WarmCream
                            ),
                            modifier = Modifier.testTag("button_confirm_delete")
                        ) {
                            Text("حذف دستور", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("انصراف", color = SilverMuted)
                        }
                    },
                    containerColor = MetalCard,
                    shape = RoundedCornerShape(16.dp)
                )
            }
            // Title and Category Header Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = BurgundyDeep,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonBright.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = recipe.categoryFa,
                                color = WarmCream,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (recipe.country.isNotBlank() && recipe.country != "ایران") {
                            Text(
                                text = "${recipe.country} • ${recipe.region}",
                                fontSize = 12.sp,
                                color = SilverMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = recipe.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = WarmCream
                    )

                    if (recipe.englishName.isNotBlank()) {
                        Text(
                            text = recipe.englishName,
                            fontSize = 14.sp,
                            color = CopperLight,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = recipe.shortDescription,
                        fontSize = 13.sp,
                        color = AntiqueParchment,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Spec Grid: Cut, Marination, Cooking, Spice
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CharcoalDark)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SpecItem(title = "برش گوشت", value = recipe.recommendedCut, icon = Icons.Default.Restaurant)
                        SpecItem(title = "استراحت", value = recipe.marinationTime, icon = Icons.Default.Schedule)
                        SpecItem(title = "تندی", value = recipe.spiceLevel, icon = Icons.Default.LocalFireDepartment)
                        SpecItem(title = "پخت", value = recipe.cookingMethod, icon = Icons.Default.OutdoorGrill)
                    }
                }
            }

            // Interactive Batch Scale Calculator
            BatchScaleCalculator(
                currentWeightKg = batchWeightKg,
                onWeightChange = onWeightChange
            )

            // Scaled Ingredients Section
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth().testTag("ingredients_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Scale,
                                contentDescription = null,
                                tint = CopperFlame
                            )
                            Text(
                                text = "مقادیر مواد برای $batchWeightKg کیلوگرم:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WarmCream
                            )
                        }

                        Text(
                            text = "${ingredients.size} قلم",
                            fontSize = 12.sp,
                            color = SilverMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ingredients.forEachIndexed { index, ing ->
                            val scaledAmount = ing.amount * (batchWeightKg / recipe.baseQuantityKg)
                            val isBaseProtein = index == 0

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isBaseProtein) BurgundyDeep.copy(alpha = 0.5f) else CharcoalDark)
                                    .border(
                                        1.dp,
                                        if (isBaseProtein) CrimsonBright.copy(alpha = 0.4f) else MetalBorder.copy(alpha = 0.5f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(if (isBaseProtein) CrimsonBright else CopperFlame)
                                    )
                                    Text(
                                        text = ing.name,
                                        fontSize = 13.sp,
                                        fontWeight = if (isBaseProtein) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isBaseProtein) WarmCream else AntiqueParchment
                                    )
                                }

                                Text(
                                    text = formatAmount(scaledAmount, ing.unit),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isBaseProtein) AmberGlow else CopperLight
                                )
                            }
                        }
                    }
                }
            }

            // Step-by-Step Preparation Checklist
            if (prepSteps.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MetalCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                    modifier = Modifier.fillMaxWidth().testTag("prep_steps_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Checklist,
                                contentDescription = null,
                                tint = CopperFlame
                            )
                            Text(
                                text = "دستورالعمل فرآوری کارگاهی",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WarmCream
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            prepSteps.forEachIndexed { idx, step ->
                                val isChecked = checkedSteps.contains(idx)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isChecked) MetalSurface.copy(alpha = 0.5f) else CharcoalDark)
                                        .clickable {
                                            checkedSteps = if (isChecked) {
                                                checkedSteps - idx
                                            } else {
                                                checkedSteps + idx
                                            }
                                        }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = {
                                            checkedSteps = if (isChecked) checkedSteps - idx else checkedSteps + idx
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = CopperFlame,
                                            checkmarkColor = ObsidianBlack,
                                            uncheckedColor = SilverMuted
                                        )
                                    )

                                    Column {
                                        Text(
                                            text = "مرحله ${idx + 1}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CopperLight
                                        )
                                        Text(
                                            text = step,
                                            fontSize = 13.sp,
                                            color = if (isChecked) SilverMuted else AntiqueParchment,
                                            lineHeight = 19.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Pro Tips & Common Mistakes Section
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Pro Tip
                    if (recipe.proTips.isNotBlank()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = AmberGlow, modifier = Modifier.size(20.dp))
                            Column {
                                Text("نکته طلایی سرآشپز قصابی:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberGlow)
                                Text(recipe.proTips, fontSize = 12.sp, color = WarmCream, lineHeight = 18.sp)
                            }
                        }
                    }

                    // Common Mistake
                    if (recipe.commonMistakes.isNotBlank()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = CrimsonBright, modifier = Modifier.size(20.dp))
                            Column {
                                Text("خطای رایج که باید پرهیز شود:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CrimsonBright)
                                Text(recipe.commonMistakes, fontSize = 12.sp, color = AntiqueParchment, lineHeight = 18.sp)
                            }
                        }
                    }

                    // Storage Notes
                    if (recipe.storageNotes.isNotBlank()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.AcUnit, contentDescription = null, tint = CopperLight, modifier = Modifier.size(20.dp))
                            Column {
                                Text("شرایط نگهداری در ویترین سرد:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CopperLight)
                                Text(recipe.storageNotes, fontSize = 12.sp, color = SilverMuted, lineHeight = 18.sp)
                            }
                        }
                    }
                }
            }

            // Personal Butcher Notes Section
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.EditNote,
                                contentDescription = null,
                                tint = CopperFlame
                            )
                            Text(
                                text = "یادداشت‌های شخصی قصاب",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WarmCream
                            )
                        }

                        Button(
                            onClick = {
                                onSaveNotes(recipe.id, userNotesText)
                                Toast.makeText(context, "یادداشت ذخیره شد", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BurgundyPrimary,
                                contentColor = WarmCream
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("button_save_notes")
                        ) {
                            Text("ذخیره", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = userNotesText,
                        onValueChange = { userNotesText = it },
                        modifier = Modifier.fillMaxWidth().testTag("notes_text_field"),
                        placeholder = {
                            Text(
                                "نکات خاص مشتریان، میزان سفارش‌های آخر هفته یا تغییرات اختصاصی را اینجا یادداشت کنید...",
                                fontSize = 12.sp,
                                color = SilverMuted
                            )
                        },
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CharcoalDark,
                            unfocusedContainerColor = CharcoalDark,
                            focusedBorderColor = CopperFlame,
                            unfocusedBorderColor = MetalBorder,
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SpecItem(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = CopperFlame, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(title, fontSize = 10.sp, color = SilverMuted)
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarmCream, maxLines = 1)
    }
}

private fun formatAmount(amount: Double, unit: String): String {
    return if (unit == "گرم" && amount >= 1000.0) {
        val kg = amount / 1000.0
        if (kg % 1.0 == 0.0) {
            "${kg.toInt()} کیلوگرم"
        } else {
            String.format(Locale.US, "%.2f کیلوگرم", kg)
        }
    } else if (unit == "میلی‌لیتر" && amount >= 1000.0) {
        val liters = amount / 1000.0
        if (liters % 1.0 == 0.0) {
            "${liters.toInt()} لیتر"
        } else {
            String.format(Locale.US, "%.2f لیتر", liters)
        }
    } else {
        if (amount % 1.0 == 0.0) {
            "${amount.toInt()} $unit"
        } else {
            String.format(Locale.US, "%.1f %s", amount, unit)
        }
    }
}
