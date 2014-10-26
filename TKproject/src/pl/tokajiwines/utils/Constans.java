
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
            "5 km", "10 km", "15 km", "20 km"
    };

    public static final String[] sMapRangeMil = {
            "3 mile", "6 mile", "9 mile", "12 mile"
    };

    // TODO nie wiem jak sprawdzic czy dobrze mi wyszlo.
    public static double sKmToLat = 0.0134;
    public static double sKmToLong = 0.0082;
    public static double sKmInDegreeAvg = 0.0108;

    public static final double[] sMapRadiusInKm = {
            0.054, 0.108, 0.162, 0.216

    };

    //Nie potrzebne ale jest 
    public static double sMilToLat = 0.08375;
    public static double sMilToLong = 0.005125;
    public static double sMilInDegreeAvg = 0.0444375;

    public static final String sUsername = "zpi";

    public static final String sPassword = "OEmGie";

    //Settings
    public static final String[] sSettingsLanguage = {
            "Polski", "English"
    };

    public static final String[] sSettingsCurrency = {
            "PLN", "Euro", "Forint"
    };

    public static final String[] sSettingsRange = {
            "5km", "10km", "15km", "20km", "25km"
    };

    public static final int[] sSettingsMeasure = {
            R.string.distance_measure_km, R.string.distance_measure_mph
    };

    //Guide 
    //TODO do stringow
    public static final String[] sGuideTabTitle = new String[] {
            "Curiosities", "Hotels", "Restaurants"
    };
    public static final int[] sGuideTabIcons = new int[] {
            R.drawable.ic_guide_curiosities, R.drawable.ic_guide_hotels,
            R.drawable.ic_guide_restaurants

    };

}
