package Oasis.Interfaces;

import Oasis.Datos.GestorBD;
import Oasis.ListasEnlazadas.ListaEnlazada;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class IEstadisticas extends JDialog {
    private ListaEnlazada<Double> dineroInvertido = new ListaEnlazada<>();
    private ListaEnlazada<Double> dineroGanancia = new ListaEnlazada<>();

    public IEstadisticas(JFrame parent, boolean bloquear) {
        super(parent, bloquear);

        // Botón atrás
        JButton btnAtras = new JButton("<-");
        btnAtras.addActionListener(e -> dispose());

        // Panel para el gráfico
        JPanel panelGraficoPastel = new JPanel(new BorderLayout());

        CalcularInversionPorProducto();

        JLabel labelInversion = new JLabel("Inversion estancada: "+ DineroTotalInvertido(0));
        JLabel labelGanancia = new JLabel("Ganancia estancada: "+ DineroTotalGanancia(0));


        // Agregar el grafico de pastel al panel de la interface
        panelGraficoPastel.add(GraficoLinea(), BorderLayout.CENTER);

        // Alineación de los componentes creados por NetBeans
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnAtras, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(680, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(114, 114, 114)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelInversion, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelGanancia, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panelGraficoPastel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(158, 158, 158))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnAtras)
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelGraficoPastel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(labelInversion)
                                                .addGap(18, 18, 18)
                                                .addComponent(labelGanancia)))
                                .addContainerGap(387, Short.MAX_VALUE))
        );


        // Configuración del diálogo
        setUndecorated(true);
        pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dimension);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void CalcularInversionPorProducto() {
        try {
            Connection conn = GestorBD.getConn();

            PreparedStatement stmtTienda = conn.prepareStatement(
                    "SELECT pCompra, pVenta, stock FROM producto_tienda"
            );
            PreparedStatement stmtAlmacen = conn.prepareStatement(
                    "SELECT pCompra, pVenta, stock FROM producto_almacen"
            );

            ResultSet rsTienda = stmtTienda.executeQuery();
            ResultSet rsAlmacen = stmtAlmacen.executeQuery();

            while (rsTienda.next()) {
                dineroInvertido.add(rsTienda.getDouble("pCompra") * rsTienda.getDouble("stock"));
                dineroGanancia.add(rsTienda.getDouble("pVenta") * rsTienda.getDouble("stock"));
            }
            while (rsAlmacen.next()) {
                dineroInvertido.add(rsAlmacen.getDouble("pCompra") * rsAlmacen.getDouble("stock"));
                dineroGanancia.add(rsAlmacen.getDouble("pVenta") * rsAlmacen.getDouble("stock"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double DineroTotalInvertido(int i) {
        if (i >= dineroInvertido.getSize()) {
            return 0.0;
        }

        return dineroInvertido.get(i) + DineroTotalInvertido(i+1);
    }

    public Double DineroTotalGanancia(int i) {
        if (i >= dineroGanancia.getSize()) {
            return 0.0;
        }

        return dineroGanancia.get(i) + DineroTotalGanancia(i+1);
    }

    // Forma para cargar datos desde la BD
    private static DefaultPieDataset<String> cargarDatosDesdeBD() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        try (Connection conn = GestorBD.getConn();
             PreparedStatement stmt = conn.prepareStatement("SELECT nombre, cantidad_vendida FROM producto");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad_vendida");
                dataset.setValue(nombre, cantidad);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }

    // Crear el Gráfico de Pastel en sí
    private ChartPanel GraficoPastel() {
        // Dataset desde la base de datos
        DefaultPieDataset<String> dataset = cargarDatosDesdeBD();

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Productos vendidos",
                dataset,
                false, true, true
        );

        @SuppressWarnings("unchecked")
        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setForegroundAlpha(0.8f);
        plot.setLabelGenerator(null);
        plot.setOutlineVisible(false);

        chart.setBackgroundPaint(Color.DARK_GRAY);

        // Panel del gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        return chartPanel;
    }

    private JPanel GraficoLinea() {
        DefaultCategoryDataset dataset = cargarDatosLineasDesdeBD();

        JFreeChart chart = ChartFactory.createLineChart(
                null, // sin título
                null, // sin eje X
                null, // sin eje Y
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true, // activamos tooltips
                false
        );

        // Tema oscuro
        chart.setBackgroundPaint(Color.DARK_GRAY);
        chart.setBorderVisible(false);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setOutlineVisible(false);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);

        plot.getDomainAxis().setTickLabelsVisible(true);
        plot.getRangeAxis().setTickLabelsVisible(false);
        plot.getDomainAxis().setTickLabelPaint(Color.WHITE);

        // Renderer con efecto glow
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 180, 255)); // azul neón
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));

        // Panel minimalista
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(false);
        panel.setOpaque(false);
        panel.setBorder(null);
        panel.setBackground(Color.BLACK);

        return panel;
    }

    private static DefaultCategoryDataset cargarDatosLineasDesdeBD() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = GestorBD.getConn();
             PreparedStatement stmt = conn.prepareStatement("SELECT fecha, Dinero FROM cuadre ORDER BY fecha ASC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Date valorFecha = rs.getDate("fecha");
                double dinero = rs.getDouble("Dinero");

                SimpleDateFormat format = new SimpleDateFormat("dd/MM");
                String fecha = format.format(valorFecha);

                dataset.setValue(dinero, "Serie1", fecha);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }

}