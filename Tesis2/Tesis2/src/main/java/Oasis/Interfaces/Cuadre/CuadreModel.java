package Oasis.Interfaces.Cuadre;

import Oasis.Datos.Actualizar;
import Oasis.Modelos.ProductoBase;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Map;

public class CuadreModel {
    private ArrayList<ProductoBase> productos = new ArrayList<>();
    private Map<String, DefaultTableModel> models;
    private Integer idCuadreHistorial;
    

    public CuadreModel(String donde, String condicion, Map<String, DefaultTableModel> models, Integer idCuadreHistorial) {
        CargarDatos(donde, condicion);
        this.models = models;
        this.idCuadreHistorial = idCuadreHistorial;
    }

    public void CargarDatos(String donde, String condicion) {
        Actualizar.lista(productos, donde, condicion);

        //Se encarga de borrar los productos en 0 menos los cigarros sueltos, y en la 1.ª parte se agg !p.getTipo().isEmpty() porque cuando se cargan los datos desde el historial no existe 'tipo'
        productos.removeIf(p -> p.getStock() == 0 && !p.getTipo().equals("CigarroSuelto") && !p.getTipo().isEmpty() || !p.getName().startsWith("Cigarro") && p.getStock() == 0);
    }

    public void ActualizarLista(String donde, String condicion) {
        CargarDatos(donde, condicion);
    }

    public ArrayList<ProductoBase> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<ProductoBase> productos) {
        this.productos = productos;
    }

    public Map<String, DefaultTableModel> getModels() {
        return models;
    }

    public void setModels(Map<String, DefaultTableModel> models) {
        this.models = models;
    }

    public Integer getIdCuadreHistorial() {
        return idCuadreHistorial;
    }

    public void setIdCuadreHistorial(Integer idCuadreHistorial) {
        this.idCuadreHistorial = idCuadreHistorial;
    }
}