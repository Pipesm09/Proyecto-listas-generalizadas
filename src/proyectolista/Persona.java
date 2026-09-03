package proyectolista;

public class Persona {

    private String cedula;
    private String fechaNacimiento;
    private String nombre;

    public Persona(String cedula, String fechaNacimiento, String nombre) {
        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

}
