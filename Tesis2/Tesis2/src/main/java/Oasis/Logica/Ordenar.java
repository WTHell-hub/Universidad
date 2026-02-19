package Oasis.Logica;

import Oasis.Datos.Actualizar;
import Oasis.Modelos.ProductoBase;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Ordenar {
    public static void tabla(String donde, DefaultTableModel model) {
        //Actualizamos la lista del almacén de forma que esté ordenado alfabéticamente
        ArrayList<ProductoBase> lista = new ArrayList<>();
        Actualizar.lista(lista, donde, "stock > 0");

        model.setRowCount(0);

        lista.sort((a, b) -> {
            String objetoA = a.getName();
            String objetoB = b.getName();

            return objetoA.compareToIgnoreCase(objetoB);
        });

        for (ProductoBase p: lista) {
            Object[] fila = {p.getName(), p.getPrecioC(), p.getPrecioV(), (int) p.getStock()};
            model.addRow(fila);
        }
    }
}
