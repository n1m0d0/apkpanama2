package gesport.xpertise.com.gesportapk;

public class obj_events {

    protected int id;
    protected String variable;
    protected String fecha_inicio;
    protected String fecha_fin;
    protected int id_from;
    protected int numero_persona;
    protected int numero_container;
    protected int estado_evento;
    protected String colorForm;
    protected String idIconForm;

    public obj_events(int id, String variable, String fecha_inicio, String fecha_fin, int id_from, int numero_persona, int numero_container, int estado_evento, String colorForm, String idIconForm) {

        this.id = id;
        this.variable = variable;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.id_from = id_from;
        this.numero_persona = numero_persona;
        this.numero_container = numero_container;
        this.estado_evento = estado_evento;
        this.colorForm = colorForm;
        this.idIconForm = idIconForm;

    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getVariable() {

        return variable;

    }

    public void setVariable(String variable) {

        this.variable = variable;

    }

    public String getFecha_inicio() {

        return fecha_inicio;

    }

    public void setFecha_inicio(String fecha_inicio) {

        this.fecha_inicio = fecha_inicio;

    }

    public String getFecha_fin() {

        return fecha_fin;

    }

    public void setFecha_fin(String fecha_fin) {

        this.fecha_fin = fecha_fin;

    }

    public int getId_from() {

        return id_from;

    }

    public void setId_from(int id_from) {

        this.id_from = id_from;

    }

    public int getNumero_persona() {

        return numero_persona;

    }

    public void setNumero_persona(int numero_persona) {

        this.numero_persona = numero_persona;

    }

    public int getNumero_container() {

        return numero_container;

    }

    public void setNumero_container(int numero_container) {

        this.numero_container = numero_container;

    }

    public int getEstado_evento() {

        return estado_evento;

    }

    public void setEstado_evento(int estado_evento) {

        this.estado_evento = estado_evento;

    }

    public String getColorForm() {
        return colorForm;
    }

    public void setColorForm(String colorForm) {
        this.colorForm = colorForm;
    }

    public String getIdIconForm() {
        return idIconForm;
    }

    public void setIdIconForm(String idIconForm) {
        this.idIconForm = idIconForm;
    }
}
