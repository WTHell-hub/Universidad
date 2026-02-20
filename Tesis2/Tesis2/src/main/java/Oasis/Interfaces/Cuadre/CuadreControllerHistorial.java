package Oasis.Interfaces.Cuadre;

import Oasis.Datos.Actualizar;
import Oasis.Datos.GestorBD;
import Oasis.Datos.Guardar;
import Oasis.Logica.Controladora;
import Oasis.Modelos.ModeloTabla;
import Oasis.Modelos.ProductoBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class CuadreControllerHistorial extends CuadreController{
    public CuadreControllerHistorial(CuadreView view, CuadreModel model) {
        super(view, model);

        //Esta consulta a la base de datos es para agg los precios de venta al model, ya que al cargar los datos desde la tabla de productos no está la columna pVenta, por lo que se buscan los
        //datos de la tabla producto_tienda
        try {
            Connection conn = GestorBD.getConn();

            for (ProductoBase p: model.getProductos()) {

                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT pVenta FROM producto_tienda WHERE nombre = ?"
                );

                stmt.setString(1, p.getName());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    p.setPrecioV(rs.getDouble("pVenta"));
                } else {
                    JOptionPane.showMessageDialog(null, "hubo un error al cargar los precios de los productos", "Warning", JOptionPane.ERROR_MESSAGE);
                    throw new SQLException();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean SeHizoCuadre() {
        return false;
    }

    @Override
    public void ActualizarDB(Map<String, DefaultTableModel> models, Integer idCuadreHistorial) {
        for (ProductoBase p : model.getProductos()) {
            Controladora.UpdateProductos(p.getName(), p.getCantidad(),  (int) (p.getStock() - p.getCantidad()), idCuadreHistorial);
            Controladora.UpdateAfterCuadre(p.getName(), p.getCantidad());
        }

        Actualizar.modeloTabla(models.getOrDefault("ModelCuadre", null), "cuadre");
        Actualizar.modeloTabla(models.getOrDefault("ModelProducto", null), "producto", "id = " + idCuadreHistorial);
    }

    @Override
    public void ModoGuardado(ArrayList<ModeloTabla> modeloAGuardar, double faltante, double dineroTotal, Integer idCuadreHistorial) {
        Guardar.ExecuteUpdate(modeloAGuardar, dineroTotal, faltante, idCuadreHistorial);
    }
}