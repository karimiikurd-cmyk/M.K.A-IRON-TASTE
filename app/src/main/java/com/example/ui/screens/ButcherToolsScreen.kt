package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButcherToolsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("محاسبه‌گر تولید و اوزان", "افت و راندمان قصابی", "مهندسی شوری و نمک")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ObsidianBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "M.K.A IRON TASTE",
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = WarmCream,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "محاسبه‌گرهای تخصصی قصابی و کارگاهی",
                            fontSize = 11.sp,
                            color = CopperLight
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("button_tools_back")) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = CopperGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IronDark)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = IronDark,
                contentColor = CopperGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CopperGold,
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
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) CopperGold else SteelMuted
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> AdvancedWeightCalculatorTab()
                1 -> ButcheryYieldCalculatorTab()
                2 -> SaltAndBrineCalculatorTab()
            }
        }
    }
}

@Composable
private fun AdvancedWeightCalculatorTab() {
    var calculationMode by remember { mutableIntStateOf(0) } // 0: By Weight, 1: By Skewers, 2: By Burgers, 3: By Steaks, 4: By Portions
    var inputWeightKg by remember { mutableStateOf("10") }
    var inputCount by remember { mutableStateOf("50") }

    // Units / Weights
    val skewerWeights = listOf(80, 100, 120, 150, 200, 250)
    var selectedSkewerGrams by remember { mutableIntStateOf(150) }

    val burgerWeights = listOf(130, 150, 180, 200, 220)
    var selectedBurgerGrams by remember { mutableIntStateOf(150) }

    val steakWeights = listOf(250, 300, 350, 450)
    var selectedSteakGrams by remember { mutableIntStateOf(300) }

    val portionGrams = 200 // standard meal portion

    val totalWeightKg = remember(calculationMode, inputWeightKg, inputCount, selectedSkewerGrams, selectedBurgerGrams, selectedSteakGrams) {
        when (calculationMode) {
            0 -> inputWeightKg.toDoubleOrNull() ?: 0.0
            1 -> ((inputCount.toDoubleOrNull() ?: 0.0) * selectedSkewerGrams) / 1000.0
            2 -> ((inputCount.toDoubleOrNull() ?: 0.0) * selectedBurgerGrams) / 1000.0
            3 -> ((inputCount.toDoubleOrNull() ?: 0.0) * selectedSteakGrams) / 1000.0
            else -> ((inputCount.toDoubleOrNull() ?: 0.0) * portionGrams) / 1000.0
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Mode Selector Chips
        item {
            Text("حالت محاسبه مقیاس تولید:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = CopperLight)
            Spacer(modifier = Modifier.height(6.dp))
            val modes = listOf("بر مبنای وزن (کیلوگرم)", "تعداد سیخ کباب", "تعداد برگر", "تعداد استیک", "تعداد پرس غذا")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(modes.indices.toList()) { index ->
                    FilterChip(
                        selected = calculationMode == index,
                        onClick = { calculationMode = index },
                        label = { Text(modes[index], fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CopperGold,
                            selectedLabelColor = ObsidianBlack,
                            containerColor = IronDark,
                            labelColor = TextLight
                        )
                    )
                }
            }
        }

        // Quick Batch Buttons for Weight
        if (calculationMode == 0) {
            item {
                Text("مقادیر سریع کارگاهی:", style = MaterialTheme.typography.labelSmall, color = SteelMuted)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf("1", "5", "10", "20", "50", "100").forEach { preset ->
                        OutlinedButton(
                            onClick = { inputWeightKg = preset },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (inputWeightKg == preset) CopperGold.copy(alpha = 0.2f) else Color.Transparent
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.horizontalGradient(listOf(if (inputWeightKg == preset) CopperGold else SteelBorder, IronDark))
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("${preset}kg", fontSize = 11.sp, color = if (inputWeightKg == preset) CopperGold else TextLight)
                        }
                    }
                }
            }
        }

        // Custom Inputs
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IronDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SteelBorder, CopperGold.copy(alpha = 0.3f)))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (calculationMode == 0) {
                        OutlinedTextField(
                            value = inputWeightKg,
                            onValueChange = { inputWeightKg = it },
                            label = { Text("وزن کل گوشت خالص (کیلوگرم)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = WarmCream,
                                unfocusedTextColor = WarmCream,
                                focusedBorderColor = CopperGold,
                                unfocusedBorderColor = SteelBorder
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("input_batch_kg")
                        )
                    } else {
                        val countLabel = when (calculationMode) {
                            1 -> "تعداد سیخ مورد نیاز"
                            2 -> "تعداد برگر مورد نیاز"
                            3 -> "تعداد استیک مورد نیاز"
                            else -> "تعداد پرس غذا"
                        }
                        OutlinedTextField(
                            value = inputCount,
                            onValueChange = { inputCount = it },
                            label = { Text(countLabel) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = WarmCream,
                                unfocusedTextColor = WarmCream,
                                focusedBorderColor = CopperGold,
                                unfocusedBorderColor = SteelBorder
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("input_batch_count")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        when (calculationMode) {
                            1 -> {
                                Text("وزن استاندارد هر سیخ (گرم):", style = MaterialTheme.typography.labelSmall, color = CopperLight)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    skewerWeights.forEach { grams ->
                                        FilterChip(
                                            selected = selectedSkewerGrams == grams,
                                            onClick = { selectedSkewerGrams = grams },
                                            label = { Text("${grams}g", fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = CopperGold,
                                                selectedLabelColor = ObsidianBlack,
                                                containerColor = ObsidianBlack
                                            )
                                        )
                                    }
                                }
                            }
                            2 -> {
                                Text("وزن هر عدد برگر خام (گرم):", style = MaterialTheme.typography.labelSmall, color = CopperLight)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    burgerWeights.forEach { grams ->
                                        FilterChip(
                                            selected = selectedBurgerGrams == grams,
                                            onClick = { selectedBurgerGrams = grams },
                                            label = { Text("${grams}g", fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = CopperGold,
                                                selectedLabelColor = ObsidianBlack,
                                                containerColor = ObsidianBlack
                                            )
                                        )
                                    }
                                }
                            }
                            3 -> {
                                Text("وزن هر استیک خام (گرم):", style = MaterialTheme.typography.labelSmall, color = CopperLight)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    steakWeights.forEach { grams ->
                                        FilterChip(
                                            selected = selectedSteakGrams == grams,
                                            onClick = { selectedSteakGrams = grams },
                                            label = { Text("${grams}g", fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = CopperGold,
                                                selectedLabelColor = ObsidianBlack,
                                                containerColor = ObsidianBlack
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Calculated Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IronDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FlameAccent, CopperGold))),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "خروجی محاسبات مقیاس کارگاهی",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CopperGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("وزن کل گوشت خالص لازم:", color = TextLight, fontSize = 13.sp)
                        Text(
                            String.format(Locale.US, "%.2f کیلوگرم (%d گرم)", totalWeightKg, (totalWeightKg * 1000).toInt()),
                            fontWeight = FontWeight.Bold,
                            color = CopperGold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("نمک استاندارد مرینیت (۱.۴٪):", color = TextLight, fontSize = 13.sp)
                        Text(
                            String.format(Locale.US, "%.1f گرم", (totalWeightKg * 1000.0) * 0.014),
                            fontWeight = FontWeight.Bold,
                            color = WarmCream,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("روغن محافظتی پایه (۵٪):", color = TextLight, fontSize = 13.sp)
                        Text(
                            String.format(Locale.US, "%.1f گرم", (totalWeightKg * 1000.0) * 0.05),
                            fontWeight = FontWeight.Bold,
                            color = WarmCream,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("آب پیاز صاف‌شده بدون تفاله (۱۰٪):", color = TextLight, fontSize = 13.sp)
                        Text(
                            String.format(Locale.US, "%.1f گرم", (totalWeightKg * 1000.0) * 0.10),
                            fontWeight = FontWeight.Bold,
                            color = WarmCream,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ButcheryYieldCalculatorTab() {
    var rawWeightInput by remember { mutableStateOf("10.0") }
    var trimmedWeightInput by remember { mutableStateOf("7.2") }
    var purchasePriceInput by remember { mutableStateOf("600000") }

    val rawWeight = rawWeightInput.toDoubleOrNull() ?: 0.0
    val trimmedWeight = trimmedWeightInput.toDoubleOrNull() ?: 0.0
    val purchasePrice = purchasePriceInput.toDoubleOrNull() ?: 0.0

    val trimLossKg = (rawWeight - trimmedWeight).coerceAtLeast(0.0)
    val wastePercent = if (rawWeight > 0) (trimLossKg / rawWeight) * 100.0 else 0.0
    val usableYieldPercent = if (rawWeight > 0) (trimmedWeight / rawWeight) * 100.0 else 0.0
    val costPerUsableKg = if (trimmedWeight > 0) (rawWeight * purchasePrice) / trimmedWeight else 0.0

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IronDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CopperGold, FlameAccent))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "⚖️ محاسبه‌گر افت و راندمان قصابی (Trim Loss & Yield)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CopperGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "محاسبه دقیق افت استخوان، رگ، چربی و بافت‌های دورریز، درصد راندمان مفید و قیمت تمام‌شده هر کیلو گوشت خالص پاک‌شده.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IronDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SteelBorder, CopperGold.copy(alpha = 0.3f)))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = rawWeightInput,
                        onValueChange = { rawWeightInput = it },
                        label = { Text("وزن اولیه ناخالص گوشت با استخوان/چربی (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream,
                            focusedBorderColor = CopperGold,
                            unfocusedBorderColor = SteelBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_raw_weight")
                    )

                    OutlinedTextField(
                        value = trimmedWeightInput,
                        onValueChange = { trimmedWeightInput = it },
                        label = { Text("وزن گوشت خالص پاک‌شده مفید (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream,
                            focusedBorderColor = CopperGold,
                            unfocusedBorderColor = SteelBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_trimmed_weight")
                    )

                    OutlinedTextField(
                        value = purchasePriceInput,
                        onValueChange = { purchasePriceInput = it },
                        label = { Text("قیمت خرید هر کیلوگرم ناخالص (تومان)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream,
                            focusedBorderColor = CopperGold,
                            unfocusedBorderColor = SteelBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_purchase_price")
                    )
                }
            }
        }

        // Result Grid
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ObsidianBlack),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FlameAccent, CopperGold))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("نتایج اقتصادی و راندمان قصابی:", fontWeight = FontWeight.Bold, color = CopperGold, style = MaterialTheme.typography.titleSmall)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("وزن افت و دورریز (Trim Loss):", color = TextLight, fontSize = 13.sp)
                        Text(String.format(Locale.US, "%.2f کیلوگرم", trimLossKg), fontWeight = FontWeight.Bold, color = FlameAccent)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("درصد افت (Waste %):", color = TextLight, fontSize = 13.sp)
                        Text(String.format(Locale.US, "%.1f %%", wastePercent), fontWeight = FontWeight.Bold, color = FlameAccent)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("درصد راندمان گوشت خالص (Yield %):", color = TextLight, fontSize = 13.sp)
                        Text(String.format(Locale.US, "%.1f %%", usableYieldPercent), fontWeight = FontWeight.Bold, color = CopperGold)
                    }

                    Divider(color = SteelBorder)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("بهای تمام‌شده هر کیلو گوشت خالص:", color = WarmCream, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(String.format(Locale.US, "%,d تومان", costPerUsableKg.toLong()), fontWeight = FontWeight.Black, color = CopperGold, fontSize = 15.sp)
                    }
                }
            }
        }

        // Benchmark Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IronDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SteelBorder, CopperGold.copy(alpha = 0.2f)))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("استانداردهای راندمان مرجع قصابی (Industry Benchmarks):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = CopperLight)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• سردست با استخوان به گوشت بی‌استخوان: ۷۰٪ تا ۷۴٪ راندمان", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text("• راسته با استخوان به استریپ‌لوین بی‌استخوان: ۶۵٪ تا ۶۸٪ راندمان", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text("• دنده کامل به استیک ریب‌آی بی‌استخوان: ۵۵٪ تا ۵۸٪ راندمان", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text("• فیله کامل با چربی و زنجیره به فیله مینیون خالص: ۷۵٪ تا ۸۰٪ راندمان", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text("• مرغ کامل به سینه و ران بی‌استخوان: ۶۴٪ تا ۶۷٪ راندمان", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun SaltAndBrineCalculatorTab() {
    var meatWeightInput by remember { mutableStateOf("5") }
    var selectedSaltPercent by remember { mutableStateOf(1.4) }

    val meatWeightKg = meatWeightInput.toDoubleOrNull() ?: 0.0
    val calculatedSaltGrams = (meatWeightKg * 1000.0) * (selectedSaltPercent / 100.0)

    val saltOptions = listOf(
        1.0 to "بسیار کم‌نمک (رژیمی)",
        1.2 to "ملایم خانگی",
        1.4 to "استاندارد طلایی رستورانی",
        1.6 to "پرنمک باربیکیو",
        1.8 to "تخصصی کوبیده و قصابی",
        2.0 to "حداکثر سوسیس و کالباس"
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IronDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CopperGold, FlameAccent))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "🧂 مهندسی نمک و درای براینینگ (Dry Brining)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CopperGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "فرمول دقیق انحلال میوزین و نگهداری آب در بافت پروتئین بر اساس استانداردهای آشپزی حرفه‌ای.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IronDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SteelBorder, CopperGold.copy(alpha = 0.3f)))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = meatWeightInput,
                        onValueChange = { meatWeightInput = it },
                        label = { Text("وزن گوشت (کیلوگرم)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream,
                            focusedBorderColor = CopperGold,
                            unfocusedBorderColor = SteelBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_salt_meat_weight")
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("درصد نمک مورد نظر بر مبنای وزن پروتئین:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, color = CopperLight)
                    Spacer(modifier = Modifier.height(6.dp))

                    saltOptions.forEach { (percent, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedSaltPercent = percent }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedSaltPercent == percent,
                                onClick = { selectedSaltPercent = percent },
                                colors = RadioButtonDefaults.colors(selectedColor = CopperGold, unselectedColor = SteelBorder)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("$percent% — $label", style = MaterialTheme.typography.bodySmall, color = if (selectedSaltPercent == percent) WarmCream else SteelMuted)
                        }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ObsidianBlack),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FlameAccent, CopperGold))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("وزن نمک خالص محاسباتی:", color = TextLight, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        String.format(Locale.US, "%.1f گرم نمک تصفیه‌شده", calculatedSaltGrams),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = CopperGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "توصیه: در روش درای براینینگ، این مقدار نمک را ۲ تا ۱۲ ساعت قبل از پخت روی گوشت بپاشید و در یخچال بگذارید تا کاملاً جذب عمق بافت شود.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
