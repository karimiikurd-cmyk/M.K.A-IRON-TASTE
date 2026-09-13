package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun BatchScaleCalculator(
    currentWeightKg: Double,
    onWeightChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets = listOf(0.5, 1.0, 2.0, 3.0, 5.0, 10.0, 15.0, 20.0, 25.0, 50.0)
    var showCustomDialog by remember { mutableStateOf(false) }
    var customInputText by remember { mutableStateOf("") }
    var customInputError by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MetalBorder, RoundedCornerShape(16.dp))
            .testTag("batch_scale_calculator"),
        color = MetalCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
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
                        imageVector = Icons.Default.Scale,
                        contentDescription = "محاسبه‌گر وزن بچ",
                        tint = CopperFlame,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "محاسبه‌گر وزن تولید کارگاهی",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarmCream
                    )
                }

                // Base recipe indicator
                Surface(
                    color = BurgundyDeep,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonBright.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "پایه فرمول: ۱ کیلوگرم",
                        color = WarmCream,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Current weight prominent display and steppers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CharcoalDark)
                    .border(1.dp, MetalBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stepper Minus
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    FilledIconButton(
                        onClick = {
                            val next = (currentWeightKg - 1.0).coerceAtLeast(0.5)
                            onWeightChange(Math.round(next * 10.0) / 10.0)
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MetalSurface,
                            contentColor = CopperFlame
                        ),
                        modifier = Modifier.size(38.dp).testTag("button_weight_minus_1kg")
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "کاهش ۱ کیلوگرم")
                    }

                    FilledIconButton(
                        onClick = {
                            val next = (currentWeightKg - 0.5).coerceAtLeast(0.5)
                            onWeightChange(Math.round(next * 10.0) / 10.0)
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MetalSurface,
                            contentColor = SilverMuted
                        ),
                        modifier = Modifier.size(38.dp).testTag("button_weight_minus_half_kg")
                    ) {
                        Text("-0.5", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Big Weight Display (Clickable for custom input)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            customInputText = if (currentWeightKg % 1.0 == 0.0) "${currentWeightKg.toInt()}" else "$currentWeightKg"
                            customInputError = false
                            showCustomDialog = true
                        }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .testTag("button_open_custom_weight_dialog")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (currentWeightKg % 1.0 == 0.0) {
                                "${currentWeightKg.toInt()}"
                            } else {
                                String.format(Locale.US, "%.1f", currentWeightKg)
                            },
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = CopperFlame
                        )
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "ویرایش دستی وزن",
                            tint = CopperLight.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = if (currentWeightKg < 1.0) "کیلوگرم (۵۰۰ گرم)" else "کیلوگرم گوشت خالص",
                        fontSize = 12.sp,
                        color = SilverMuted
                    )
                }

                // Stepper Plus
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    FilledIconButton(
                        onClick = {
                            val next = (currentWeightKg + 0.5).coerceAtMost(50.0)
                            onWeightChange(Math.round(next * 10.0) / 10.0)
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MetalSurface,
                            contentColor = SilverMuted
                        ),
                        modifier = Modifier.size(38.dp).testTag("button_weight_plus_half_kg")
                    ) {
                        Text("+0.5", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    FilledIconButton(
                        onClick = {
                            val next = (currentWeightKg + 1.0).coerceAtMost(50.0)
                            onWeightChange(Math.round(next * 10.0) / 10.0)
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MetalSurface,
                            contentColor = CopperFlame
                        ),
                        modifier = Modifier.size(38.dp).testTag("button_weight_plus_1kg")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "افزایش ۱ کیلوگرم")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Slider for smooth continuous scaling
            Slider(
                value = currentWeightKg.toFloat(),
                onValueChange = { newVal ->
                    val rounded = Math.round(newVal * 2) / 2.0 // step by 0.5kg
                    onWeightChange(rounded.coerceIn(0.5, 50.0))
                },
                valueRange = 0.5f..50f,
                colors = SliderDefaults.colors(
                    thumbColor = CopperFlame,
                    activeTrackColor = CopperFlame,
                    inactiveTrackColor = MetalSurface
                ),
                modifier = Modifier.fillMaxWidth().testTag("weight_slider")
            )

            // Preset Chips
            Text(
                text = "انتخاب سریع وزن کارگاهی:",
                fontSize = 11.sp,
                color = SilverMuted,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Custom weight chip
                item {
                    val isCustom = presets.none { Math.abs(currentWeightKg - it) < 0.05 }
                    Surface(
                        color = if (isCustom) BurgundyPrimary else CharcoalDark,
                        contentColor = WarmCream,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCustom) CrimsonBright else MetalBorder
                        ),
                        modifier = Modifier
                            .clickable {
                                customInputText = if (currentWeightKg % 1.0 == 0.0) "${currentWeightKg.toInt()}" else "$currentWeightKg"
                                customInputError = false
                                showCustomDialog = true
                            }
                            .testTag("preset_custom")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp), tint = CopperFlame)
                            Text(
                                text = "وزن دلخواه...",
                                fontSize = 12.sp,
                                fontWeight = if (isCustom) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                items(presets) { preset ->
                    val isSelected = Math.abs(currentWeightKg - preset) < 0.05
                    val label = if (preset == 0.5) "۵۰۰ گرم" else "${preset.toInt()} ک"

                    Surface(
                        color = if (isSelected) CopperFlame else MetalSurface,
                        contentColor = if (isSelected) ObsidianBlack else WarmCream,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) CopperFlame else MetalBorder
                        ),
                        modifier = Modifier
                            .clickable { onWeightChange(preset) }
                            .testTag("preset_${preset}")
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }

    if (showCustomDialog) {
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = {
                Text(
                    text = "تنظیم وزن دلخواه کارگاهی",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "وزن دقیق بچ را به کیلوگرم وارد کنید (مثال: 7.5 یا 12.25):",
                        fontSize = 13.sp,
                        color = AntiqueParchment
                    )
                    OutlinedTextField(
                        value = customInputText,
                        onValueChange = {
                            customInputText = it
                            customInputError = false
                        },
                        isError = customInputError,
                        label = { Text("وزن گوشت (کیلوگرم)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CharcoalDark,
                            unfocusedContainerColor = CharcoalDark,
                            focusedBorderColor = CopperFlame,
                            unfocusedBorderColor = MetalBorder,
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream,
                            focusedLabelColor = CopperFlame
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_custom_weight_field")
                    )
                    if (customInputError) {
                        Text(
                            text = "لطفاً یک عدد مثبت معتبر وارد کنید (۰.۱ تا ۱۰۰۰)",
                            color = CrimsonBright,
                            fontSize = 11.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = customInputText.toDoubleOrNull()
                        if (parsed != null && parsed in 0.1..1000.0) {
                            onWeightChange(Math.round(parsed * 100.0) / 100.0)
                            showCustomDialog = false
                        } else {
                            customInputError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CopperFlame,
                        contentColor = ObsidianBlack
                    ),
                    modifier = Modifier.testTag("button_confirm_custom_weight")
                ) {
                    Text("اعمال وزن", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCustomDialog = false }
                ) {
                    Text("انصراف", color = SilverMuted)
                }
            },
            containerColor = MetalCard,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
