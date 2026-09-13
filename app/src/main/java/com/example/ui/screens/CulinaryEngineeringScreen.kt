package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CulinaryEngineeringScreen(
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToRecipe: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("آزمایشگاه رسپی", "مهندسی مرینت", "نجات رسپی")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "آزمایشگاه و مهندسی طعم",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCopper
                        )
                        Text(
                            "M.K.A IRON TASTE • علم مرینیت و توسعه فرمولاسیون",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("engineering_back_button")
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = PrimaryCopper
                        )
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
                0 -> RecipeLabTab(viewModel = viewModel, onNavigateToRecipe = onNavigateToRecipe)
                1 -> MarinadeEngineeringTab()
                2 -> RecipeRescueTab()
            }
        }
    }
}

@Composable
private fun RecipeLabTab(
    viewModel: RecipeViewModel,
    onNavigateToRecipe: (String) -> Unit
) {
    val context = LocalContext.current
    val proteinOptions = listOf("سینه مرغ", "ران بی‌استخوان مرغ", "فیله گوساله", "راسته گوساله", "راسته گوسفندی", "میگو", "ماهی", "گوشت چرخ‌کرده")
    val availableIngredientSuggestions = listOf(
        "ماست چکیده", "زعفران", "آب لیمو ترش", "سیر تازه", "آب پیاز", "روغن زیتون",
        "سس خردل دیژون", "فلفل سیاه نیم‌کوب", "پاپریکا دودی", "سویا سس", "رزماری تازه",
        "کره حیوانی", "رب انار", "سرکه سیب", "سماق", "پودر زیره", "عسل", "سس مایونز"
    )

    var selectedProtein by remember { mutableStateOf(proteinOptions[0]) }
    val selectedIngredients = remember { mutableStateListOf("ماست چکیده", "سیر تازه", "آب لیمو ترش", "روغن زیتون") }
    var customIngredientInput by remember { mutableStateOf("") }
    var generatedConcepts by remember { mutableStateOf<List<GeneratedRecipeConcept>>(emptyList()) }

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
                    Text(
                        "🔬 موتور طراحی و فرمولاسیون رسپی کارگاهی",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryCopper
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "پروتئین و اقلام در دسترس را انتخاب کنید تا الگوریتم هوشمند M.K.A فرمول‌های تخصصی با مقادیر دقیق بر مبنای ۱ کیلوگرم گوشت و دستور آماده‌سازی گام‌به‌گام را به صورت کاملاً آفلاین تولید نماید.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // Protein Selection
        item {
            Text(
                "۱. انتخاب پروتئین پایه:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryCopper
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(proteinOptions) { protein ->
                    FilterChip(
                        selected = selectedProtein == protein,
                        onClick = { selectedProtein = protein },
                        label = { Text(protein) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryCopper,
                            selectedLabelColor = DarkBackground,
                            containerColor = DarkSurface,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedProtein == protein,
                            borderColor = if (selectedProtein == protein) PrimaryCopper else DarkBorder
                        )
                    )
                }
            }
        }

        // Ingredients Selection
        item {
            Text(
                "۲. اقلام و چاشنی‌های در دسترس شما:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryCopper
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = customIngredientInput,
                    onValueChange = { customIngredientInput = it },
                    placeholder = { Text("افزودن چاشنی دلخواه...", color = TextMuted, fontSize = 12.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedBorderColor = PrimaryCopper,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("custom_ingredient_input")
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (customIngredientInput.isNotBlank()) {
                            if (!selectedIngredients.contains(customIngredientInput.trim())) {
                                selectedIngredients.add(customIngredientInput.trim())
                            }
                            customIngredientInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryCopper),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("افزودن", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Suggestions flow
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("پیشنهادات پرکاربرد (لمس برای انتخاب/حذف):", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                val chunked = availableIngredientSuggestions.chunked(3)
                chunked.forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        rowItems.forEach { ing ->
                            val isSelected = selectedIngredients.contains(ing)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) PrimaryCopper.copy(alpha = 0.2f) else DarkSurface,
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = Brush.horizontalGradient(
                                        listOf(if (isSelected) PrimaryCopper else DarkBorder, if (isSelected) FlameAccent else DarkBorder)
                                    )
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (isSelected) selectedIngredients.remove(ing)
                                        else selectedIngredients.add(ing)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryCopper, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        ing,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) PrimaryCopper else TextPrimary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Generate Action Button
        item {
            Button(
                onClick = {
                    generatedConcepts = CulinaryEngineeringData.generateRecipeConcepts(
                        protein = selectedProtein,
                        selectedIngredients = selectedIngredients.toList()
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryCopper),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("generate_concepts_button")
            ) {
                Icon(Icons.Default.Science, contentDescription = null, tint = DarkBackground)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "تولید فرمولاسیون‌های تخصصی M.K.A",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkBackground
                )
            }
        }

        // Display Generated Concepts
        if (generatedConcepts.isNotEmpty()) {
            item {
                Text(
                    "فرمول‌های مهندسی‌شده پیشنهادی:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCopper
                )
            }

            items(generatedConcepts, key = { it.id }) { concept ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(DarkBorder, PrimaryCopper.copy(alpha = 0.3f)))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            concept.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCopper
                        )
                        Text(
                            concept.englishTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "پروفایل طعمی: ${concept.flavorProfileFa}",
                            style = MaterialTheme.typography.labelSmall,
                            color = FlameAccent
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = DarkBorder)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("مقادیر دقیق برای ۱ کیلوگرم گوشت:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                        concept.ingredients.forEach { ing ->
                            Text(
                                "• ${ing.name}: ${ing.amount.toInt()} ${ing.unit}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("ترتیب آماده‌سازی کارگاهی:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                        concept.prepSequence.forEach { step ->
                            Text(
                                step,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("زمان استراحت: ${concept.marinationTime} • پخت: ${concept.cookingMethod}", style = MaterialTheme.typography.labelSmall, color = TextMuted)

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.saveConceptAsPersonalRecipe(concept) { newId ->
                                    Toast.makeText(context, "در «رسپی‌های من» با موفقیت ذخیره شد!", Toast.LENGTH_SHORT).show()
                                    onNavigateToRecipe(newId)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCopper.copy(alpha = 0.2f)),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryCopper, FlameAccent))),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = PrimaryCopper)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ذخیره مستقیم در رسپی‌های من", color = PrimaryCopper, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MarinadeEngineeringTab() {
    var selectedPillar by remember { mutableStateOf<MarinadePillar?>(null) }
    var selectedDirection by remember { mutableStateOf(CulinaryEngineeringData.flavorTunings[0]) }

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
                    Text(
                        "🏛️ هشت ستون علمی مرینیت (8 Pillars of Marinade)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryCopper
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "هر مرینیت موفق در قصابی و رستوران بر تعامل دقیق این ۸ رکن استوار است: پروتئین، نمک، اسید، چربی، ادویه، آروماتیک، شیرینی و مایعات ناقل.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        item {
            Text("ستون‌های هشت‌گانه (برای جزئیات لمس کنید):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, color = PrimaryCopper)
        }

        items(CulinaryEngineeringData.marinadePillars, key = { it.id }) { pillar ->
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(10.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(DarkBorder, PrimaryCopper.copy(alpha = 0.2f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedPillar = pillar }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(pillar.nameFa, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(pillar.nameEn, style = MaterialTheme.typography.labelSmall, color = PrimaryCopper)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("نسبت بهینه: ${pillar.optimalRatio}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = PrimaryCopper)
                }
            }
        }

        // Flavor Direction Tuner Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryCopper, FlameAccent))),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "🎛️ تنظیم‌کننده جهت طعمی مرینیت (Flavor Direction Tuner)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryCopper
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(CulinaryEngineeringData.flavorTunings) { tuning ->
                            FilterChip(
                                selected = selectedDirection.directionId == tuning.directionId,
                                onClick = { selectedDirection = tuning },
                                label = { Text(tuning.nameFa.substringBefore(" (")) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryCopper,
                                    selectedLabelColor = DarkBackground,
                                    containerColor = DarkSurfaceVariant,
                                    labelColor = TextPrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(selectedDirection.nameFa, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = FlameAccent)
                    Text(selectedDirection.description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)

                    Spacer(modifier = Modifier.height(10.dp))
                    TuningItem("تنظیم نمک", selectedDirection.saltAdjustment)
                    TuningItem("تنظیم اسید", selectedDirection.acidAdjustment)
                    TuningItem("تنظیم روغن و چربی", selectedDirection.fatAdjustment)
                    TuningItem("تنظیم ادویه و فلفل", selectedDirection.spiceAdjustment)
                    TuningItem("آروماتیک و سبزیجات", selectedDirection.aromaticsAdjustment)
                    TuningItem("قند و کاراملیزاسیون", selectedDirection.sweetnessAdjustment)
                    TuningItem("قانون طلایی سرآشپز", selectedDirection.proChefRule)
                }
            }
        }
    }

    selectedPillar?.let { pillar ->
        Dialog(onDismissRequest = { selectedPillar = null }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(PrimaryCopper, DarkBorder))),
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Surface(color = DarkSurfaceVariant, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(pillar.nameFa, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(pillar.nameEn, style = MaterialTheme.typography.bodySmall, color = PrimaryCopper)
                            }
                            IconButton(onClick = { selectedPillar = null }) {
                                Icon(Icons.Default.Close, contentDescription = "بستن", tint = TextMuted)
                            }
                        }
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item { DetailBox("نقش علمی و بیوشیمیایی", pillar.scientificRole) }
                        item { DetailBox("نسبت بهینه کارگاهی", pillar.optimalRatio) }
                        item { DetailBox("تأثیر بر بافت گوشت", pillar.impactOnTexture) }
                        item { DetailBox("تأثیر بر ادراک طعم", pillar.impactOnFlavor) }
                        item { DetailBox("اشتباهات رایج قصابان و آشپزان", pillar.commonMistakes) }
                        item { DetailBox("توصیه حرفه‌ای سرآشپز", pillar.proGuidance) }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeRescueTab() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSub by remember { mutableStateOf<IngredientSubstitution?>(null) }

    val filteredSubs = remember(searchQuery) {
        if (searchQuery.isBlank()) CulinaryEngineeringData.substitutions
        else CulinaryEngineeringData.substitutions.filter {
            it.missingIngredient.contains(searchQuery, ignoreCase = true) ||
            it.missingIngredientEn.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("کدام ماده را در آشپزخانه ندارید؟ (آبلیمو، زعفران، سویا سس...)", color = TextMuted, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryCopper) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface,
                focusedBorderColor = PrimaryCopper,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("rescue_search_input")
        )

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredSubs, key = { it.missingIngredient }) { sub ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(DarkBorder, PrimaryCopper.copy(alpha = 0.2f)))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedSub = sub }
                        .testTag("rescue_item_${sub.missingIngredient}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "ماده ناموجود: ${sub.missingIngredient}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = FlameAccent
                            )
                            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = PrimaryCopper)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "جایگزین اول: ${sub.primarySubstitute}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryCopper
                        )
                        Text(
                            "نسبت جایگزینی: ${sub.ratioGuidance}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }

    selectedSub?.let { sub ->
        Dialog(onDismissRequest = { selectedSub = null }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(PrimaryCopper, DarkBorder))),
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Surface(color = DarkSurfaceVariant, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("جایگزین برای: ${sub.missingIngredient}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = FlameAccent)
                                Text(sub.missingIngredientEn, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                            }
                            IconButton(onClick = { selectedSub = null }) {
                                Icon(Icons.Default.Close, contentDescription = "بستن", tint = TextMuted)
                            }
                        }
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item { DetailBox("جایگزین اصلی درجه یک", sub.primarySubstitute) }
                        item { DetailBox("جایگزین دوم", sub.secondarySubstitute) }
                        item { DetailBox("راهنمای نسبت دقیق", sub.ratioGuidance) }
                        item { DetailBox("تفاوت طعمی در غذای نهایی", sub.flavorDifference) }
                        item { DetailBox("تفاوت در بافت گوشت", sub.textureDifference) }
                        item { DetailBox("زمان و نحوه افزودن در مرینیت", sub.additionPoint) }
                        item { DetailBox("ملاحظات ایمنی و بهداشتی", sub.safeCulinaryGuardrails) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TuningItem(title: String, desc: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PrimaryCopper)
        Text(desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}

@Composable
private fun DetailBox(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Text(title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = PrimaryCopper)
        Spacer(modifier = Modifier.height(3.dp))
        Text(content, style = MaterialTheme.typography.bodySmall, color = TextSecondary, lineHeight = 19.sp)
    }
}
