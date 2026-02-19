package Oasis.Interfaces;

import Oasis.Datos.Guardar;
import Oasis.Interfaces.Login.LoginController;
import Oasis.Interfaces.Login.LoginModel;
import Oasis.Interfaces.Login.LoginView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CrearUsuario extends JFrame {

    public CrearUsuario() {
        JLabel jLabel1 = new JLabel();
        JLabel txtNombre = new JLabel();
        JTextField nombre = new JTextField();
        JLabel txtPassword = new JLabel();
        JTextField password = new JTextField();
        JButton btnAceptar = new JButton();
        JButton btnCancelar = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 24)); // NOI18N
        jLabel1.setText("Nuevo Usuario");

        txtNombre.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 14)); // NOI18N
        txtNombre.setText("Nombre:");

        txtPassword.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 14)); // NOI18N
        txtPassword.setText("Password:");

        ActionListener agregar = (e -> {
            boolean ok = !nombre.getText().trim().isEmpty() && !password.getText().isEmpty();

            if (ok) {
                Guardar.usuario(nombre.getText(), password.getText());

                nombre.setText(" ");
                password.setText(" ");

                dispose();

                LoginModel model = new LoginModel();
                LoginView view = new LoginView();
                new LoginController(model, view);
                view.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "No pueden estar vacíos los parámetros", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(agregar);
        password.addActionListener(agregar);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnCancelar)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnAceptar))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(txtNombre, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(nombre, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                                                .addComponent(password)))))
                                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel1)
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtNombre)
                                        .addComponent(nombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPassword)
                                        .addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAceptar)
                                        .addComponent(btnCancelar))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

