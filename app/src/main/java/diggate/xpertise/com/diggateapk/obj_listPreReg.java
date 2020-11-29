package diggate.xpertise.com.diggateapk;

public class obj_listPreReg {
    protected int id;
    protected String description;
    protected String listColor;
    protected String listIcon;
    protected String keyValue;
    protected String stringValue;

    public obj_listPreReg(int id, String description, String listColor, String listIcon, String keyValue, String stringValue) {
        this.id = id;
        this.description = description;
        this.listColor = listColor;
        this.listIcon = listIcon;
        this.keyValue = keyValue;
        this.stringValue = stringValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListColor() {
        return listColor;
    }

    public void setListColor(String listColor) {
        this.listColor = listColor;
    }

    public String getListIcon() {
        return listIcon;
    }

    public void setListIcon(String listIcon) {
        this.listIcon = listIcon;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
