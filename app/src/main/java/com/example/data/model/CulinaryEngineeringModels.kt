package com.example.data.model

data class MarinadePillar(
    val id: String,
    val nameFa: String,
    val nameEn: String,
    val scientificRole: String,
    val optimalRatio: String,
    val impactOnTexture: String,
    val impactOnFlavor: String,
    val commonMistakes: String,
    val proGuidance: String
)

data class FlavorDirectionTuning(
    val directionId: String,
    val nameFa: String,
    val description: String,
    val saltAdjustment: String,
    val acidAdjustment: String,
    val fatAdjustment: String,
    val spiceAdjustment: String,
    val aromaticsAdjustment: String,
    val sweetnessAdjustment: String,
    val proChefRule: String
)

data class IngredientSubstitution(
    val missingIngredient: String,
    val missingIngredientEn: String,
    val primarySubstitute: String,
    val secondarySubstitute: String,
    val ratioGuidance: String,
    val flavorDifference: String,
    val textureDifference: String,
    val additionPoint: String,
    val safeCulinaryGuardrails: String
)

data class GeneratedRecipeConcept(
    val id: String,
    val title: String,
    val englishTitle: String,
    val baseProtein: String,
    val baseProteinFa: String,
    val flavorProfileFa: String,
    val ingredients: List<Ingredient>,
    val prepSequence: List<String>,
    val marinationTime: String,
    val cookingMethod: String,
    val cookingTemp: String,
    val proTips: String
)

object CulinaryEngineeringData {

    val marinadePillars: List<MarinadePillar> = listOf(
        MarinadePillar(
            id = "protein",
            nameFa = "پروتئین و بافت پایه",
            nameEn = "PROTEIN TISSUE",
            scientificRole = "پروتئین‌های ساختاری عضلانی شامل میوزین (Myosin) و اکتین (Actin) که در شبکه‌های فیبری آب را به دام می‌اندازند. چگالی فیبر و میزان کلاژن تعیین‌کننده عمق نفوذ مرینیت است.",
            optimalRatio = "پایه محاسبات (۱۰۰٪ وزن مرجع؛ فرمول‌ها بر مبنای ۱ کیلوگرم گوشت تنظیم می‌شوند)",
            impactOnTexture = "سطح تماس گوشت با ضخامت برش ارتباط معکوس دارد؛ قطعات نازک‌تر سریع‌تر مرینیت می‌شوند اما حساس‌ترند.",
            impactOnFlavor = "پروتئین‌های مختلف دارای طعم ذاتی (Gamey, Iron-rich, Fatty) هستند که باید چاشنی‌ها بر اساس آن متوازن شوند.",
            commonMistakes = "انداختن گوشت خیس و مرطوب درون مرینیت؛ آب سطحی مرینیت را رقیق کرده و فیلم روغنی را پس می‌زند.",
            proGuidance = "همیشه قبل از مرینیت، سطح گوشت را با دستمال حوله‌ای کاملاً خشک کنید تا منافذ گوشت چاشنی‌ها را جذب کنند."
        ),
        MarinadePillar(
            id = "salt",
            nameFa = "نمک و فشار اسمزی",
            nameEn = "SALT (NaCl)",
            scientificRole = "تنها مولکولی که قابلیت نفوذ واقعی به عمق گوشت را از طریق فرآیند اسمز دارد. نمک ساختار فیلامنت‌های میوزین را باز کرده و ظرفیت نگهداری آب (WHC) را افزایش می‌دهد.",
            optimalRatio = "۱.۲٪ تا ۱.۸٪ وزن پروتئین (۱۲ تا ۱۸ گرم در هر کیلوگرم گوشت خالص)",
            impactOnTexture = "جلوگیری از آب‌افتادن گوشت روی حرارت و ایجاد بافتی بی‌نهایت آبدار و منعطف پس از پخت",
            impactOnFlavor = "تقویت‌کننده طعم‌های ذاتی گوشت و کاهش حس تلخی؛ بدون نمک کافی هیچ مرینیتی موفق نخواهد بود.",
            commonMistakes = "ریختن چشمی نمک یا استفاده از نمک‌های طعم‌دار با درصد خلوص نامعلوم که باعث شوری تند یا بی‌نمکی می‌شود.",
            proGuidance = "در روش Dry Brining، نمک خالص را ۲ تا ۱۲ ساعت قبل به گوشت بزنید تا آب سطح را کشیده، در خود حل کند و مجدداً به عمق عضله جذب شود."
        ),
        MarinadePillar(
            id = "acid",
            nameFa = "اسیدها و آنزیم‌های فعال",
            nameEn = "ACID & ENZYMES",
            scientificRole = "کاهش pH محیط به زیر نقطه ایزوالکتریک پروتئین‌ها که باعث دناتوره شدن بافت همبند می‌شود. شامل اسید استیک (سرکه)، سیتریک (لیمو) و لاکتیک (ماست).",
            optimalRatio = "۳٪ تا ۶٪ وزن پروتئین (۳۰ تا ۶۰ گرم آبلیمو یا سرکه رقیق بر هر کیلو)",
            impactOnTexture = "اسید ملایم بافت را لطیف می‌کند؛ اما اسید زیاد و زمان طولانی بافت گوشت را تبدیل به خمیر سفید و لاستیکی بی‌رمق می‌کند (Acid cooking).",
            impactOnFlavor = "برش‌دهنده حس چربی در دهان، روشن‌کننده طعم و ایجاد حس طراوت و سرزندگی در کام",
            commonMistakes = "خواباندن سینه مرغ در آبلیموی خالص بیش از ۶ ساعت که فیبرهای پروتئین را نابود و خشک می‌کند.",
            proGuidance = "اسید لاکتیک موجود در ماست ملایم‌ترین و مطمئن‌ترین اسید برای مرینیت مرغ و گوشت قرمز است و هرگز بافت را خمیری نمی‌کند."
        ),
        MarinadePillar(
            id = "fat",
            nameFa = "روغن و لیپیدها",
            nameEn = "FAT & OILS",
            scientificRole = "حلال ترکیبات آروماتیک چربی‌دوست (Lipophilic) در ادویه‌جات. روغن لایه‌ای محافظ دور قطعات گوشت ایجاد کرده و تبخیر رطوبت درونی را متوقف می‌سازد.",
            optimalRatio = "۴٪ تا ۸٪ وزن پروتئین (۴۰ تا ۸۰ گرم روغن زیتون یا مایع باکیفیت بر هر کیلو)",
            impactOnTexture = "ایجاد لطافت ابریشمی در سطح گوشت و مانع از چسبیدن به میله‌های گریل یا صفحه چدن",
            impactOnFlavor = "آزادکننده طعم‌های پنهان فلفل، سیر، رزماری و زعفران در دهان و طولانی‌تر کردن ماندگاری افترتیست (Aftertaste)",
            commonMistakes = "افزودن روغن در اولین مرحله قبل از نمک و زعفران؛ روغن روی گوشت سد ایجاد کرده و مانع از جذب زعفران و نمک می‌شود.",
            proGuidance = "قانون طلایی قصابی: اول ادویه و اسید و زعفران را به خورد گوشت بدهید، سپس در مرحله آخر با روغن ماساژ دهید تا طعم‌ها درون گوشت قفل (Lock) شوند."
        ),
        MarinadePillar(
            id = "spices",
            nameFa = "ادویه‌جات خشک و آسیاب‌شده",
            nameEn = "SPICES & PEPPERS",
            scientificRole = "ترکیبات فیتوشیمیایی ضدباکتری و طعم‌دهنده حاوی پیپرین (فلفل سیاه)، کپسایسین (چیلی)، کورکومین (زردچوبه) و ترکیبات فنلی دودی.",
            optimalRatio = "۱٪ تا ۲٪ وزن پروتئین (۱۰ تا ۲۰ گرم مخلوط ادویه استاندارد بر کیلوگرم)",
            impactOnTexture = "تشکیل کراست و پوسته کاراملی معطر روی سطح گوشت در واکنش با حرارت گریل",
            impactOnFlavor = "ایجاد لایه‌های طعمی عمیق، حرارت کنترل‌شده، عطر دودی و رنگ چشم‌نواز ویترینی",
            commonMistakes = "استفاده از ادویه‌های کهنه و اکسیدشده که عطر خود را از دست داده و فقط طعم خاک و تلخی به جا می‌گذارند.",
            proGuidance = "فلفل سیاه را همیشه همان لحظه مرینیت نیم‌کوب کنید (سایز ۱۶ تا ۲۰ مش) تا روغن‌های فرار معطر آن آزاد شوند."
        ),
        MarinadePillar(
            id = "aromatics",
            nameFa = "آروماتیک‌ها و سبزیجات تازه",
            nameEn = "AROMATICS (Allium & Herbs)",
            scientificRole = "آلیسین در سیر و ترکیبات گوگردی در پیاز که بوی زهم گوشت را خنثی کرده و عطر کباب اصیل را بازتولید می‌کنند. رزماری، آویشن و گشنیز آنتی‌اکسیدان قوی هستند.",
            optimalRatio = "۸٪ تا ۱۵٪ آب پیاز صاف‌شده و ۱٪ تا ۲٪ سیر له شده",
            impactOnTexture = "آنزیم‌های طبیعی پیاز باعث تردی ملایم گوشت قرمز و جوجه می‌شوند.",
            impactOnFlavor = "پایه‌گذار هویت کباب ایرانی و استیک مدیترانه‌ای؛ عطری بی‌بدیل در حین پخت روی زغال",
            commonMistakes = "ریختن پیاز رنده‌شده با تفاله روی جوجه یا گوشت؛ تفاله پیاز روی آتش به سرعت می‌سوزد و لکه‌های تلخ سیاه ایجاد می‌کند.",
            proGuidance = "پیاز را چرخ کرده، از صافی ریز رد کنید و فقط از آب زلال پیاز استفاده نمایید تا سطحی براق و طلایی بدون سوختگی داشته باشید."
        ),
        MarinadePillar(
            id = "sweetness",
            nameFa = "شیرینی و قندهای کاراملی",
            nameEn = "SWEETNESS & SUGARS",
            scientificRole = "تسریع‌کننده شگفت‌انگیز واکنش میلارد (Maillard Reaction) بین آمینواسیدها و قندهای احیاکننده در دمای بالای ۱۴۰ درجه سانتی‌گراد.",
            optimalRatio = "۰.۵٪ تا ۲٪ (۵ تا ۲۰ گرم عسل، شکر قهوه‌ای یا رب انار بر کیلوگرم)",
            impactOnTexture = "ایجاد لعاب چسبنده براق (Glaze) و پوسته شیشه‌ای برشته روی سطح کباب و استیک",
            impactOnFlavor = "متعادل‌کننده تیزی اسید و نمک، ایجاد طعم ملس پیچیده در کباب‌های باربیکیو و ترش",
            commonMistakes = "زیاده‌روی در شکر در پخت با حرارت مستقیم زغال که منجر به سوختگی سیاه سریع قبل از مغزپخت شدن می‌شود.",
            proGuidance = "اگر قند مرینیت بالاست، گوشت را با فاصله از زغال بپزید یا سس گلیز قندی را در ۲ دقیقه پایانی پخت با قلم‌مو روی گوشت بمالید."
        ),
        MarinadePillar(
            id = "liquid",
            nameFa = "مایعات امولسیون و رسانا",
            nameEn = "LIQUID VEHICLE",
            scientificRole = "محیط پیوسته برای انحلال نمک و اسید و توزیع همگن تمام ذرات ادویه در تمام زوایای گوشت به صورت امولسیون پایدار.",
            optimalRatio = "۵٪ تا ۱۰٪ وزن گوشت",
            impactOnTexture = "تضمین رطوبت یکنواخت در تمام بخش‌های قطعات گوشت",
            impactOnFlavor = "انتقال یکدست مزه‌ها به بافت بدون تشکیل لکه‌های نمک‌زده یا پر ادویه در یک نقطه",
            commonMistakes = "غوطه‌ور کردن گوشت در آب زیاد که باعث شسته شدن عصاره طبیعی و خروج میوگلوبین می‌شود.",
            proGuidance = "از مایعات طعم‌دار مانند آب پیاز، سویا سس رقیق، ماست چکیده یا آبجو مالت بدون الکل استفاده کنید، نه آب لوله‌کشی معمولی."
        )
    )

    val flavorTunings: List<FlavorDirectionTuning> = listOf(
        FlavorDirectionTuning(
            directionId = "spicy",
            nameFa = "جهت طعمی تند و آتشین (Spicy / Fiery)",
            description = "تمرکز بر حرارت کپسایسین با تعادل اسیدی برای زنده نگه داشتن حس چشایی و باز شدن پرزهای چشایی",
            saltAdjustment = "نمک در حد نرمال ۱.۴٪ حفظ شود؛ نمک کم باعث بی‌رمق شدن تندی می‌شود.",
            acidAdjustment = "افزایش ۱۰٪ اسید (سرکه سیب یا لیمو) جهت تشدید و تیزتر کردن نیش فلفل در دهان",
            fatAdjustment = "روغن ۱۰٪ افزایش یابد تا مولکول‌های کپسایسین را در دهان پخش کند و سوزش نقطه‌ای به سوزش لذت‌بخش یکدست تبدیل شود.",
            spiceAdjustment = "ترکیب فلفل قرمز تند کاین (۴ گرم/کیلو)، چیلی دودی چیپوتله (۳ گرم) و فلفل سیاه نیم‌کوب تازه (۳ گرم)",
            aromaticsAdjustment = "سیر تازه رنده‌شده ۲۰٪ افزایش یابد تا تندی طبیعی آلیسین با کپسایسین هم‌افزایی ایجاد کند.",
            sweetnessAdjustment = "کاهش شکر به زیر ۵ گرم بر کیلوگرم؛ شکر اثر تندی را خنثی و تضعیف می‌کند.",
            proChefRule = "تندی مرینیت باید در ترکیب فلفل تازه و فلفل خشک باشد؛ فلفل خشک طعم پس‌زمینه و فلفل تازه ضربه اولیه را ایجاد می‌کند."
        ),
        FlavorDirectionTuning(
            directionId = "sour",
            nameFa = "جهت طعمی ترش و ملس (Sour / Tangy / Zesty)",
            description = "تحریک ترشح بزاق با ترکیبی از اسیدهای چندگانه و چاشنی‌های سنتی ایرانی و مدیترانه‌ای",
            saltAdjustment = "نمک را کمی افزایش دهید (۱.۶٪)؛ اسید بدون نمک کافی طعمی تیز و ناخوشایند پیدا می‌کند.",
            acidAdjustment = "ترکیب ۵۰/۵۰ آبلیمو تازه و آبغوره سنتی یا سرکه بالزامیک تا سقف ۵۰ گرم بر کیلوگرم",
            fatAdjustment = "روغن زیتون فرابکر برای گرد کردن لبه‌های تیز اسید ضروری است.",
            spiceAdjustment = "سماق قهوه‌ای تبریز (۱۰ گرم/کیلو)، فلفل سیاه نرم و دانه گشنیز ساییده",
            aromaticsAdjustment = "پیاز فراوان و سبزیجات ترش‌مزه مانند جعفری خرد شده و ترخون",
            sweetnessAdjustment = "افزایش ملایم (۱۰ تا ۱۵ گرم رب انار ملس یا عسل) برای ایجاد تعادل ترش و شیرین شگفت‌انگیز",
            proChefRule = "اسید لیمو ترش را بیش از ۴ ساعت روی مرغ نگذارید؛ اگر زمان مرینیت ۱۲ ساعت است، پایه اسید را ماست یا آبغوره قرار دهید."
        ),
        FlavorDirectionTuning(
            directionId = "sweet",
            nameFa = "جهت طعمی شیرین و کاراملی (Sweet & Glazed)",
            description = "سبک باربیکیو تگزاسی و کُره‌ای با پوسته کاراملی طلایی و بافت براق آبدار",
            saltAdjustment = "نمک دقیقاً ۱.۵٪ تنظیم شود؛ کنتراست شوری و شیرینی اساس این طعم است.",
            acidAdjustment = "اسید در حد ملایم (سرکه بالزامیک یا آب سیب) برای جلوگیری از دل‌زدگی از شیرینی",
            fatAdjustment = "کره آب‌شده یا روغن کنجد در مرحله پایانی برای براقیت استثنایی",
            spiceAdjustment = "پاپریکا شیرین مجارستانی، پودر خردل زرد، دارچین بسیار کم (نوک قاشق چایخوری)",
            aromaticsAdjustment = "پودر پیاز و سیر پخته کاراملی شده",
            sweetnessAdjustment = "۲۵ تا ۳۵ گرم عسل طبیعی، شکر قهوه‌ای یا ملاس نیشکر بر هر کیلوگرم",
            proChefRule = "گوشت شیرین در دمای بالای ۱۴۰ درجه به سرعت می‌سوزد؛ ابتدا گوشت را با حرارت ملایم بپزید و در ۲ دقیقه آخر حرارت را بالا ببرید."
        ),
        FlavorDirectionTuning(
            directionId = "smoky",
            nameFa = "جهت طعمی دودی و چوبی (Smoky Barbecue)",
            description = "تداعی‌کننده اسموکرهای صنعتی چوب بلوط و آتش هیزم در قصابی و رستوران",
            saltAdjustment = "استفاده از نمک دودی طبیعی مالدون یا نمک هیمالیا ۱.۵٪",
            acidAdjustment = "سرکه سیب طبیعی بهترین مکمل عطر دودی است.",
            fatAdjustment = "روغن پایه خنثی (هسته انگور یا ذرت) تا طعم دود را نپوشاند.",
            spiceAdjustment = "پاپریکا دودی ممتاز اسپانیایی (پیمنتون د لا ورا) ۶ تا ۸ گرم/کیلو، زیره دودی بو داده",
            aromaticsAdjustment = "سیر کبابی شده له شده",
            sweetnessAdjustment = "شکر قهوه‌ای ۱۰ گرم جهت کمک به کاراملیزاسیون دودی",
            proChefRule = "اگر اسموکر ندارید، از پاپریکا دودی باکیفیت و روغن زغالی‌شده سنتی (Dungar method) استفاده کنید نه اسانس‌های مصنوعی تند."
        ),
        FlavorDirectionTuning(
            directionId = "garlic",
            nameFa = "جهت طعمی سیردار قوی (Garlic-Forward / Aioli)",
            description = "عطر غنی و اشتهاآور سیر تازه مدیترانه‌ای بدون حس تلخی",
            saltAdjustment = "۱.۳٪ تا ۱.۵٪ نمک طعام استاندارد",
            acidAdjustment = "آب لیمو ترش تازه برای فعال‌سازی و در عین حال ملایم کردن تندی گوگردی سیر",
            fatAdjustment = "روغن زیتون فرابکر باکیفیت؛ سیر در چربی حل شده و عطری ابریشمی پیدا می‌کند.",
            spiceAdjustment = "فلفل سفید، آویشن شیرازی و فلفل سیاه نیم‌کوب",
            aromaticsAdjustment = "سیر له شده با سنگ هاون (۲۰ تا ۲۵ گرم بر کیلو) همراه با چند شاخه رزماری له شده",
            sweetnessAdjustment = "حداقل (صفر تا ۳ گرم)",
            proChefRule = "سیر را هرگز در روغن داغ نسوزانید؛ سیر سوخته تلخ‌ترین طعم دنیا را تولید می‌کند. در مرینیت خام، سیر آسیاب‌شده با نمک بهترین پیوند را می‌سازد."
        ),
        FlavorDirectionTuning(
            directionId = "saffron",
            nameFa = "جهت طعمی زعفرانی درباری (Royal Saffron Infused)",
            description = "استاندارد طلایی کباب‌های اشرافی ایرانی با عطر گلگون زعفران و کره محلی",
            saltAdjustment = "۱.۲٪ تا ۱.۴٪ برای اینکه ظرافت عطر زعفران تحت‌الشعاع شوری قرار نگیرد.",
            acidAdjustment = "آبلیمو بسیار کم و حتماً در آخرین دقایق قبل از پخت؛ اسید شدید رنگ زعفران را کدر می‌کند.",
            fatAdjustment = "کره حیوانی آب‌شده اعلا (گی) یا روغن زرد کرمانشاهی ۵۰ گرم بر کیلو",
            spiceAdjustment = "هل سبز ساییده شده (نوک قاشق چایخوری برای تکمیل عطر)، فلفل سفید ملایم",
            aromaticsAdjustment = "فقط و فقط آب پیاز سفید بدون تفاله؛ هرگز سیر نزنید چون بوی سیر زعفران را محو می‌کند.",
            sweetnessAdjustment = "اختیاری (نصف قاشق عسل ملایم)",
            proChefRule = "زعفران غلیظ دم‌کرده با یخ را در اولین مرحله مستقیماً روی بافت برهنه گوشت بزنید و ۲۰ دقیقه ماساژ دهید تا رنگ تا عمق گوشت نفوذ کند؛ سپس روغن بزنید."
        ),
        FlavorDirectionTuning(
            directionId = "herb",
            nameFa = "جهت طعمی گیاهی و معطر (Herb & Botanical)",
            description = "عطر چمنزار و بوته‌های وحشی کوهستان با رزماری، ترخون و آویشن کوهی",
            saltAdjustment = "۱.۵٪ نمک دریا برای استخراج اسانس‌های فرار گیاهی",
            acidAdjustment = "سرکه سفید یا آبلیمو تازه به نسبت ملایم",
            fatAdjustment = "روغن زیتون بکر فشرده سرد برای اتصال با اسانس‌های معطر",
            spiceAdjustment = "دانه خردل کوبیده، فلفل سیاه نیم‌کوب درشت ۱۶ مش",
            aromaticsAdjustment = "برگ رزماری تازه کوبیده شده، آویشن شیرازی، ترخون خشک و پیازچه ساطوری",
            sweetnessAdjustment = "بسیار کم (کمتر از ۵ گرم)",
            proChefRule = "برگ‌های رزماری و آویشن را قبل از اضافه کردن بین دو دست محکم بمالید یا با هاون بکوبید تا سلول‌های روغنی آنها شکسته و عطرشان آزاد شود."
        ),
        FlavorDirectionTuning(
            directionId = "mild",
            nameFa = "جهت طعمی ملایم و نرم (Mild & Butter-Basted)",
            description = "طراحی شده برای کودکان و افراد حساس به ادویه با تمرکز بر بافت بی‌نهایت لطیف و طعم طبیعی کره و خامه",
            saltAdjustment = "۱.۲٪ دقیق برای طعمی دلپذیر و ملایم",
            acidAdjustment = "ماست یونانی یا خامه ترش به جای آبلیموهای تند",
            fatAdjustment = "کره حیوانی پاستوریزه یا روغن حیوانی اعلا",
            spiceAdjustment = "زردچوبه مرغوب در حد رنگ طلایی ملایم، فلفل سیاه در حد نامحسوس",
            aromaticsAdjustment = "پیاز رنده شده صاف شده با عطر ملایم",
            sweetnessAdjustment = "مقدار جزئی عسل یا شکر سفید برای تعدیل مزه",
            proChefRule = "در مرینیت ملایم، کیفیت و تازگی خود پروتئین ستاره میدان است و هیچ ادویه تندی برای پنهان کردن عیوب گوشت وجود ندارد."
        ),
        FlavorDirectionTuning(
            directionId = "bold",
            nameFa = "جهت طعمی پرقدرت و کارگاهی (Bold & Intense Umami)",
            description = "طعم قوی و کوبنده مخصوص منوی ویترینی قصابی با نفوذ عمیق اومامی و ماندگاری طعم",
            saltAdjustment = "۱.۶٪ تا ۱.۸٪ همراه با سس سویا غلیظ",
            acidAdjustment = "ترکیب سرکه بالزامیک و سس ووسترشر (Worcestershire)",
            fatAdjustment = "روغن‌های غنی و چربی حیوانی فرآوری شده",
            spiceAdjustment = "مخلوط ۵ ادویه، پاپریکا دودی، پودر سیر روست‌شده و خردل دیژون دانه‌دار",
            aromaticsAdjustment = "عصاره فشرده پیاز، سیر سیاه تخمیری و زنجبیل تازه",
            sweetnessAdjustment = "رب انار غلیظ یا عسل کوهی ۱۵ گرم",
            proChefRule = "تعریف سبک M.K.A IRON TASTE: مرینیتی که حتی بعد از پختن روی زغال داغ عطر و مزه آن محو نمی‌شود و حس قدرت به گوشت می‌بخشد."
        )
    )

    val substitutions: List<IngredientSubstitution> = listOf(
        IngredientSubstitution(
            missingIngredient = "آبلیمو تازه",
            missingIngredientEn = "Fresh Lemon Juice",
            primarySubstitute = "سرکه سیب طبیعی (Apple Cider Vinegar) رقیق‌شده با آب به نسبت ۲ به ۱",
            secondarySubstitute = "آبغوره سنتی بدون نمک یا سماق خیسانده در آب جوش",
            ratioGuidance = "به ازای هر ۲ قاشق غذاخوری آبلیمو، از ۱.۵ قاشق غذاخوری سرکه سیب یا ۲ قاشق آبغوره استفاده کنید.",
            flavorDifference = "سرکه اسیدیته تیزتری دارد و عطر میوه‌ای سیب می‌دهد؛ آبغوره طعم گس و سنتی ایرانی ایجاد می‌کند.",
            textureDifference = "سرکه قدرت نرم‌کنندگی فیبر بالاتری نسبت به لیمو دارد، پس زمان مرینیت را ۲۰٪ کاهش دهید.",
            additionPoint = "همراه با نمک در ابتدای کار برای نفوذ به بافت عضلانی",
            safeCulinaryGuardrails = "از جوهر لیمو (اسید سیتریک صنعتی) غلیظ استفاده نکنید چون گوشت را می‌سوزاند و طعم فلزی ایجاد می‌کند."
        ),
        IngredientSubstitution(
            missingIngredient = "زعفران دم‌کرده اعلا",
            missingIngredientEn = "Brewed Saffron",
            primarySubstitute = "عصاره ریشه زعفران + زردچوبه درجه یک + یک قطره گلاب و نوک قاشق هل",
            secondarySubstitute = "رنگ طبیعی پاپریکا زرد مجارستانی همراه با عصاره روغن دانه هل",
            ratioGuidance = "۱ قاشق چایخوری زردچوبه تفت‌داده در روغن + نصف قاشق چایخوری پودر ریشه زعفران به ازای هر قاشق سوپ‌خوری زعفران",
            flavorDifference = "فاقد نت گلی و بوی کروسین اصیل زعفران، اما رنگ طلایی بسیار زیبا و گرمای ملایم به گوشت می‌دهد.",
            textureDifference = "هیچ تفاوتی در بافت گوشت ایجاد نمی‌کند.",
            additionPoint = "ابتدا در ۲ قاشق آب گرم یا روغن حل کرده و سپس به پروتئین بزنید.",
            safeCulinaryGuardrails = "هرگز از رنگ‌های شیمیایی غیرخوراکی موسوم به رنگ زعفران هندی (تارترازین) در قصابی استفاده نکنید."
        ),
        IngredientSubstitution(
            missingIngredient = "آب پیاز صاف‌شده",
            missingIngredientEn = "Strained Onion Juice",
            primarySubstitute = "پودر پیاز خالص استاندارد کارگاهی (Onion Powder) حل شده در آب ولرم",
            secondarySubstitute = "آب تره‌فرنگی تازه چرخ‌شده یا پیازچه ساطوری بسیار ریز",
            ratioGuidance = "۱ قاشق غذاخوری سرپر پودر پیاز حل شده در ۱۰۰ میلی‌لیتر آب ولرم به ازای آب یک عدد پیاز بزرگ",
            flavorDifference = "پودر پیاز طعمی شیرین‌تر و کاراملی‌تر دارد بدون تندی گاز سولفیدی پیاز تازه خام.",
            textureDifference = "پودر پیاز آب نمی‌اندازد و بافت گوشت را بسیار منسجم نگه می‌دارد.",
            additionPoint = "مستقیماً روی گوشت بپاشید و ماساژ دهید.",
            safeCulinaryGuardrails = "مطمئن شوید پودر پیاز حاوی نمک افزوده نباشد تا تعادل شوری مرینیت به هم نخورد."
        ),
        IngredientSubstitution(
            missingIngredient = "سس سویا (Soy Sauce)",
            missingIngredientEn = "Soy Sauce",
            primarySubstitute = "عصاره گوشت غلیظ حل شده در آب + نمک دریا + چند قطره سرکه بالزامیک",
            secondarySubstitute = "تمبر هندی رقیق شده با آب جوش + نمک و یک قطره ملاس",
            ratioGuidance = "۱ قاشق غذاخوری عصاره گوشت با نصف قاشق چایخوری سرکه بالزامیک به جای ۲ قاشق سویا سس",
            flavorDifference = "اومامی گوشتی غنی حاصل می‌شود اما عطر تخمیری لوبیای سویا غایب است.",
            textureDifference = "بافت گوشت را به خوبی تیره و آبدار می‌کند.",
            additionPoint = "در مرحله افزودن مایعات و ادویه‌ها",
            safeCulinaryGuardrails = "به دلیل شوری بالای عصاره‌های جایگزین، نمک دستی مرینیت را نصف کنید."
        ),
        IngredientSubstitution(
            missingIngredient = "ماست چکیده پرچرب",
            missingIngredientEn = "Greek / Strained Yogurt",
            primarySubstitute = "خامه ترش (Sour Cream) یا مخلوط سس مایونز و آبلیمو (نسبت ۳ به ۱)",
            secondarySubstitute = "باترمیلک دست‌ساز (شیر کامل پرچرب مخلوط با ۱ قاشق سرکه بعد از ۱۰ دقیقه استراحت)",
            ratioGuidance = "به نسبت ۱ به ۱ می‌توان خامه ترش یا ترکیب مایونز و لیمو را جایگزین ماست چکیده کرد.",
            flavorDifference = "خامه ترش چربی غنی‌تری دارد؛ ترکیب مایونز طعم تخم‌مرغی و پوشش‌دهی گریل بهتری به ارمغان می‌آورد.",
            textureDifference = "مایونز به دلیل داشتن روغن، مانع از سوختگی سطح جوجه روی زغال می‌شود.",
            additionPoint = "همراه با ادویه‌ها به عنوان لایه امولسیون‌ساز",
            safeCulinaryGuardrails = "از ماست‌های شیرین یا ماست‌های ترشیده که بوی کپک می‌دهند هرگز استفاده نکنید."
        ),
        IngredientSubstitution(
            missingIngredient = "سس خردل دیژون (Dijon Mustard)",
            missingIngredientEn = "Dijon Mustard",
            primarySubstitute = "پودر خردل زرد خشک حل شده در سرکه سفید انگور و چند قطره روغن زیتون",
            secondarySubstitute = "سس خردل زرد ملایم معمولی به اضافه نوک قاشق فلفل سیاه و سرکه",
            ratioGuidance = "نصف قاشق چایخوری پودر خردل + نصف قاشق چایخوری سرکه به جای ۱ قاشق چایخوری خردل دیژون",
            flavorDifference = "پودر خردل تندی دماغ‌سوز قوی‌تری دارد؛ خردل معمولی زرد طعم ملایم‌تری ایجاد می‌کند.",
            textureDifference = "خردل دیژون خاصیت امولسیون‌کنندگی بالایی بین روغن و آبلیمو دارد؛ هنگام استفاده از پودر خردل هم‌زدن بیشتری لازم است.",
            additionPoint = "در ابتدای تهیه سس مرینیت برای پایدارسازی امولسیون",
            safeCulinaryGuardrails = "خردل دیژون دارای نمک است؛ با جایگزینی پودر خردل میزان نمک را کنترل کنید."
        ),
        IngredientSubstitution(
            missingIngredient = "پاپریکا دودی (Smoked Paprika)",
            missingIngredientEn = "Smoked Paprika",
            primarySubstitute = "پاپریکا معمولی شیرین + یک قطره عصاره دود مایع خالص (Liquid Smoke)",
            secondarySubstitute = "پودر چیپوتله دودی یا فلفل دلمه‌ای خشک‌شده روی زغال هیزمی",
            ratioGuidance = "۱ قاشق چایخوری پاپریکا معمولی + ۱ قطره بسیار کوچک دود مایع",
            flavorDifference = "بسیار نزدیک؛ رنگ قرمز آتشین با عطر هیزم چوب بلوط حاصل می‌شود.",
            textureDifference = "یکسان",
            additionPoint = "همراه با سایر ادویه‌های خشک",
            safeCulinaryGuardrails = "دود مایع را هرگز بیش از ۱ تا ۲ قطره در هر کیلوگرم استفاده نکنید؛ قطره اضافه طعم قطران تلخ و شیمیایی می‌دهد."
        ),
        IngredientSubstitution(
            missingIngredient = "رزماری تازه",
            missingIngredientEn = "Fresh Rosemary",
            primarySubstitute = "رزماری خشک سائیده شده (نصف مقدار تازه)",
            secondarySubstitute = "آویشن کوهی شیرازی (Thyme) یا برگ مریم‌گلی (Sage)",
            ratioGuidance = "۱ قاشق چایخوری رزماری خشک به ازای ۱ شاخه کامل رزماری تازه",
            flavorDifference = "رزماری خشک غلظت اسانس بالاتری دارد اما عطر علفی شاداب برگ تازه را ندارد.",
            textureDifference = "رزماری خشک را حتماً در هاون پودر کنید تا تکه‌های چوبی آن زیر دندان نیاید.",
            additionPoint = "در روغن گرم حل شود تا اسانس آن باز شود.",
            safeCulinaryGuardrails = "رزماری خشک اگر در هاون نرم نشود، لبه‌های سوزنی آن هنگام سوختن روی گوشت تلخ می‌شود."
        ),
        IngredientSubstitution(
            missingIngredient = "روغن زیتون فرابکر (EVOO)",
            missingIngredientEn = "Extra Virgin Olive Oil",
            primarySubstitute = "روغن هسته انگور (نقطه دود بالاتر، بی‌بو و بسیار سبک)",
            secondarySubstitute = "روغن کنجد تصفیه‌شده، روغن آفتابگردان مرغوب یا کره شفاف‌شده (Ghee)",
            ratioGuidance = "نسبت ۱ به ۱ جایگزین شود.",
            flavorDifference = "روغن هسته انگور کاملاً خنثی است و طعم خود گوشت و ادویه‌ها را خالص‌تر نشان می‌دهد.",
            textureDifference = "پوشش فوق‌العاده روی گوشت ایجاد کرده و در گریل بسیار دیرتر می‌سوزد.",
            additionPoint = "مرحله نهایی مرینیت برای قفل کردن طعم‌ها",
            safeCulinaryGuardrails = "برای استیک‌هایی که در حرارت بسیار بالا پخته می‌شوند، روغن هسته انگور حتی از روغن زیتون بکر عملکرد بهتری دارد."
        ),
        IngredientSubstitution(
            missingIngredient = "سس بالزامیک (Balsamic Vinegar)",
            missingIngredientEn = "Balsamic Vinegar",
            primarySubstitute = "سرکه قرمز انگور + رب انار ملس یا شیره انگور غلیظ (نسبت ۲ به ۱)",
            secondarySubstitute = "سرکه سیب مخلوط با مقداری عسل و سس سویا",
            ratioGuidance = "۲ قاشق سرکه قرمز + ۱ قاشق شیره انگور به جای ۳ قاشق بالزامیک",
            flavorDifference = "اسیدیته ملایم میوه‌ای با شیرینی ملاس‌مانند و رنگ تیره درخشان",
            textureDifference = "ایجاد کاراملیزاسیون و براقیت عالی روی استیک و فیله",
            additionPoint = "در فاز اسید و شیرینی مرینیت",
            safeCulinaryGuardrails = "رب انار نباید بیش از حد ترش باشد؛ تعادل اسید و قند بالزامیک باید حفظ شود."
        )
    )

    fun generateRecipeConcepts(
        protein: String,
        selectedIngredients: List<String>
    ): List<GeneratedRecipeConcept> {
        val ingredientsLower = selectedIngredients.map { it.trim().lowercase() }
        val hasYogurt = ingredientsLower.any { it.contains("ماست") || it.contains("yogurt") }
        val hasSaffron = ingredientsLower.any { it.contains("زعفران") || it.contains("saffron") }
        val hasGarlic = ingredientsLower.any { it.contains("سیر") || it.contains("garlic") }
        val hasLemon = ingredientsLower.any { it.contains("لیمو") || it.contains("lemon") }
        val hasOnion = ingredientsLower.any { it.contains("پیاز") || it.contains("onion") }
        val hasSoy = ingredientsLower.any { it.contains("سویا") || it.contains("soy") }
        val hasMustard = ingredientsLower.any { it.contains("خردل") || it.contains("mustard") }
        val hasRosemary = ingredientsLower.any { it.contains("رزماری") || it.contains("rosemary") }
        val hasSmoky = ingredientsLower.any { it.contains("دودی") || it.contains("پاپریکا") || it.contains("smoke") }

        val concepts = mutableListOf<GeneratedRecipeConcept>()

        // Concept 1: Saffron & Royal Citrus
        val c1Name = if (hasSaffron) "جوجه/استیک سلطنتی زعفرانی و لیمو ترش M.K.A" else "کباب کرمی لیمو و فلفل سیاه کارگاهی"
        concepts.add(
            GeneratedRecipeConcept(
                id = "concept_royal_${System.currentTimeMillis()}_1",
                title = c1Name,
                englishTitle = "Royal Saffron Citrus Infusion",
                baseProtein = protein,
                baseProteinFa = protein,
                flavorProfileFa = "زعفرانی، مرکباتی، کرمی و معطر",
                ingredients = listOf(
                    Ingredient("گوشت خالص $protein پاک‌شده", 1000.0, "گرم"),
                    Ingredient("زعفران غلیظ دم‌کرده با یخ", 25.0, "میلی‌لیتر"),
                    Ingredient("آب لیمو ترش تازه شیرازی", 40.0, "میلی‌لیتر"),
                    Ingredient("آب پیاز زلال صاف‌شده بدون تفاله", 100.0, "میلی‌لیتر"),
                    Ingredient("روغن زیتون یا کره حیوانی آب‌شده", 50.0, "گرم"),
                    Ingredient("نمک طعام تصفیه‌شده کارگاهی", 14.0, "گرم"),
                    Ingredient("فلفل سیاه تازه نیم‌کوب ۱۶ مش", 4.0, "گرم"),
                    Ingredient("سیر تازه له‌شده با سنگ", 10.0, "گرم")
                ),
                prepSequence = listOf(
                    "۱. سنجش و آماده‌سازی: گوشت $protein را پس از تمیزکاری با دستمال حوله‌ای کاملاً خشک نموده و در دمای ۴ درجه آماده کنید.",
                    "۲. تثبیت رنگ زعفرانی: زعفران دم‌کرده خالص را مستقیماً روی بافت برهنه گوشت ریخته و ۵ دقیقه ماساژ دهید تا رنگ تا عمق گوشت نفوذ کند.",
                    "۳. اضافه کردن اسید و نمک: نمک، آب لیمو ترش تازه و آب پیاز صاف‌شده را افزوده و ترکیب را یکنواخت نمایید.",
                    "۴. قفل با روکش روغنی: روغن زیتون یا کره آب‌شده را در آخرین گام افزوده و ماساژ دهید تا فیلم محافظتی تشکیل شود.",
                    "۵. استراحت و خواباندن: ظرف را سلفون کشیده و ۴ تا ۸ ساعت در یخچال (دمای ۱ تا ۳ درجه) استراحت دهید."
                ),
                marinationTime = "۴ تا ۸ ساعت در یخچال",
                cookingMethod = "گریل منقل زغالی یا تابه چدنی با حرارت متوسط رو به بالا",
                cookingTemp = "دمای هسته گوشت ۷۴ درجه (مرغ) یا ۵۵ درجه (گوساله)",
                proTips = "آب پیاز را حتماً از صافی بسیار ریز رد کنید تا هیچ تفاله‌ای روی آتش نسوزد."
            )
        )

        // Concept 2: Garlic & Herb Steakhouse
        val c2Name = "مرینیت سیر، خردل دیژون و گیاهان معطر استیک‌هاوس"
        concepts.add(
            GeneratedRecipeConcept(
                id = "concept_herb_${System.currentTimeMillis()}_2",
                title = c2Name,
                englishTitle = "Garlic Dijon & Herb Steakhouse Marinade",
                baseProtein = protein,
                baseProteinFa = protein,
                flavorProfileFa = "سیردار قوی، خردلی، علفی و دودی ملایم",
                ingredients = listOf(
                    Ingredient("گوشت خالص $protein", 1000.0, "گرم"),
                    Ingredient("سیر تازه پوره شده", 20.0, "گرم"),
                    Ingredient("سس خردل دیژون", 25.0, "گرم"),
                    Ingredient("سرکه سیب طبیعی یا آبلیمو", 30.0, "میلی‌لیتر"),
                    Ingredient("روغن زیتون فرابکر", 60.0, "میلی‌لیتر"),
                    Ingredient("رزماری و آویشن کوهی ساطوری", 10.0, "گرم"),
                    Ingredient("پاپریکا دودی ممتاز", 6.0, "گرم"),
                    Ingredient("نمک دریا و فلفل سیاه نیم‌کوب", 16.0, "گرم")
                ),
                prepSequence = listOf(
                    "۱. تهیه امولسیون سس: خردل دیژون، سیر پوره شده، سرکه و نمک را در ظرفی با همزن دستی بزنید.",
                    "۲. ورود روغن: روغن زیتون را به صورت نواری باریک همزمان با هم‌زدن بیفزایید تا سسی کرمی و پایدار به دست آید.",
                    "۳. افزودن ادویه‌ها: رزماری، آویشن، پاپریکا دودی و فلفل سیاه را اضافه کرده و هم بزنید.",
                    "۴. پوشش کامل پروتئین: قطعات $protein را به سس آغشته کرده و با دستکش به آرامی مالش دهید.",
                    "۵. بیات‌سازی سرد: ۶ تا ۱۲ ساعت در دمای ۲ درجه سانتی‌گراد نگهداری نمایید."
                ),
                marinationTime = "۶ تا ۱۲ ساعت در دمای سردخانه",
                cookingMethod = "چدن شیاردار بسیار داغ یا گریل باربیکیو",
                cookingTemp = "حرارت مستقیم بالا با استراحت ۵ دقیقه‌ای پس از پخت",
                proTips = "خردل دیژون مانع از دوفاز شدن روغن و اسید شده و در هنگام گریل پوسته قهوه‌ای معطری خلق می‌کند."
            )
        )

        // Concept 3: Creamy Pepper & Umami Fusion
        val c3Name = "مرینیت ماستی کرمی تند با عصاره دودی و فلفل سیاه M.K.A"
        concepts.add(
            GeneratedRecipeConcept(
                id = "concept_creamy_${System.currentTimeMillis()}_3",
                title = c3Name,
                englishTitle = "Creamy Spiced Pepper Umami Marinade",
                baseProtein = protein,
                baseProteinFa = protein,
                flavorProfileFa = "کرمی، فلفلی ملایم، آبدار و اسیدی ملایم",
                ingredients = listOf(
                    Ingredient("گوشت یا فیله $protein", 1000.0, "گرم"),
                    Ingredient("ماست چکیده پرچرب یا خامه ترش", 100.0, "گرم"),
                    Ingredient("سس مایونز درجه یک", 30.0, "گرم"),
                    Ingredient("سس سویا یا سس ووسترشر", 20.0, "میلی‌لیتر"),
                    Ingredient("آب لیمو ترش تازه", 20.0, "میلی‌لیتر"),
                    Ingredient("روغن کنجد یا آفتابگردان", 30.0, "میلی‌لیتر"),
                    Ingredient("فلفل سیاه تازه ساییده درشت", 8.0, "گرم"),
                    Ingredient("پودر پیاز و پاپریکا شیرین", 10.0, "گرم"),
                    Ingredient("نمک طعام استاندارد", 12.0, "گرم")
                ),
                prepSequence = listOf(
                    "۱. آماده‌سازی پایه لبنی: ماست چکیده و مایونز را با همزن مخلوط کنید تا بافتی ابریشمی پیدا کند.",
                    "۲. طعم‌دهی اومامی: سس سویا، لیمو ترش، پودر پیاز و فلفل سیاه درشت را اضافه نمایید.",
                    "۳. نمک‌سنجی: نمک را به میزان دقیق اضافه کرده و مخلوط را بچشید.",
                    "۴. کاور پروتئین: قطعات $protein را به طور یکنواخت درون مایه بخوابانید.",
                    "۵. استراحت: ۳ تا ۶ ساعت در یخچال خوابانده شود."
                ),
                marinationTime = "۳ تا ۶ ساعت",
                cookingMethod = "سیخ منقل زغالی یا فر گریل با فن کانوکشن",
                cookingTemp = "حرارت یکنواخت ملایم تا مغزپخت کامل",
                proTips = "لاکتیک اسید موجود در ماست آرام‌ترین و ایمن‌ترین نرم‌کننده طبیعی گوشت در صنعت قصابی است."
            )
        )

        return concepts
    }
}
