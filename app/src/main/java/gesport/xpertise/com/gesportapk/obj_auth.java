package gesport.xpertise.com.gesportapk;

public class obj_auth {
    protected int idauth;
    protected String binaryfp;
    protected String descfp;
    protected String urlData;

    public obj_auth(int idauth, String binaryfp, String descfp, String urlData) {
        this.idauth = idauth;
        this.binaryfp = binaryfp;
        this.descfp = descfp;
        this.urlData = urlData;
    }

    public void setIdauth(int idauth) {
        this.idauth = idauth;
    }

    public int getIdauth() {
        return idauth;
    }

    public void setBinaryfp(String binaryfp) {
        this.binaryfp = binaryfp;
    }

    public String getBinaryfp() {
        return binaryfp;
    }

    public void setDescfp(String descfp) {
        this.descfp = descfp;
    }

    public String getDescfp() {
        return descfp;
    }

    public void setUrlData(String urlData) {
        this.urlData = urlData;
    }

    public String getUrlData() {
        return urlData;
    }
}
