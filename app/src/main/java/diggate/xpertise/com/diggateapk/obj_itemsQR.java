package diggate.xpertise.com.diggateapk;

public class obj_itemsQR {
    protected int idAuth;
    protected String binaryFP;
    protected int used;
    protected String urlData;
    protected String descFP;
    protected String descFP1;
    protected String descFP2;

    public obj_itemsQR(int idauth, String binaryfp, int used, String urlData, String descfp, String descfp1, String descfp2) {
        this.idAuth = idauth;
        this.binaryFP = binaryfp;
        this.used = used;
        this.urlData = urlData;
        this.descFP = descfp;
        this.descFP1 = descfp1;
        this.descFP2 = descfp2;
    }

    public int getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(int idAuth) {
        this.idAuth = idAuth;
    }

    public String getBinaryFP() {
        return binaryFP;
    }

    public void setBinaryFP(String binaryFP) {
        this.binaryFP = binaryFP;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getUrlData() {
        return urlData;
    }

    public void setUrlData(String urlData) {
        this.urlData = urlData;
    }

    public String getDescFP() {
        return descFP;
    }

    public void setDescFP(String descFP) {
        this.descFP = descFP;
    }

    public String getDescFP1() {
        return descFP1;
    }

    public void setDescFP1(String descFP1) {
        this.descFP1 = descFP1;
    }

    public String getDescFP2() {
        return descFP2;
    }

    public void setDescFP2(String descFP2) {
        this.descFP2 = descFP2;
    }
}
