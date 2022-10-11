package diggate.xpertise.com.diggateapk;

public class obj_params {
    protected int id;
    protected String description;
    protected int control;
    protected boolean active;

    public obj_params(int id, String description, int control) {
        this.id = id;
        this.description = description;
        this.control = control;
        this.active = false;
    }

    public obj_params(int id, String description, int control, boolean active) {
        this.id = id;
        this.description = description;
        this.control = control;
        this.active = active;
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
}
