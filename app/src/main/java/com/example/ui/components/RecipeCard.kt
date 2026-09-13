package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RecipeEntity
import com.example.ui.theme.*

@Composable
fun RecipeCard(
    recipe: RecipeEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spiceColor = when (recipe.spiceLevel) {
        "بسیار تند" -> SpiceFireRed
        "تند" -> SpiceHotRed
        "متوسط" -> SpiceMediumOrange
        else -> SpiceMildGreen
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (recipe.isFavorite) CopperFlame.copy(alpha = 0.6f) else MetalBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .testTag("recipe_card_${recipe.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MetalCard
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Row: Cut Badge & Favorite Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cut Badge
                Surface(
                    color = BurgundyDeep,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonBright.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = recipe.recommendedCut,
                        color = WarmCream,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Favorite action
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("button_favorite_${recipe.id}")
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (recipe.isFavorite) "حذف از نشان‌شده‌ها" else "نشان کردن دستور",
                        tint = if (recipe.isFavorite) CopperFlame else SilverMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Persian Recipe Title
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = WarmCream,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // English Name
            if (recipe.englishName.isNotBlank()) {
                Text(
                    text = recipe.englishName,
                    fontSize = 12.sp,
                    color = SilverMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Short Description
            Text(
                text = recipe.shortDescription,
                fontSize = 12.sp,
                color = AntiqueParchment.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = MetalBorder.copy(alpha = 0.5f), thickness = 0.5.dp)

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Badges: Flavor, Marination Time, Cooking Method
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flavor & Protein Chip
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MetalSurface,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = recipe.flavorProfileFa,
                            color = CopperLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Spice level
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(spiceColor)
                        )
                        Text(
                            text = recipe.spiceLevel,
                            fontSize = 10.sp,
                            color = spiceColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Time badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = SilverMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = recipe.marinationTime,
                        fontSize = 11.sp,
                        color = SilverMuted
                    )
                }
            }
        }
    }
}
