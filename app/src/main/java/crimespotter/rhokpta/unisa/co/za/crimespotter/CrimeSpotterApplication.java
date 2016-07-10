package crimespotter.rhokpta.unisa.co.za.crimespotter;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kgundula on 16/07/10.
 */
public class CrimeSpotterApplication extends Application {

    public String LOG_TAG  = CrimeSpotterApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Log.d(LOG_TAG, FirebaseDatabase.getInstance().toString());
        } catch (Exception e) {
            Log.w(LOG_TAG, "SetPresistenceEnabled:Fail" + FirebaseDatabase.getInstance().toString());
            e.printStackTrace();
        }
    }
}
