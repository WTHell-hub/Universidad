package Oasis.Interfaces.Cuadre;

import Oasis.Datos.Actualizar;
import Oasis.Datos.Guardar;
import Oasis.Logica.Controladora;
import Oasis.Modelos.ModeloTabla;
import Oasis.Modelos.ProductoBase;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Map;

public class CuadreControllerHistorial extends CuadreController{
    public CuadreControllerHistorial(CuadreView view, CuadreModel model) {
        super(view, model);
    }

    @Override
    public boolean SeHizoCuadre() {
        return false;
    }

    @Override
    public void ActualizarDB(Map<String, DefaultTableModel> models, Integer idCuadreHistorial) {
        for (ProductoBase p : model.getProductos()) {
            Controladora.UpdateProductos(p.getName(), p.getCantidad(), idCuadreHistorial);
        }

        Actualizar.modeloTabla(models.getOrDefault("ModelCuadre", null), "cuadre");
        Actualizar.modeloTabla(models.getOrDefault("ModelProducto", null), "producto", "id = " + idCuadreHistorial);
    }

    @Override
    public void ModoGuardado(ArrayList<ModeloTabla> modeloAGuardar, double faltante, double dineroTotal, Integer idCuadreHistorial) {
        Guardar.ExecuteUpdate(modeloAGuardar, dineroTotal, faltante, idCuadreHistorial);
    }
}