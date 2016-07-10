package crimespotter.rhokpta.unisa.co.za.crimespotter.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kgundula on 16/07/09.
 */
public class CrimeHotspot {

    private String crimeType;
    private String timeStamp;
    private double latitude;
    private double longitude;


    public CrimeHotspot() {
    }

    public CrimeHotspot(String crimeType, String timeStamp, double latitude, double longitude) {
        this.crimeType = crimeType;
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("crime_type", crimeType);
        result.put("timestamp", timeStamp);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }
}
