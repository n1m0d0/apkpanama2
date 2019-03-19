package gesport.xpertise.com.gesportapk;

public class obj_form {

    protected int id;
    protected String colorForm;
    protected String descriptionForm;
    protected String idIconForm;
    protected int positionForm;
    protected int typeDependency;

    public obj_form(int id, String colorForm, String descriptionForm, String idIconForm, int positionForm, int typeDependency) {

        this.id = id;
        this.colorForm = colorForm;
        this.descriptionForm = descriptionForm;
        this.idIconForm = idIconForm;
        this.positionForm = positionForm;
        this.typeDependency = typeDependency;

    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getColorForm() {

        return colorForm;

    }

    public void setColorForm(String colorForm) {

        this.colorForm = colorForm;

    }

    public String getDescriptionForm() {

        return descriptionForm;

    }

    public void setDescriptionForm(String descriptionForm) {

        this.descriptionForm = descriptionForm;

    }

    public String getIdIconForm() {

        return idIconForm;

    }

    public void setIdIconForm(String idIconForm) {

        this.idIconForm = idIconForm;

    }

    public int getPositionForm() {

        return positionForm;

    }

    public void setPositionForm(int positionForm) {

        this.positionForm = positionForm;

    }

    public int getTypeDependency() {

        return typeDependency;

    }

    public void setTypeDependency(int typeDependency) {

        this.typeDependency = typeDependency;

    }

}
