package gesport.xpertise.com.gesportapk;

public class obj_preReg {
    protected int id;
    protected String colorBackground;
    protected String description;
    protected String icon;

    public obj_preReg(int id, String colorBackground, String description, String icon) {
        this.id = id;
        this.colorBackground = colorBackground;
        this.description = description;
        this.icon = icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setColorBackground(String colorBackground) {
        this.colorBackground = colorBackground;
    }

    public String getColorBackground() {
        return colorBackground;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
