package Oasis.Logica;

import Oasis.Modelos.ProductoBase;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ProductoTableModel extends AbstractTableModel {
    private ArrayList<ProductoBase> lista;
    private String[] columnas;

    public ProductoTableModel(ArrayList<ProductoBase> lista, String[] columnas) {
        this.lista = lista;
        this.columnas = columnas;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }


    //En este código, estoy tratando de devolver los valores referenciando el nombre de la columna, pero no funciona para él cuadre, no carga nada ni da error
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductoBase producto = lista.get(rowIndex);
        String nombreCol = getColumnName(columnIndex);

        switch(nombreCol) {
            case "Nombre" : return producto.getName() + " -> (" + (int) producto.getStock() + ")";
            case "Cantidad" : return producto.getCantidad() == null ? "" : producto.getCantidad();
            case "Stock" : return producto.getStock(); //No se esta utilizando
            default: throw new IndexOutOfBoundsException("Indice fuera de rango");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ProductoBase producto = lista.get(rowIndex);

        switch (columnIndex) {
            case 0: producto.setName((String) aValue); break;
            case 1: {
                producto.setCantidad(Integer.parseInt(aValue.toString()));
                break;
            }
            default: throw new IndexOutOfBoundsException("Index fuera de rango");
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    public ProductoBase getProductoAt(int rowIndex) {
        return lista.get(rowIndex);
    }
}