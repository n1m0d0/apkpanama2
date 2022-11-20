package diggate.xpertise.com.diggateapk;

public class obj_params2 {
    protected int id;
    protected String title;
    protected String text;
    protected String description;
    protected int control;
    protected boolean active;
    protected int idValueP;

    public obj_params2(int id, String title, String text, String description, int control) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.description = description;
        this.control = control;
        this.active = false;
        this.idValueP = 0;
    }

    public obj_params2(int id, String title, String text, String description, int control, int idValueP) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.description = description;
        this.control = control;
        this.active = false;
        this.idValueP = idValueP;
    }

    public obj_params2(int id, String title, String text, String description, int control, boolean active) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.description = description;
        this.control = control;
        this.active = active;
        this.idValueP = 0;
    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {

        return description;

    }

    public void setDescription(String description) {

        this.description = description;

    }

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getIdValueP() {
        return idValueP;
    }

    public void setIdValueP(int idValueP) {
        this.idValueP = idValueP;
    }
}
