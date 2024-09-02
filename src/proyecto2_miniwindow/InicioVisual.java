/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2_miniwindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;

/**
 *
 * @author User
 */
public class InicioVisual extends JFrame {
    
    EscritorioVisual escritorio;
    public InicioVisual() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel() {
            private Image fondo;

            {
                try {
                    fondo = new ImageIcon("src/Iconos/Fondo.png").getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibuja la imagen de fondo
                if (fondo != null) {
                    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new GridBagLayout());
        getContentPane().add(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        ImageIcon originalIcon = new ImageIcon("src/Iconos/PerfilInicio.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel profileIcon = new JLabel(scaledIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(profileIcon, gbc);

        PlaceholderTextField usernameField = new PlaceholderTextField("Usuario");
        usernameField.setFont(new Font("Arial", Font.ITALIC, 24));
        Dimension fieldSize = new Dimension(300, 40); 
        usernameField.setPreferredSize(fieldSize);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);
        PlaceholderPasswordField passwordField = new PlaceholderPasswordField("Contraseña");
        passwordField.setFont(new Font("Arial", Font.ITALIC, 24));
        passwordField.setPreferredSize(fieldSize); 
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                
                // Verifica si ambos campos están vacíos
                if (username.isEmpty() && password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese su usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese su usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese su contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    escritorio = new EscritorioVisual();
                    escritorio.setVisible(true);
                    dispose();
                }
            }
        });
        
        setVisible(true);
    }

    class PlaceholderTextField extends JTextField {
        private String placeholder;
        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setText(placeholder);
            setForeground(Color.GRAY);
            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }
    }
    
    class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;

        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setText(placeholder);
            setEchoChar((char) 0);
            setForeground(Color.GRAY);
            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (new String(getPassword()).equals(placeholder)) {
                        setText("");
                        setEchoChar('•');
                        setForeground(Color.BLACK);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (getPassword().length == 0) {
                        setText(placeholder);
                        setEchoChar((char) 0);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InicioVisual());
    }
}
