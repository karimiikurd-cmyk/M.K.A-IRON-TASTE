package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeatAtlasScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedPrimalCut by remember { mutableStateOf<PrimalCut?>(null) }
    var selectedSteakCut by remember { mutableStateOf<SteakCutGuide?>(null) }
    var selectedChickenGuide by remember { mutableStateOf<ChickenCuttingGuide?>(null) }
    var steakSearchQuery by remember { mutableStateOf("") }

    val tabs = listOf("اطلس لاشه گوساله", "۲۱ استیک تخصصی", "برش و فیله‌کاری مرغ")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "اطلس گوشت و استیک",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCopper
                        )
                        Text(
                            "M.K.A IRON TASTE • مرجع آناتومی و قصابی",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("atlas_back_button")
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
            // Tab Selector Row
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
                0 -> PrimalCutsListTab(onSelectPrimal = { selectedPrimalCut = it })
                1 -> SteakGuidesTab(
                    searchQuery = steakSearchQuery,
                    onSearchQueryChange = { steakSearchQuery = it },
                    onSelectSteak = { selectedSteakCut = it }
                )
                2 -> ChickenGuidesTab(onSelectChicken = { selectedChickenGuide = it })
            }
        }
    }

    // Detail Dialogs
    selectedPrimalCut?.let { primal ->
        PrimalCutDetailDialog(primal = primal, onDismiss = { selectedPrimalCut = null })
    }

    selectedSteakCut?.let { steak ->
        SteakCutDetailDialog(steak = steak, onDismiss = { selectedSteakCut = null })
    }

    selectedChickenGuide?.let { chicken ->
        ChickenGuideDetailDialog(chicken = chicken, onDismiss = { selectedChickenGuide = null })
    }
}

@Composable
private fun PrimalCutsListTab(onSelectPrimal: (PrimalCut) -> Unit) {
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryCopper)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "سلسله مراتب آناتومی لاشه گوساله",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCopper
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "لاشه کامل (Whole Carcass) ➔ قطعه اصلی (Primal Cut) ➔ قطعه فرعی (Sub-Primal) ➔ عضلات تفکیکی (Muscles) ➔ برش استیک یا کباب (Steak / Cut) ➔ سهم مصرفی (Portion)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        items(MeatAtlasData.primalCuts, key = { it.id }) { primal ->
            PrimalCutCard(primal = primal, onClick = { onSelectPrimal(primal) })
        }
    }
}

@Composable
private fun PrimalCutCard(primal: PrimalCut, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(DarkBorder, PrimaryCopper.copy(alpha = 0.3f)))),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("primal_card_${primal.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        primal.persianName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        primal.englishName,
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryCopper
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = FlameAccent.copy(alpha = 0.15f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FlameAccent, PrimaryCopper)))
                ) {
                    Text(
                        "نرمی: ${primal.tendernessScore}/۱۰",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FlameAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "موقعیت: ${primal.carcassLocation}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                primal.steakCutsObtainable.take(3).forEach { cutName ->
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = DarkSurfaceVariant
                    ) {
                        Text(
                            cutName,
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryCopper,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SteakGuidesTab(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSelectSteak: (SteakCutGuide) -> Unit
) {
    val filteredSteaks = remember(searchQuery) {
        if (searchQuery.isBlank()) MeatAtlasData.steakCuts
        else MeatAtlasData.steakCuts.filter {
            it.nameFa.contains(searchQuery, ignoreCase = true) ||
            it.nameEn.contains(searchQuery, ignoreCase = true) ||
            it.primalOrigin.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("جستجو میان ۲۱ استیک تخصصی (ریب‌آی، توماهاوک، پیکانیا...)", color = TextMuted, fontSize = 13.sp) },
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
                .testTag("steak_search_input")
        )

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredSteaks, key = { it.id }) { steak ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(DarkBorder, PrimaryCopper.copy(alpha = 0.2f)))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectSteak(steak) }
                        .testTag("steak_item_${steak.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                steak.nameFa,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                steak.nameEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryCopper
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "منشأ: ${steak.primalOrigin} • ضخامت: ${steak.recommendedThickness}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = "مشاهده",
                            tint = PrimaryCopper
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChickenGuidesTab(onSelectChicken: (ChickenCuttingGuide) -> Unit) {
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "راهنمای جامع آماده‌سازی و فیله‌کاری استیک‌های مرغ",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryCopper
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "تکنیک‌های استخوان‌گیری، یکنواخت‌سازی ضخامت، برش پروانه‌ای و پیشگیری از پارگی بافت جهت عرضه در ویترین قصابی و گریل رستورانی.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        items(MeatAtlasData.chickenGuides, key = { it.id }) { chicken ->
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(DarkBorder, PrimaryCopper.copy(alpha = 0.25f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectChicken(chicken) }
                    .testTag("chicken_item_${chicken.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            chicken.nameFa,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null, tint = PrimaryCopper)
                    }
                    Text(
                        chicken.nameEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryCopper
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "بخش دقیق: ${chicken.exactPart}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun PrimalCutDetailDialog(primal: PrimalCut, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(PrimaryCopper, DarkBorder))),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Surface(
                    color = DarkSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                primal.persianName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                primal.englishName,
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryCopper
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن", tint = TextMuted)
                        }
                    }
                }

                // Scrollable Content
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item { DetailField("موقعیت در لاشه", primal.carcassLocation) }
                    item { DetailField("عضلات تفکیکی اصلی", primal.primaryMuscles.joinToString("\n• ", prefix = "• ")) }
                    item { DetailField("قطعات فرعی (Sub-Primals)", primal.subPrimals.joinToString(" • ")) }
                    item { DetailField("وضعیت استخوان", primal.boneOptions) }
                    item { DetailField("ویژگی چربی و ماربلینگ", primal.fatCharacteristics) }
                    item { DetailField("بافت پیوندی و کلاژن", primal.connectiveTissue) }
                    item { DetailField("نرمی و بافت (${primal.tendernessScore}/۱۰)", primal.tendernessDescription) }
                    item { DetailField("کاربردهای کباب ایرانی", primal.kebabApplications) }
                    item { DetailField("کاربرد در برگر و چرخ‌کرده", primal.groundMeatApplications) }
                    item { DetailField("پخت آهسته و خورش", primal.slowCookingApplications) }
                    item { DetailField("نکات تریم و قصابی", primal.trimmingConsiderations) }
                    item { DetailField("تفاوت سیستم قصابی ایران و بین‌الملل", primal.butcherySystemDifferences) }
                }
            }
        }
    }
}

@Composable
private fun SteakCutDetailDialog(steak: SteakCutGuide, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(PrimaryCopper, DarkBorder))),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(
                    color = DarkSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                steak.nameFa,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                steak.nameEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryCopper
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن", tint = TextMuted)
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item { DetailField("منشأ و قطعه اصلی", "${steak.primalOrigin} (${steak.carcassLocation})") }
                    item { DetailField("عضلات تشکیل‌دهنده", steak.muscles) }
                    item { DetailField("چگونه قطعه خام را شناسایی کنیم؟", steak.identificationGuide) }
                    item { DetailField("نوع استخوان", steak.boneType) }
                    item { DetailField("پاک‌سازی بافت‌های ناخواسته", steak.unwantedTissueRemoval) }
                    item { DetailField("تنظیم ضخامت چربی باقی‌مانده", steak.fatRemainingGuide) }
                    item { DetailField("حذف سیلوراسکین و بافت همبند", steak.connectiveTissueRemoval) }
                    item { DetailField("جهت الیاف و نحوه برش", "امتداد: ${steak.fiberDirection}\nدستور برش: ${steak.cuttingDirection}") }
                    item { DetailField("ضخامت و وزن استاندارد", "ضخامت: ${steak.recommendedThickness} • وزن: ${steak.recommendedPortionWeight}") }
                    item { DetailField("فرم‌دهی نهایی استیک", steak.finalShapingGuide) }
                    item { DetailField("نگهداری و بیات‌سازی (Aging)", steak.storageAndAging) }
                    item { DetailField("بهترین روش‌های پخت", steak.bestCookingMethods) }
                    item { DetailField("نکات طلایی سرآشپز M.K.A", steak.proTips) }
                }
            }
        }
    }
}

@Composable
private fun ChickenGuideDetailDialog(chicken: ChickenCuttingGuide, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(PrimaryCopper, DarkBorder))),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(
                    color = DarkSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                chicken.nameFa,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                chicken.nameEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryCopper
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "بستن", tint = TextMuted)
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item { DetailField("بخش دقیق آناتومی", chicken.exactPart) }
                    item { DetailField("تکنیک استخوان‌گیری", chicken.boneRemoval) }
                    item { DetailField("برداشتن بافت همبند و تاندون", chicken.connectiveTissueRemoval) }
                    item { DetailField("تکنیک برش پروانه‌ای", chicken.butterflyingTechnique) }
                    item { DetailField("یکنواخت‌سازی ضخامت", chicken.thicknessAndUniformity) }
                    item { DetailField("آماده‌سازی برای مرینیت", chicken.marinadePrep) }
                    item { DetailField("روش پیشگیری از پارگی بافت", chicken.preventingTearing) }
                    item { DetailField("روش‌های پخت پیشنهادی", chicken.cookingMethods) }
                    item { DetailField("نکته حرفه‌ای سرآشپز", chicken.proTips) }
                }
            }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryCopper
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 19.sp
        )
    }
}
