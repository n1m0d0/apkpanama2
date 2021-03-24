package diggate.xpertise.com.diggateapk;

public class obj_params {
    protected int id;
    protected String description;
    protected int control;

    public obj_params(int id, String description, int control) {

        this.id = id;
        this.description = description;
        this.control = control;

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
}
