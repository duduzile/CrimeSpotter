package crimespotter.rhokpta.unisa.co.za.crimespotter.model;

/**
 * Created by kgundula on 16/07/09.
 */
public class CrimeHotspot {

    private String crimeType;
    private String timeStamp;
    private Object location;

    public CrimeHotspot() {
    }

    public CrimeHotspot(String crimeType, String timeStamp, Object location) {
        this.crimeType = crimeType;
        this.timeStamp = timeStamp;
        this.location = location;
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

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }
}
