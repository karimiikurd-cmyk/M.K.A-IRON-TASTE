package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

/**
 * Official M.K.A IRON TASTE Brand Wordmark & Emblem.
 *
 * Brand Identity:
 * - M.K.A: Chef's signature personal brand.
 * - IRON: Industrial strength, durability, forged metal feel.
 * - TASTE: Flavor mastery, culinary craft.
 * - Motif: Kurdish & Iranian geometric diamond/rhombus accents with dark metal textures.
 */
@Composable
fun MkaWordmarkBanner(
    totalRecipes: Int,
    onOpenAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BurgundyDeep.copy(alpha = 0.95f),
                        CharcoalDark,
                        ObsidianBlack
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CopperFlame.copy(alpha = 0.6f),
                        BurgundyDeep.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
    ) {
        // Geometric Kurdish diamond watermark background canvas
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidth = 1.dp.toPx()
            val gridColor = CopperFlame.copy(alpha = 0.07f)

            // Draw subtle Kurdish geometric diamond lines in background
            val w = size.width
            val h = size.height

            // Center diamond
            val cx = w / 2f
            val cy = h / 2f
            val diamondSize = 90.dp.toPx()

            val path = Path().apply {
                moveTo(cx, cy - diamondSize)
                lineTo(cx + diamondSize * 1.6f, cy)
                lineTo(cx, cy + diamondSize)
                lineTo(cx - diamondSize * 1.6f, cy)
                close()
            }
            drawPath(path, color = gridColor, style = Stroke(width = strokeWidth))

            // Lateral industrial framing lines
            drawLine(
                color = CopperFlame.copy(alpha = 0.15f),
                start = Offset(16.dp.toPx(), h - 2.dp.toPx()),
                end = Offset(w - 16.dp.toPx(), h - 2.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Row: Brand Monogram & Kurdish Rhombus motif + About Trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Kurdish Rhombus / Forged Stamp Accent
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    KurdishRhombusAccent(size = 14.dp, color = CopperFlame)
                    Text(
                        text = "CHEF'S SIGNATURE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = CopperLight.copy(alpha = 0.8f),
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Info / About Brand Button
                IconButton(
                    onClick = onOpenAbout,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("button_mka_about")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "درباره برند M.K.A IRON TASTE",
                        tint = CopperLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Prominent "M.K.A" Signature Monogram
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(28.dp)
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
                    modifier = Modifier.testTag("brand_signature_mka")
                )

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(28.dp)
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
                shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp),
                color = CharcoalDark.copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, CopperFlame.copy(alpha = 0.45f)),
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = "IRON TASTE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                    color = CopperFlame,
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 3.dp)
                        .testTag("brand_wordmark_iron_taste")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle & Total Recipes Counter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "طعم قدرتمند با هویت صنعتی و اصیل",
                    fontSize = 11.sp,
                    color = AntiqueParchment,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "•",
                    color = CopperFlame,
                    fontSize = 11.sp
                )
                Text(
                    text = if (totalRecipes > 0) "$totalRecipes دستور تخصصی" else "بارگذاری بانک دستورها...",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CopperLight
                )
            }
        }
    }
}

/**
 * Compact Header Wordmark for Top Bars / Navigation
 */
@Composable
fun MkaWordmarkHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            shape = CutCornerShape(4.dp),
            color = CopperFlame,
            modifier = Modifier.size(28.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "M",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = ObsidianBlack
                )
            }
        }

        Column {
            Text(
                text = "M.K.A IRON TASTE",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = WarmCream
            )
            Text(
                text = "POWERFUL FLAVOR • INDUSTRIAL CRAFT",
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = CopperLight
            )
        }
    }
}

/**
 * Kurdish & Iranian Geometric Rhombus Accent (Vector Canvas)
 */
@Composable
fun KurdishRhombusAccent(
    size: androidx.compose.ui.unit.Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val s = this.size.width
        val path = Path().apply {
            moveTo(s / 2f, 0f)
            lineTo(s, s / 2f)
            lineTo(s / 2f, s)
            lineTo(0f, s / 2f)
            close()
        }
        drawPath(path, color = color)

        // Inner negative diamond
        val innerPath = Path().apply {
            moveTo(s / 2f, s * 0.25f)
            lineTo(s * 0.75f, s / 2f)
            lineTo(s / 2f, s * 0.75f)
            lineTo(s * 0.25f, s / 2f)
            close()
        }
        drawPath(innerPath, color = ObsidianBlack)
    }
}

/**
 * Official About Dialog explaining the M.K.A IRON TASTE Brand Identity.
 */
@Composable
fun AboutMkaIronTasteDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MetalCard,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CopperFlame.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Badge
                Surface(
                    shape = CutCornerShape(6.dp),
                    color = CharcoalDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CopperFlame),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "M . K . A",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 4.sp,
                            color = WarmCream
                        )
                        Text(
                            text = "IRON TASTE",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 3.sp,
                            color = CopperFlame
                        )
                    }
                }

                Text(
                    text = "هویت رسمی و فلسفه برند",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )

                // Pillar Breakdown
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CharcoalDark)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BrandPillarItem(
                        tag = "M.K.A",
                        title = "امضای شخصی سرآشپز",
                        desc = "نشان اختصاصی و امضای کیفیت سرآشپز در انتخاب بهترین متریال و ترکیب‌های نوآورانه."
                    )
                    BrandPillarItem(
                        tag = "IRON",
                        title = "استحکام، ماندگاری و هویت صنعتی راک/متال",
                        desc = "الهام‌گرفته از صلابت آهن گداخته، چدن داغ کارگاهی و متدهای فرآوری بادوام و دقیق."
                    )
                    BrandPillarItem(
                        tag = "TASTE",
                        title = "خلاقیت طعم و اصالت آشپزی",
                        desc = "تسلط بر مرینیت‌ها، چاشنی‌ها و ترکیب طعم‌های اصیل کردی، ایرانی و ملل."
                    )
                }

                // Slogan banner
                Text(
                    text = "\"طعم قدرتمند با هویت صنعتی راک/متال\"",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CopperLight,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CopperFlame, contentColor = ObsidianBlack),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("بستن", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun BrandPillarItem(
    tag: String,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = BurgundyDeep,
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(
                text = tag,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = WarmCream,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = WarmCream
            )
            Text(
                text = desc,
                fontSize = 11.sp,
                color = SilverMuted,
                lineHeight = 16.sp
            )
        }
    }
}
