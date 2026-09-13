package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class CategoryItem(
    val id: String,
    val titleFa: String,
    val icon: ImageVector
)

val ALL_CATEGORIES = listOf(
    CategoryItem("همه", "همه دستورها", Icons.AutoMirrored.Filled.MenuBook),
    CategoryItem("نشان‌شده‌ها", "نشان‌شده‌ها", Icons.Default.Bookmark),
    CategoryItem("دستورهای من", "دستورهای من", Icons.Default.EditNote),
    CategoryItem("سالاد و پیش‌غذا", "سالاد و پیش‌غذا", Icons.Default.RamenDining),
    CategoryItem("دسر سرد", "دسر سرد", Icons.Default.Icecream),
    CategoryItem("جوجه کباب و مرغ آماده", "جوجه کباب و مرغ", Icons.Default.Restaurant),
    CategoryItem("انواع کباب", "انواع کباب", Icons.Default.OutdoorGrill),
    CategoryItem("مرینیت‌های گوشت قرمز و گوساله", "گوشت قرمز و گوساله", Icons.Default.Fastfood),
    CategoryItem("استیک و مرینیت استیک", "استیک تخصصی", Icons.Default.LunchDining),
    CategoryItem("برگر و پتی", "برگر و پتی", Icons.Default.LunchDining),
    CategoryItem("سوسیس و کالباس دست‌ساز", "سوسیس دست‌ساز", Icons.Default.DinnerDining),
    CategoryItem("مرینیت‌های گوشت بره و گوسفندی", "گوشت بره و گوسفند", Icons.Default.KebabDining),
    CategoryItem("مرینیت‌های بوقلمون و بلدرچین", "بوقلمون و بلدرچین", Icons.Default.EggAlt),
    CategoryItem("مرینیت‌های ماهی و غذاهای دریایی", "ماهی و دریایی", Icons.Default.SetMeal),
    CategoryItem("راب‌های خشک و ادویه‌جات ترکیبی", "راب خشک و ادویه", Icons.Default.Grain),
    CategoryItem("سس‌های باربیکیو و گلیزها", "سس باربیکیو و لعاب", Icons.Default.SoupKitchen),
    CategoryItem("روغن‌ها و کره‌های طعم‌دار", "روغن و کره طعم‌دار", Icons.Default.Opacity),
    CategoryItem("محصولات آماده طبخ قصابی", "آماده طبخ ویترینی", Icons.Default.Inventory2),
    CategoryItem("مرینیت‌های اختصاصی", "مرینیت‌های اختصاصی", Icons.Default.Star),
    CategoryItem("مرینیت‌های بین‌المللی", "مرینیت‌های بین‌المللی", Icons.Default.Public),
    CategoryItem("دستورهای کردی", "دستورهای اصیل کردی", Icons.Default.LocalFireDepartment),
    CategoryItem("دستورهای ایرانی", "دستورهای سنتی ایرانی", Icons.Default.EmojiFlags)
)

@Composable
fun CategoryChips(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .testTag("category_chips_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    ) {
        items(ALL_CATEGORIES) { item ->
            val isSelected = selectedCategory == item.id

            Surface(
                color = if (isSelected) CopperFlame else MetalSurface,
                contentColor = if (isSelected) ObsidianBlack else WarmCream,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) CopperFlame else MetalBorder
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onCategorySelect(item.id) }
                    .testTag("category_chip_${item.id}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isSelected) ObsidianBlack else CopperLight
                    )
                    Text(
                        text = item.titleFa,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
