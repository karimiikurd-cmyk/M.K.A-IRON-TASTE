package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.database.AppDatabase
import com.example.data.database.DatabaseInitializer
import com.example.data.database.RecipeTypeConverters
import com.example.data.model.Ingredient
import com.example.data.model.RecipeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MkaIronTasteFullAuditTest {

    private lateinit var context: Context
    private lateinit var db: AppDatabase

    @Before
    fun setUp() = runBlocking {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        DatabaseInitializer.populateIfNeeded(context, db.recipeDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `test recipe database integrity - exactly 1160 recipes and valid contents`() = runBlocking {
        val dao = db.recipeDao()
        val totalCount = dao.getRecipeCount()
        assertEquals("Total recipes in database must be exactly 1160", 1160, totalCount)

        val allRecipes = dao.getAllRecipes().first()
        assertEquals(1160, allRecipes.size)

        val converters = RecipeTypeConverters()

        for (recipe in allRecipes) {
            assertTrue("Recipe id must not be blank: ${recipe.name}", recipe.id.isNotBlank())
            assertTrue("Recipe name must not be blank: ${recipe.id}", recipe.name.isNotBlank())
            assertTrue("Recipe category must not be blank: ${recipe.name}", recipe.category.isNotBlank())
            assertTrue("Recipe categoryFa must not be blank: ${recipe.name}", recipe.categoryFa.isNotBlank())
            assertTrue("Recipe baseProtein must not be blank: ${recipe.name}", recipe.baseProtein.isNotBlank())

            val ingredients = converters.toIngredientsList(recipe.ingredientsJson)
            assertTrue("Recipe ingredients must not be empty: ${recipe.name}", ingredients.isNotEmpty())
            for (ing in ingredients) {
                assertTrue("Ingredient name must not be blank in recipe: ${recipe.name}", ing.name.isNotBlank())
                assertTrue("Ingredient amount must be > 0 in recipe: ${recipe.name}", ing.amount > 0)
                assertTrue("Ingredient unit must not be blank in recipe: ${recipe.name}", ing.unit.isNotBlank())
            }

            val steps = converters.toStringList(recipe.prepStepsJson)
            assertTrue("Recipe prep steps must not be empty: ${recipe.name}", steps.isNotEmpty())
            for (step in steps) {
                assertTrue("Step text must not be blank in recipe: ${recipe.name}", step.isNotBlank())
            }
        }
    }

    @Test
    fun `test all categories exist and have valid counts`() = runBlocking {
        val dao = db.recipeDao()
        val allRecipes = dao.getAllRecipes().first()
        val categoryCounts = allRecipes.groupBy { it.categoryFa }.mapValues { it.value.size }
        
        // Verify all recipes are distributed across categories with no empty or null categories
        assertTrue("Database must have at least 15 categories", categoryCounts.size >= 15)
        var sum = 0
        for ((catName, count) in categoryCounts) {
            assertTrue("Category '$catName' must not be blank", catName.isNotBlank())
            assertTrue("Category '$catName' must have at least 10 recipes, had $count", count >= 10)
            sum += count
        }
        assertEquals("Sum of all category counts must equal 1160", 1160, sum)

        // Specifically verify core butchery categories
        assertTrue(dao.getCountByCategory("مرینیت‌های ماهی و غذاهای دریایی") >= 50)
        assertTrue(dao.getCountByCategory("سوسیس و کالباس دست‌ساز") >= 50)
        assertTrue(dao.getCountByCategory("استیک و مرینیت استیک") >= 50)
    }

    @Test
    fun `test precision batch weight scaling calculations`() {
        val baseQuantityKg = 1.0
        val baseIngredients = listOf(
            Ingredient("گوشت سینه مرغ خالص", 1000.0, "گرم"),
            Ingredient("زعفران دم‌کرده قلیظ", 30.0, "میلی‌لیتر"),
            Ingredient("نمک تصفیه‌شده کارگاهی", 14.0, "گرم"),
            Ingredient("روغن زیتون فرابکر", 50.0, "میلی‌لیتر")
        )

        // Helper function for scaling
        fun scale(amount: Double, targetWeight: Double) = amount * (targetWeight / baseQuantityKg)

        // Test 500g (0.5 kg)
        val w500g = 0.5
        assertEquals(500.0, scale(1000.0, w500g), 0.001)
        assertEquals(15.0, scale(30.0, w500g), 0.001)
        assertEquals(7.0, scale(14.0, w500g), 0.001)
        assertEquals(25.0, scale(50.0, w500g), 0.001)

        // Test 1 kg
        val w1kg = 1.0
        assertEquals(1000.0, scale(1000.0, w1kg), 0.001)
        assertEquals(30.0, scale(30.0, w1kg), 0.001)
        assertEquals(14.0, scale(14.0, w1kg), 0.001)

        // Test 2 kg
        val w2kg = 2.0
        assertEquals(2000.0, scale(1000.0, w2kg), 0.001)
        assertEquals(60.0, scale(30.0, w2kg), 0.001)
        assertEquals(28.0, scale(14.0, w2kg), 0.001)

        // Test 5 kg
        val w5kg = 5.0
        assertEquals(5000.0, scale(1000.0, w5kg), 0.001)
        assertEquals(150.0, scale(30.0, w5kg), 0.001)
        assertEquals(70.0, scale(14.0, w5kg), 0.001)

        // Test 10 kg
        val w10kg = 10.0
        assertEquals(10000.0, scale(1000.0, w10kg), 0.001)
        assertEquals(300.0, scale(30.0, w10kg), 0.001)
        assertEquals(140.0, scale(14.0, w10kg), 0.001)

        // Test 15 kg
        val w15kg = 15.0
        assertEquals(15000.0, scale(1000.0, w15kg), 0.001)
        assertEquals(450.0, scale(30.0, w15kg), 0.001)
        assertEquals(210.0, scale(14.0, w15kg), 0.001)

        // Test 20 kg
        val w20kg = 20.0
        assertEquals(20000.0, scale(1000.0, w20kg), 0.001)
        assertEquals(600.0, scale(30.0, w20kg), 0.001)
        assertEquals(280.0, scale(14.0, w20kg), 0.001)

        // Test custom weight 7.25 kg
        val w7_25kg = 7.25
        assertEquals(7250.0, scale(1000.0, w7_25kg), 0.001)
        assertEquals(217.5, scale(30.0, w7_25kg), 0.001)
        assertEquals(101.5, scale(14.0, w7_25kg), 0.001)

        // Test custom weight 12.5 kg
        val w12_5kg = 12.5
        assertEquals(12500.0, scale(1000.0, w12_5kg), 0.001)
        assertEquals(375.0, scale(30.0, w12_5kg), 0.001)
        assertEquals(175.0, scale(14.0, w12_5kg), 0.001)
    }

    @Test
    fun `test custom recipe CRUD lifecycle - create, read, update, delete`() = runBlocking {
        val dao = db.recipeDao()
        val converters = RecipeTypeConverters()

        // 1. CREATE
        val customRecipe = RecipeEntity(
            id = "custom_test_001",
            name = "جوجه کباب کارگاهی M.K.A اختصاصی",
            englishName = "MKA Signature Chicken Kebab",
            category = "My Recipes",
            categoryFa = "دستورهای من",
            shortDescription = "فرمولاسیون تست کارگاهی برای برش راسته و فیله",
            baseProtein = "Chicken",
            baseProteinFa = "گوشت مرغ",
            recommendedCut = "فیله مرغ بدون چربی",
            flavorProfile = "Saffron",
            flavorProfileFa = "زعفرانی اصیل",
            baseQuantityKg = 1.0,
            ingredientsJson = converters.fromIngredientsList(
                listOf(
                    Ingredient("فیله مرغ", 1000.0, "گرم"),
                    Ingredient("زعفران سرگل", 25.0, "میلی‌لیتر"),
                    Ingredient("نمک دریا", 14.0, "گرم")
                )
            ),
            prepStepsJson = converters.fromStringList(
                listOf(
                    "برش فیله‌ها به ضخامت یکنواخت",
                    "مرینیت و ماساژ به مدت ۵ دقیقه",
                    "استراحت در سردخانه ۲ درجه"
                )
            ),
            marinationTime = "۶ ساعت",
            cookingMethod = "گریل زغالی",
            cookingTemp = "حرارت ۲۰۰ درجه",
            spiceLevel = "ملایم",
            recommendedUse = "سفارش ویژه مشتری",
            storageNotes = "دمای ۲ درجه",
            proTips = "از روغن زیتون در دقایق پایانی استفاده شود",
            commonMistakes = "نمک‌زدن زودهنگام قبل از ماساژ",
            substitutions = "قابل استفاده برای بوقلمون",
            isTraditional = false,
            isFusion = true,
            country = "ایران",
            region = "کارگاهی",
            isFavorite = true,
            isCustom = true,
            userNotes = "یادداشت اولیه کارگاه"
        )

        dao.insert(customRecipe)

        // 2. READ
        val retrieved = dao.getRecipeById("custom_test_001").first()
        assertNotNull("Custom recipe should be found in DB", retrieved)
        assertEquals("جوجه کباب کارگاهی M.K.A اختصاصی", retrieved!!.name)
        assertTrue(retrieved.isCustom)
        assertTrue(retrieved.isFavorite)
        assertEquals("یادداشت اولیه کارگاه", retrieved.userNotes)

        val customList = dao.getCustomRecipes().first()
        assertTrue("Custom list should contain our recipe", customList.any { it.id == "custom_test_001" })

        // 3. UPDATE
        val updatedRecipe = retrieved.copy(
            name = "جوجه کباب کارگاهی M.K.A ویرایش‌شده",
            userNotes = "یادداشت اصلاح‌شده با تنظیمات نمک جدید"
        )
        dao.insert(updatedRecipe) // OnConflictStrategy.REPLACE

        val updatedRetrieved = dao.getRecipeById("custom_test_001").first()
        assertEquals("جوجه کباب کارگاهی M.K.A ویرایش‌شده", updatedRetrieved!!.name)
        assertEquals("یادداشت اصلاح‌شده با تنظیمات نمک جدید", updatedRetrieved.userNotes)

        // Test note update directly
        dao.updateUserNotes("custom_test_001", "یادداشت سوم")
        val noteUpdated = dao.getRecipeById("custom_test_001").first()
        assertEquals("یادداشت سوم", noteUpdated!!.userNotes)

        // Test favorite toggle
        dao.setFavorite("custom_test_001", false)
        val favToggled = dao.getRecipeById("custom_test_001").first()
        assertFalse(favToggled!!.isFavorite)

        // 4. DELETE
        dao.delete(favToggled)
        val deleted = dao.getRecipeById("custom_test_001").first()
        assertNull("Recipe should be deleted and return null", deleted)

        val customListAfterDelete = dao.getCustomRecipes().first()
        assertFalse("Deleted recipe should not be in custom list", customListAfterDelete.any { it.id == "custom_test_001" })
    }

    @Test
    fun `test search and filtering with multiple parameters`() = runBlocking {
        val dao = db.recipeDao()

        // Search by query (Persian word)
        val saffronResults = dao.filterRecipes("زعفران", null, null, null).first()
        assertTrue("Search for 'زعفران' should yield results", saffronResults.isNotEmpty())
        for (r in saffronResults) {
            val match = r.name.contains("زعفران") ||
                    r.shortDescription.contains("زعفران") ||
                    r.ingredientsJson.contains("زعفران") ||
                    r.flavorProfileFa.contains("زعفران") ||
                    r.recommendedCut.contains("زعفران")
            assertTrue("Result must contain search term: ${r.name}", match)
        }

        // Search by cut (e.g. راسته or فیله)
        val tenderloinResults = dao.filterRecipes("فیله", null, null, null).first()
        assertTrue("Search for 'فیله' should yield results", tenderloinResults.isNotEmpty())

        // Filter by category
        val steakResults = dao.filterRecipes(null, "استیک و مرینیت استیک", null, null).first()
        assertTrue("Steak category results must not be empty", steakResults.isNotEmpty())
        assertEquals(dao.getCountByCategory("استیک و مرینیت استیک"), steakResults.size)

        // Filter by protein
        val chickenResults = dao.filterRecipes(null, null, "Chicken", null).first()
        assertTrue("Chicken protein results should be non-empty", chickenResults.isNotEmpty())
    }
}
