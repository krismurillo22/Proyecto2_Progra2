package proyecto2_miniwindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import java.io.File;

public class InicioVisual extends JFrame {

    EscritorioVisual escritorio;
    SistemaArchivos sistemaArchivos = new SistemaArchivos();  // Instancia para manejar archivos y usuarios
    
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

        // Botón para agregar nuevo usuario
        JButton agregarUsuarioButton = new JButton("Agregar Usuario");
        agregarUsuarioButton.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 4; // Coloca el botón debajo del de iniciar sesión
        panel.add(agregarUsuarioButton, gbc);

        // Lógica de login
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
            // Verificación de usuario en el sistema de archivos
            if (sistemaArchivos.verificarUsuario(username, password)) {
                // Aquí debes pasar el nombre de usuario al escritorio
                escritorio = new EscritorioVisual(username);  
                escritorio.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
});


        // Lógica para agregar un nuevo usuario
        agregarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar un cuadro de diálogo para pedir el nombre y contraseña del nuevo usuario
                String nuevoUsuario = JOptionPane.showInputDialog("Ingrese el nombre del nuevo usuario:");
                String nuevaContraseña = JOptionPane.showInputDialog("Ingrese la contraseña del nuevo usuario:");

                // Verificar que no estén vacíos
                if (nuevoUsuario != null && nuevaContraseña != null && !nuevoUsuario.trim().isEmpty() && !nuevaContraseña.trim().isEmpty()) {
                    // Crear usuario y sus carpetas
                    if (!sistemaArchivos.usuarioExiste(nuevoUsuario)) {
                        sistemaArchivos.crearUsuario(nuevoUsuario, nuevaContraseña);  // Crear las carpetas del usuario
                        JOptionPane.showMessageDialog(null, "Usuario " + nuevoUsuario + " creado con éxito.");
                    } else {
                        JOptionPane.showMessageDialog(null, "El usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: El nombre de usuario o la contraseña no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    // Clase para gestionar el campo de texto con placeholder
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

    // Clase para gestionar el campo de contraseña con placeholder
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
