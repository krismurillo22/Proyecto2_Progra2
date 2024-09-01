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
import java.io.File;

public class Registrarse extends JFrame{
    private JLabel profilePictureLabel;

    public Registrarse() {
        setTitle("Regístrate");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel nameLabel = new JLabel("Nombre completo:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameLabel, gbc);

        JTextField nameField = new JTextField();
        gbc.gridy = 1;
        add(nameField, gbc);

        JLabel genderLabel = new JLabel("Género:");
        gbc.gridy = 2;
        add(genderLabel, gbc);

        String[] genderOptions = {"M", "F"};
        JComboBox<String> genderComboBox = new JComboBox<>(genderOptions);
        gbc.gridy = 3;
        add(genderComboBox, gbc);

        JLabel usernameLabel = new JLabel("Nombre de usuario:");
        gbc.gridy = 4;
        add(usernameLabel, gbc);

        JTextField usernameField = new JTextField();
        gbc.gridy = 5;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Contraseña:");
        gbc.gridy = 6;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        gbc.gridy = 7;
        add(passwordField, gbc);

        JLabel ageLabel = new JLabel("Edad:");
        gbc.gridy = 8;
        add(ageLabel, gbc);

        JTextField ageField = new JTextField();
        gbc.gridy = 9;
        add(ageField, gbc);

        JButton uploadPhotoButton = new JButton("Agregar fotografía");
        gbc.gridy = 10;
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
                // Redimensionar la imagen para que se ajuste al JLabel
                Image image = profilePicture.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                profilePictureLabel.setIcon(new ImageIcon(image));
            }
        });

        JButton registerButton = new JButton("Registrarse");
        registerButton.setBackground(new Color(0, 120, 230));
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            // Aquí puedes agregar la lógica para guardar los datos del usuario
            JOptionPane.showMessageDialog(this, "Registro exitoso", "Registro", JOptionPane.INFORMATION_MESSAGE);
            dispose(); 
            MenuPrincipal loginGUI = new MenuPrincipal();
            loginGUI.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Registrarse registerGUI = new Registrarse();
            registerGUI.setVisible(true);
        });
    }
}
