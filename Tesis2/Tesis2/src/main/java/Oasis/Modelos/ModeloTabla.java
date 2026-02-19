package Oasis.Modelos;

public class ModeloTabla {
    private String nombre;
    private int cuadre_anterior;
    private int cuadre_posterior;
    private int cantVendida;


    public ModeloTabla(String nombre, int cuadre_anterior, int cuadre_posterior, int cantVendida) {
        this.nombre = nombre;
        this.cuadre_anterior = cuadre_anterior;
        this.cuadre_posterior = cuadre_posterior;
        this.cantVendida = cantVendida;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCuadre_anterior() {
        return cuadre_anterior;
    }

    public void setCuadre_anterior(int cuadre_anterior) {
        this.cuadre_anterior = cuadre_anterior;
    }

    public int getCuadre_posterior() {
        return cuadre_posterior;
    }

    public void setCuadre_posterior(int cuadre_posterior) {
        this.cuadre_posterior = cuadre_posterior;
    }

    public int getCantVendida() {
        return cantVendida;
    }

    public void setCantVendida(int cantVendida) {
        this.cantVendida = cantVendida;
    }
}
