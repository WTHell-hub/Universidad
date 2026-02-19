package Oasis.Modelos;

public class ProductoBase {
    protected String name;
    protected double precioC;
    protected double precioV;
    protected double stock;
    protected String tipo;
    protected Integer cantidad;

    public ProductoBase(String name, double precioC, double precioV, double stock, String tipo) {
        this.name = name;
        this.precioC = precioC;
        this.precioV = precioV;
        this.stock = stock;
        this.tipo = tipo;
        this.cantidad = null;
    }

    public ProductoBase(String name, double precioC, double precioV, double stock, String tipo, Integer cantidad) {
        this.name = name;
        this.precioC = precioC;
        this.precioV = precioV;
        this.stock = stock;
        this.tipo = tipo;
        this.cantidad = cantidad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrecioC() {
        return precioC;
    }

    public double getPrecioV() {
        return precioV;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Almacen{" +
                "name='" + name + '\'' +
                ", stock=" + stock +
                '}';
    }
}