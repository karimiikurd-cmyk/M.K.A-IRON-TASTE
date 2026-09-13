package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.database.RecipeTypeConverters
import com.example.data.model.Ingredient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("M.K.A IRON TASTE", appName)
  }

  @Test
  fun `test database population with 1160 recipes and all categories`() = kotlinx.coroutines.runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = androidx.room.Room.inMemoryDatabaseBuilder(context, com.example.data.database.AppDatabase::class.java).build()
    val dao = db.recipeDao()
    com.example.data.database.DatabaseInitializer.populateIfNeeded(context, dao)

    val total = dao.getRecipeCount()
    assertEquals(1160, total)

    val seafood = dao.getCountByCategory("مرینیت‌های ماهی و غذاهای دریایی")
    assertEquals(70, seafood)

    val sausage = dao.getCountByCategory("سوسیس و کالباس دست‌ساز")
    assertEquals(60, sausage)

    val rubs = dao.getCountByCategory("راب‌های خشک و ادویه‌جات ترکیبی")
    assertEquals(60, rubs)

    val readyToCook = dao.getCountByCategory("محصولات آماده طبخ قصابی")
    assertEquals(65, readyToCook)

    val oils = dao.getCountByCategory("روغن‌ها و کره‌های طعم‌دار")
    assertEquals(60, oils)

    val sauces = dao.getCountByCategory("سس‌های باربیکیو و گلیزها")
    assertEquals(100, sauces)

    val turkey = dao.getCountByCategory("مرینیت‌های بوقلمون و بلدرچین")
    assertEquals(60, turkey)

    val lamb = dao.getCountByCategory("مرینیت‌های گوشت بره و گوسفندی")
    assertEquals(65, lamb)

    db.close()
  }
}

