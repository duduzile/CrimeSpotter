package crimespotter.rhokpta.unisa.co.za.crimespotter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import crimespotter.rhokpta.unisa.co.za.crimespotter.model.CrimeHotspot;
import crimespotter.rhokpta.unisa.co.za.crimespotter.utils.Constants;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Context context;
    DatabaseReference dataRef;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    Button btnCrimeSpot;
    String crimeType = "";
    JSONObject locationObject;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        linearLayout = (LinearLayout)findViewById(R.id.main_layout);

        dataRef = FirebaseDatabase.getInstance().getReference();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        btnCrimeSpot = (Button) findViewById(R.id.btnCrimeSpot);
        btnCrimeSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationObject = new JSONObject();
                try {
                    locationObject.put("latitude", mLastLocation.getLatitude());
                    locationObject.put("longitude", mLastLocation.getLongitude());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                long unixTime = System.currentTimeMillis() / 1000L;

                final String timmeStamp = String.valueOf(unixTime);

                final String[] crime_hotspots_array = getResources().getStringArray(R.array.crime_hotspots_array);

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.title_report_crime)
                .setItems(crime_hotspots_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        crimeType = crime_hotspots_array[i];
                        reportCrime(timmeStamp);
                    }
                });

                builder.show();


            }
        });

    }

    public void reportCrime(String timeStamp) {
        CrimeHotspot crimeHotspot = new CrimeHotspot(crimeType,timeStamp, mLastLocation.getLatitude() , mLastLocation.getLongitude());
        assert crimeHotspot != null;
        Log.i("Ygritte", "Which Crime : "+crimeHotspot.getCrimeType()+" , Time Stamp : "+ timeStamp);

        Map<String, Object> crimeHotspotMap = crimeHotspot.toMap();
        HashMap<String, Object> hotspotMap = new HashMap<String, Object>();



        String key = dataRef.child(Constants.FIREBASE_LOCATION_CRIME_HOTSPOT).push().getKey();
        hotspotMap.put("/" + Constants.FIREBASE_LOCATION_CRIME_HOTSPOT+"/"+key , crimeHotspotMap);

        dataRef.updateChildren(hotspotMap);
        showSnackBar("Crime Reported");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(linearLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
        } else if ( id == R.id.action_map ) {
            navigateToMap();
        }


        return super.onOptionsItemSelected(item);
    }
    /**
     * Logs out the user from their current session and starts LoginActivity.
     * Also disconnects the mGoogleApiClient if connected and provider is Google
     */
    protected void logout() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public void navigateToMap () {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Location Permissions")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION);
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION);
            }
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {



            } else {
                Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
