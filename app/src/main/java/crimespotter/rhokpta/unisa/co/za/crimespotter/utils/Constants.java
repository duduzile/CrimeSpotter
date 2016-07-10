package crimespotter.rhokpta.unisa.co.za.crimespotter.utils;

import crimespotter.rhokpta.unisa.co.za.crimespotter.BuildConfig;

/**
 * Created by kgundula on 16/07/09.
 */
public class Constants {


    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;


    public static final String FIREBASE_LOCATION_CRIME_HOTSPOT = "crime_hotspots";


    public static final String FIREBASE_URL_CRIME_HOTSPOTS = FIREBASE_URL + "/" + FIREBASE_LOCATION_CRIME_HOTSPOT;

    /*
    *  Constants for bundles, extras and shared preferences keys
    *
    */

    public static final String KEY_SIGNUP_EMAIL = "SIGNUP_EMAIL";
    public static final String KEY_PROVIDER = "PROVIDER";
    public static final String KEY_ENCODED_EMAIL = "ENCODED_EMAIL";
    public static final String KEY_GOOGLE_EMAIL = "GOOGLE_EMAIL";
    public static final String KEY_BOARDING_SCREEN = "BOARDING_SCREEN";
    public static final String PREF_USER_FIRST_TIME = "user_first_time";

    /**
     * Constants for Firebase login
     */
    public static final String PASSWORD_PROVIDER = "password";
    public static final String GOOGLE_PROVIDER = "google";
    public static final String PROVIDER_DATA_DISPLAY_NAME = "displayName";

    public static final int REQUEST_LOCATION = 0;


}
