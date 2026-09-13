package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
    var meatWeightInput by remember { mutableStateOf("5") }
    var selectedSaltPercent by remember { mutableStateOf(1.4) }

    val meatWeightKg = meatWeightInput.toDoubleOrNull() ?: 0.0
    val calculatedSaltGrams = (meatWeightKg * 1000.0) * (selectedSaltPercent / 100.0)

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
                            text = "ابزارهای تخصصی قصاب و سرآشپز",
                            fontSize = 11.sp,
                            color = CopperLight
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("button_tools_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = CopperFlame
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
            // M.K.A IRON TASTE Brand Header Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CharcoalDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CopperFlame.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "M . K . A",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        color = WarmCream
                    )
                    Text(
                        text = "IRON TASTE",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 3.sp,
                        color = CopperFlame
                    )
                    Text(
                        text = "استانداردهای محاسباتی کارگاه تخصصی و سرآشپز",
                        fontSize = 11.sp,
                        color = SilverMuted
                    )
                }
            }

            // Salt & Brine Calculator
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth().testTag("salting_calculator_card")
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Scale, contentDescription = null, tint = CopperFlame)
                        Text(
                            text = "محاسبه‌گر درصد نمک و چاشنی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WarmCream
                        )
                    }

                    Text(
                        text = "استاندارد طلایی در فرآوری پروتئینی محاسبه نمک بر اساس درصد وزن خالص گوشت است.",
                        fontSize = 12.sp,
                        color = SilverMuted,
                        lineHeight = 18.sp
                    )

                    OutlinedTextField(
                        value = meatWeightInput,
                        onValueChange = { meatWeightInput = it },
                        label = { Text("وزن گوشت (کیلوگرم)") },
                        modifier = Modifier.fillMaxWidth().testTag("input_salt_meat_weight"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CharcoalDark,
                            unfocusedContainerColor = CharcoalDark,
                            focusedBorderColor = CopperFlame,
                            unfocusedBorderColor = MetalBorder,
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream
                        ),
                        singleLine = true
                    )

                    Text(
                        text = "درصد نمک مورد نظر:",
                        fontSize = 12.sp,
                        color = CopperLight,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            1.2 to "۱.۲٪ (استیک و کم‌نمک)",
                            1.4 to "۱.۴٪ (جوجه و کباب)",
                            1.6 to "۱.۶٪ (کوبیده و تند)",
                            1.8 to "۱.۸٪ (سوسیس کارگاهی)"
                        ).forEach { (pct, label) ->
                            val isSel = selectedSaltPercent == pct
                            Surface(
                                color = if (isSel) CopperFlame else MetalSurface,
                                contentColor = if (isSel) ObsidianBlack else WarmCream,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) CopperFlame else MetalBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedSaltPercent = pct }
                            ) {
                                Text(
                                    text = "$pct%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    // Result Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CharcoalDark)
                            .border(1.dp, BurgundyPrimary, RoundedCornerShape(10.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مقدار دقیق نمک مورد نیاز:",
                            fontSize = 13.sp,
                            color = WarmCream,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = String.format(Locale.US, "%.1f گرم", calculatedSaltGrams),
                            fontSize = 18.sp,
                            color = AmberGlow,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Core Temperatures Guide
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Thermostat, contentDescription = null, tint = CrimsonBright)
                        Text(
                            text = "دمای مغزپخت گوشت (Core Temp)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WarmCream
                        )
                    }

                    TempRow(title = "استیک گوساله ریر (Rare)", temp = "۵۲ تا ۵۵ درجه")
                    TempRow(title = "استیک مدیوم ریر (Medium Rare)", temp = "۵۷ تا ۶۰ درجه")
                    TempRow(title = "استیک و دنده مدیوم (Medium)", temp = "۶۳ تا ۶۷ درجه")
                    TempRow(title = "گوشت گوسفندی و کباب ول‌دان (Well)", temp = "۷۱ تا ۷۵ درجه")
                    TempRow(title = "سینه و فیله مرغ (Chicken Breast)", temp = "۷۴ درجه (حداقل)")
                    TempRow(title = "ماهی سالمون و قزل‌آلا", temp = "۵۵ تا ۵۸ درجه")
                }
            }

            // Supermarket Cold Display Shelf-Life Guide
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MetalCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.AcUnit, contentDescription = null, tint = CopperLight)
                        Text(
                            text = "راهنمای ماندگاری در ویترین سرد قصابی (۱ تا ۳ درجه)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WarmCream
                        )
                    }

                    ShelfLifeRow("جوجه کباب زعفرانی با پیاز", "۴۸ تا ۷۲ ساعت در یخچال")
                    ShelfLifeRow("جوجه کباب با ماست (لاری)", "۳۶ تا ۴۸ ساعت (اسید ماست)")
                    ShelfLifeRow("گوشت چرخ‌کرده و پتی برگر خام", "۲۴ تا ۳۶ ساعت")
                    ShelfLifeRow("استیک و گوشت قرمز مرینیت روغنی", "۳ تا ۵ روز")
                    ShelfLifeRow("ماهی مرینیت‌شده", "۲۴ تا ۴۸ ساعت حداکثر")
                    ShelfLifeRow("محصولات وکیوم خلأ آماده طبخ", "۷ تا ۱۰ روز در یخچال")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TempRow(title: String, temp: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CharcoalDark)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 12.sp, color = AntiqueParchment)
        Text(temp, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CopperLight)
    }
}

@Composable
private fun ShelfLifeRow(product: String, duration: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CharcoalDark)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(product, fontSize = 12.sp, color = AntiqueParchment)
        Text(duration, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WarmCream)
    }
}
