package gesport.xpertise.com.gesportapk;

public class obj_auth {
    protected int idauth;
    protected String binaryfp;
    protected String descfp;

    public obj_auth(int idauth, String binaryfp, String descfp) {
        this.idauth = idauth;
        this.binaryfp = binaryfp;
        this.descfp = descfp;
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
}
