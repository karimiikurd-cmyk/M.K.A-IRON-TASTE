# M.K.A IRON TASTE
### Professional Signature Recipe Reference Library & Butchery Batch Scaling System

> **طعم قدرتمند با هویت صنعتی و اصیل**  
> *"Powerful flavor with an industrial Rock/Metal identity."*

---

## 📖 Overview
**M.K.A IRON TASTE** is a high-performance, 100% offline native Android application designed for professional butchers, culinary artisans, and grillmasters. Built entirely with modern **Kotlin**, **Jetpack Compose (Material 3)**, and **Room Database (SQLite)**, it serves as an authoritative formulation handbook containing **1,120 production-grade meat marinades, cuts, sausages, artisan burgers, rubs, sauces, and ready-to-cook butcher display products**.

### 🏷️ Brand Identity
- **M.K.A**: The chef's personal signature and authentic brand mark.
- **IRON**: Strength, durability, forged metal character, and a subtle Rock/Metal industrial edge.
- **TASTE**: Uncompromising flavor mastery, deep culinary science, and professional butchery craft.

---

## ⚡ Key Capabilities & Features

### 1. 1,120 Production-Ready Formulations (100% Offline)
- Pre-populated locally in Room SQLite via assets on initial run.
- Zero network reliance: works completely offline in cold storage, walk-in coolers, butcher shops, and commercial kitchens.
- 18 comprehensive butcher categories:
  - جوجه کباب و مرغ آماده (Chicken & Poultry Kebab)
  - مرینیت‌های استیک و بیف (Steak & Beef Cuts)
  - مرینیت‌های گوشت بره و گوسفندی (Lamb & Mutton Cuts)
  - محصولات چرخ‌کرده و کوبیده (Minced Meat & Koobideh)
  - برگرهای دست‌ساز آرتیزان (Artisan Handcrafted Burgers)
  - سوسیس و کالباس دست‌ساز (Handmade Charcuterie & Sausages)
  - محصولات آماده طبخ قصابی (Ready-to-Cook Butcher Display)
  - مرینیت‌های ماهی و غذاهای دریایی (Fish & Seafood)
  - مرینیت‌های بوقلمون و بلدرچین (Turkey & Quail)
  - کباب‌های ملل و خاورمیانه (International Kebabs)
  - مرینیت‌های اسموکی و باربیکیو (Smoked & BBQ)
  - مرینیت‌های ملایم، گیاهی و رژیمی (Dietary & Mild)
  - راب‌های خشک و ادویه‌جات ترکیبی (Dry Rubs & Seasonings)
  - سس‌های باربیکیو و گلیزها (BBQ Glazes & Sauces)
  - روغن‌ها و کره‌های طعم‌دار (Infused Oils & Finishing Butters)
  - گارنیش و دورچین‌های قصابی (Garnishes & Butcher Accompaniments)

### 2. Industrial Batch Scaling Calculator
- Real-time precision calculations for any batch size:
  - **Quick Presets**: 500 g (0.5 kg), 1 kg, 2 kg, 3 kg, 5 kg, 10 kg, 15 kg, 20 kg, 25 kg, 50 kg.
  - **Steppers**: Direct +0.5 kg, +1.0 kg, -0.5 kg, -1.0 kg adjustments.
  - **Continuous Slider**: Drag to adjust batch weight smoothly.
  - **Custom Decimal Weight Dialog**: Enter precise batch sizes (e.g. `7.25` kg, `12.5` kg) with instant re-calculation of all ingredients, ratios, and units.

### 3. Full Recipe CRUD Lifecycle
- **Create**: Register custom in-house formulas, proprietary marinades, and specialized cuts.
- **Edit**: Edit any custom or existing recipe fields (name, cuts, flavor, spice level, ingredients, prep steps, pro tips, common mistakes).
- **Delete**: Safely delete custom formulas with a modal confirmation dialog.
- **Bookmarks (Favorites)**: 1-tap star/bookmarking for rapid access on busy shifts.
- **Chef Notes**: Add and save persistent notes, tweaks, and batch observations per recipe.

### 4. Search & Multi-Faceted Filter Engine
- Instant search across recipe titles (Persian and English), recommended cuts, short descriptions, and individual spices/ingredients (e.g., searching for "زعفران", "سماق", or "پاپریکا").
- Filter by Category, Protein Source (Chicken, Beef, Lamb, Seafood, etc.), and Flavor Profile (Saffron, Smoky, Sour/Tangy, Herbaceous, Spicy, etc.).

### 5. Professional Butcher Specialty Tools
- **Salting & Brining Percentage Calculator**: Calculate exact salt grammage based on raw meat weight across industry standards:
  - 1.2% (Steak & Low Salt)
  - 1.4% (Chicken & Kebab)
  - 1.6% (Koobideh & Spicy)
  - 1.8% (Artisan Sausage)
- **Core Temperature Guide (Core Temp)**: Accurate donor guides for beef (Rare, Medium Rare, Medium, Well), lamb, poultry, and fish.
- **Supermarket Cold Display Shelf-Life Guide**: Storage matrix for display cases (1°C to 3°C).

### 6. Mobile & Kitchen Ergonomics
- Full Persian/Kurdish Right-to-Left (RTL) layout support.
- Industrial dark mode palette: Charcoal Dark (`#1F2124`), Obsidian Black (`#121315`), Copper Flame (`#D96B27`), Burgundy (`#4A121A`), Warm Cream (`#F5EBE6`).
- Minimum 48dp touch targets for touchscreen operation with kitchen gloves.
- 1-tap clipboard copy to share scaled recipes with staff via messaging apps.

---

## 🛠️ Architecture & Tech Stack

```
Architecture: MVVM (Model-View-ViewModel) + Repository Pattern
Language: Kotlin 2.0+
UI Framework: Jetpack Compose with Material Design 3
Local Database: Android Jetpack Room (SQLite) with KSP
Async & Concurrency: Kotlin Coroutines & Flow (StateFlow)
Dependency Management: Gradle Kotlin DSL (build.gradle.kts)
Testing Framework: JUnit 4 + Robolectric (JVM local testing)
```

---

## 🚀 Building & Running the App

### Prerequisites
- JDK 17+
- Android SDK (API Level 36 / 35 / 34)

### Clone Repository
```bash
git clone https://github.com/your-username/mka-iron-taste.git
cd mka-iron-taste
```

### Build Debug APK
```bash
gradle assembleDebug
```
The generated APK will be at:
`app/build/outputs/apk/debug/app-debug.apk`

### Build Release APK
```bash
gradle assembleRelease
```

### Run Unit & Verification Tests
```bash
gradle :app:testDebugUnitTest
```

---

## 📁 Repository Layout
```
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   └── recipes.json         # 1,120 verified complete recipes
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt      # Main entry point & screen router
│   │   │   │   ├── data/
│   │   │   │   │   ├── database/        # Room Database, DAOs, TypeConverters
│   │   │   │   │   ├── model/           # RecipeEntity & Ingredient data models
│   │   │   │   │   └── repository/      # RecipeRepository
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/      # BatchScaleCalculator, Wordmark, CategoryChips
│   │   │   │   │   ├── screens/         # HomeScreen, RecipeDetail, AddEdit, ButcherTools
│   │   │   │   │   ├── theme/           # Color, Type, Theme (M3)
│   │   │   │   │   └── viewmodel/       # RecipeViewModel
│   │   │   ├── res/                     # Drawables, strings.xml, adaptive icons
│   │   │   └── AndroidManifest.xml
│   │   └── test/                        # Comprehensive unit & Robolectric tests
├── build.gradle.kts
├── settings.gradle.kts
└── metadata.json
```

---

## 📄 License
All rights reserved © **M.K.A IRON TASTE**. Formulated and engineered for professional butcher craft.
