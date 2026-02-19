package Oasis.Estadistica;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;

public class Estadisticas extends JFrame{
    public Estadisticas() {
        // Dataset (datos)
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Café", 40);
        dataset.setValue("Té", 25);
        dataset.setValue("Jugos", 35);

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Ventas por categoría", // título
                dataset,                // datos
                true,                   // leyenda
                true,                   // tooltips
                false                   // URLs
        );

        // Panel para mostrarlo en Swing
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}