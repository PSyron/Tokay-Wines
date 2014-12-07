
package pl.tokajiwines.utils;

import pl.tokajiwines.R;

public class Constans {

    public static final int[] sRandomPhotos = {
        R.drawable.ic_launcher
    };

    public static final int sNumberOfOptions = 6;

    public static final int[] sMenuTitles = {
            R.string.title_news, R.string.title_wines, R.string.title_wineyards,
            R.string.title_map, R.string.title_tour, R.string.title_settings
    };

    public static final int[] sMenuIcons = {
            R.drawable.menu_item_selector_news, R.drawable.menu_item_selector_wines,
            R.drawable.menu_item_selector_producers, R.drawable.menu_item_selector_map,
            R.drawable.menu_item_selector_guide, R.drawable.menu_item_selector_settings
    };

    public static final String[] sMapRangeKm = {
            "5 km", "10 km", "15 km", "20 km", "25 km"
    };

    public static final String[] sMapRangeMil = {
            "3 mile", "6 mile", "9 mile", "12 mile"
    };

    // TODO nie wiem jak sprawdzic czy dobrze mi wyszlo.
    public static double sKmToLat = 0.0134;
    public static double sKmToLong = 0.0082;
    public static double sKmInDegreeAvg = 0.0108;

    public static final double[] sMapRadiusInKm = {
            0.054, 0.108, 0.162, 0.216, 0.27

    };

    //Nie potrzebne ale jest 
    public static double sMilToLat = 0.08375;
    public static double sMilToLong = 0.005125;
    public static double sMilInDegreeAvg = 0.0444375;

    //WineFilter

    public static final String[] sWineType = {
            "Szamorodni Édes", "Szamorodni Szaraz", "Késői Szüret", "Aszú", "Aszúeszencia",
            "Eszencia", "Fordítás", "Máslás"
    };

    public static final String[] sWineStrain = {
            "Furmint", "Hárslevelű", "Sárga Muskotály", "Zéta", "Kabar", "Kövérszőlő"
    };
    public static final String[] sWinePricesPLN = {
            "< 2000 ft (30 zł)", "2000 - 4000 ft (30 - 60 zł)",
            "4000 - 8000 ft (60 - 120 zł)", "> 8000 ft (120 zł)"
    };
    public static final String[] sWinePricesEuro = {
            "< 2000 ft (7 €)", "2000 - 4000 ft (7 - 14 €)", "4000 - 8000 ft (14 - 28 €)",
            "> 8000 ft (28 €)"
    };
    public static final String[] sWinePricesForint = {
            "< 2000 ft", "2000 - 4000 ft", "4000 - 8000 ft", "> 8000 ft"
    };

    public static final String[] sWinePricesQuery = {
            "Price <= 2000", "(Price > 2000 AND Price <= 4000)",
            "(Price > 4000 AND Price <= 8000)", "Price > 8000"
    };

    //Settings
    public static final String[] sSettingsLanguage = {
            "Polski", "English"
    };

    public static final String[] sSettingsCurrency = {
            "PLN", "Euro", "Forint"
    };
    
    // ratio for currencies (forint/currency)
    public static final float[] sCurrencyRatio = {
        0.01354f, 0.00326f, 1.f
    };
    
    public static final String[] sCurrencyShorts = {
        "zł", "€", "ft"
    };

    public static final String[] sSettingsRange = {
            "5km = 3.1mil", "10km = 6.2mil", "15km = 9.3mil", "20km = 12.4mil", "25km = 15.5mil"
    };

    public static final int[] sSettingsMeasure = {
            R.string.distance_measure_km, R.string.distance_measure_miles
    };

    //Guide 
    public static final int[] sGuideTabTitle = new int[] {
            R.string.tab_tours, 
            R.string.hotels, R.string.restaurants
    };
    public static final int[] sGuideTabIcons = new int[] {
                    R.drawable.menu_item_selector_map, 
            R.drawable.guide_hotels_selector, R.drawable.guide_restaurants_selector

    };

}
