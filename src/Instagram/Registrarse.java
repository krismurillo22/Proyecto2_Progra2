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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Registrarse extends JFrame{
    private JLabel profilePictureLabel;

    FuncionesUsers funciones;
    public Registrarse() {
        funciones = new FuncionesUsers();
        setTitle("Regístrate");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título de Registro
        JLabel titleLabel = new JLabel("              Regístrate:              ", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Etiqueta de Nombre completo
        JLabel nameLabel = new JLabel("Nombre completo:");
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameLabel, gbc);

        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(300, 30));
        gbc.gridy = 2;
        add(nameField, gbc);

        JLabel genderLabel = new JLabel("Género:");
        gbc.gridy = 3;
        add(genderLabel, gbc);

        String[] genderOptions = {"M", "F"};
        JComboBox<String> genderComboBox = new JComboBox<>(genderOptions);
        genderComboBox.setPreferredSize(new Dimension(300, 30));
        gbc.gridy = 4;
        add(genderComboBox, gbc);

        JLabel usernameLabel = new JLabel("Nombre de usuario:");
        gbc.gridy = 5;
        add(usernameLabel, gbc);

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 30));
        gbc.gridy = 6;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Contraseña:");
        gbc.gridy = 7;
        add(passwordLabel, gbc);

        JTextField passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(300, 30));
        gbc.gridy = 8;
        add(passwordField, gbc);

        JLabel ageLabel = new JLabel("Edad:");
        gbc.gridy = 9;
        add(ageLabel, gbc);

        JTextField ageField = new JTextField();
        ageField.setPreferredSize(new Dimension(300, 30));
        gbc.gridy = 10;
        add(ageField, gbc);

        JButton uploadPhotoButton = new JButton("Agregar fotografía");
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        add(uploadPhotoButton, gbc);

        profilePictureLabel = new JLabel();
        profilePictureLabel.setPreferredSize(new Dimension(100, 100));
        gbc.gridx = 1;
        add(profilePictureLabel, gbc);

        uploadPhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIcon profilePicture = new ImageIcon(file.getPath());
                Image image = profilePicture.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                profilePictureLabel.setIcon(new ImageIcon(image));

                String user = usernameField.getText().trim();
                try {
                    funciones.guardarImagen(file, user);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        JButton registerButton = new JButton("Registrarse");
        registerButton.setBackground(new Color(177, 22, 110));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        add(registerButton, gbc);
        

        registerButton.addActionListener(e -> {
            try {
            String nombre = nameField.getText().trim();
            String contra = passwordField.getText();
            String generoString = (String) genderComboBox.getSelectedItem();
            char genero = generoString.charAt(0);
            String user = usernameField.getText().trim();
            String edadTexto = ageField.getText().trim();

            if (nombre.isEmpty() || contra.isEmpty() || user.isEmpty() || edadTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Registro fallido", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int edad;
            try {
                edad = Integer.parseInt(edadTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Edad inválida. Por favor, ingrese un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (profilePictureLabel.getIcon() == null) {
                int opcion = JOptionPane.showOptionDialog(
                    this,
                    "No se ha subido ninguna imagen. ¿Desea agregar una imagen o continuar sin ella?",
                    "Imagen de perfil",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Agregar imagen", "Omitir"},
                    "Agregar imagen"
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    JFileChooser fileChooser = new JFileChooser();
                    int option = fileChooser.showOpenDialog(this);
                    if (option == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        ImageIcon profilePicture = new ImageIcon(file.getPath());
                        Image image = profilePicture.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        profilePictureLabel.setIcon(new ImageIcon(image));
                        try {
                            funciones.guardarImagen(file, user);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(this, "Error al guardar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    } 
                }else{
                    try {
                        funciones.guardarImagen(null, user);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error al guardar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
            
            if (funciones.agregarCuenta(nombre, contra, genero, user, edad)) {
                JOptionPane.showMessageDialog(this, "Registro exitoso", "Registro", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                MenuPrincipal loginGUI = new MenuPrincipal();
                loginGUI.setVisible(true);
            } else if (funciones.buscar(user)) {
                JOptionPane.showMessageDialog(this, "Ya existe una cuenta con ese user.", "Registro fallido", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registro fallido", "Registro", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException ex) {
            Logger.getLogger(Registrarse.class.getName()).log(Level.SEVERE, null, ex);
        }
        });
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                new MenuPrincipal().setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Registrarse registerGUI = new Registrarse();
            registerGUI.setVisible(true);
        });
    }
}
