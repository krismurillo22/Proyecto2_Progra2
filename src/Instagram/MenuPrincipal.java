/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

/**
 *
 * @author User
 */

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame{
    InstagramVisual insta;
    public MenuPrincipal() {
        setTitle("Iniciar sesión");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Instagram");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        ImageIcon userIcon = new ImageIcon(getClass().getResource("/Iconos/Insta.png"));
        Image image = userIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        userIcon = new ImageIcon(image);
        imageLabel.setIcon(userIcon);
        add(imageLabel, gbc);

        JLabel usernameLabel = new JLabel("Usuario:");
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 30));
        gbc.gridy = 3;
        add(usernameField, gbc);
        
        JLabel passwordLabel = new JLabel("Contraseña:");
        gbc.gridy = 4;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 30));
        gbc.gridy = 5;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setBackground(new Color(177, 22, 110));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(200, 30));
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso", "Login", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            insta = new InstagramVisual();
            insta.setVisible(true);
        });

        JLabel registerLabel = new JLabel("¿No tienes una cuenta?");
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(registerLabel, gbc);

        JButton registerButton = new JButton("Regístrate");
        registerButton.setBorderPainted(false);
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(new Color(177, 22, 110));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            dispose();
            Registrarse registerGUI = new Registrarse();
            registerGUI.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal loginRegisterGUI = new MenuPrincipal();
            loginRegisterGUI.setVisible(true);
        });
    }
}
