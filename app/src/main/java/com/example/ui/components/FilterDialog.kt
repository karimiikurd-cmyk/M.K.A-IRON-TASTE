package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    selectedProtein: String?,
    selectedFlavor: String?,
    onProteinSelect: (String?) -> Unit,
    onFlavorSelect: (String?) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {
    val proteins = listOf(
        "همه" to null,
        "مرغ" to "Chicken",
        "گوساله" to "Beef",
        "گوسفند" to "Lamb",
        "ماهی و میگو" to "Seafood",
        "بوقلمون و بلدرچین" to "Turkey",
        "سوسیس و کالباس" to "Sausage",
        "کباب مخلوط" to "Kebab",
        "برگر" to "Burger",
        "آماده طبخ" to "Mixed",
        "ادویه و راب" to "Spice",
        "سس و گلیز" to "Sauce",
        "کره و روغن" to "Butter",
        "سالاد و پیش‌غذا" to "Appetizer",
        "دسر سرد" to "Dessert"
    )

    val flavors = listOf(
        "همه" to null,
        "زعفرانی" to "Saffron",
        "ترش و ملس" to "Sour",
        "تند" to "Spicy",
        "دودی" to "Smoky",
        "سیر و کره" to "Garlic",
        "سبزیجات معطر" to "Herb",
        "شیرین و نمکی" to "Sweet",
        "ملایم" to "Mild"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MetalCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("filter_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
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
                            Icons.Default.FilterList,
                            contentDescription = null,
                            tint = CopperFlame
                        )
                        Text(
                            text = "فیلتر پیشرفته کارگاهی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WarmCream
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "بستن",
                            tint = SilverMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Protein Group
                Text(
                    text = "نوع پروتئین پایه:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CopperLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    proteins.forEach { (label, value) ->
                        val isSelected = (value == null && selectedProtein == null) ||
                                (value != null && (selectedProtein == value || selectedProtein == label))

                        Surface(
                            color = if (isSelected) BurgundyPrimary else MetalSurface,
                            contentColor = if (isSelected) WarmCream else SilverMuted,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) CrimsonBright else MetalBorder
                            ),
                            modifier = Modifier
                                .clickable { onProteinSelect(value) }
                                .testTag("filter_protein_$label")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Flavor Group
                Text(
                    text = "طعم و چاشنی غالب:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CopperLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    flavors.forEach { (label, value) ->
                        val isSelected = (value == null && selectedFlavor == null) ||
                                (value != null && (selectedFlavor == value || selectedFlavor == label))

                        Surface(
                            color = if (isSelected) CopperFlame else MetalSurface,
                            contentColor = if (isSelected) ObsidianBlack else SilverMuted,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) CopperFlame else MetalBorder
                            ),
                            modifier = Modifier
                                .clickable { onFlavorSelect(value) }
                                .testTag("filter_flavor_$label")
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

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onClearAll()
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("button_clear_filters"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SilverMuted
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MetalBorder)
                    ) {
                        Text("پاک‌کردن همه")
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("button_apply_filters"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CopperFlame,
                            contentColor = ObsidianBlack
                        )
                    ) {
                        Text("اعمال فیلتر", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
