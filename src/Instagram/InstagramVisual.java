/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author User
 */
public class InstagramVisual extends JFrame{
    private JPanel panelMenu, panelContenido;
    private CardLayout cardLayout;

    public InstagramVisual() {
        setTitle("Insta GUI");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear panel de menú lateral
        panelMenu = new JPanel();
        panelMenu.setBackground(Color.WHITE);
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));

        // Botones del menú lateral
        String[] opcionesMenu = {
            "Perfil del usuario", 
            "Cargar imágenes", 
            "Comentar a las imágenes", 
            "Seguir o dejar de seguir", 
            "Editar Perfil", 
            "Buscar personas", 
            "Entrar a una cuenta", 
            "Desactivar/Activar cuenta", 
            "Búsqueda de InstaHashtags", 
            "Cerrar sesión"
        };

        for (String opcion : opcionesMenu) {
            JButton boton = crearBoton(opcion);
            panelMenu.add(boton);
            panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));  // Espaciado entre botones
        }

        add(panelMenu, BorderLayout.WEST);

        // Crear panel de contenido principal con CardLayout
        panelContenido = new JPanel();
        cardLayout = new CardLayout();
        panelContenido.setLayout(cardLayout);

        // Paneles de contenido para cada opción
        for (String opcion : opcionesMenu) {
            JPanel panel = new JPanel();
            panel.add(new JLabel(opcion));
            panelContenido.add(panel, opcion);
        }

        add(panelContenido, BorderLayout.CENTER);
    }

    private JButton crearBoton(String text) {
        JButton boton = new JButton(text);
        boton.setForeground(Color.BLACK);
        boton.setBackground(Color.LIGHT_GRAY);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.PLAIN, 16));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Cerrar sesión")) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                } else {
                    cardLayout.show(panelContenido, text);
                }
            }
        });
        
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InstagramVisual gui = new InstagramVisual();
            gui.setVisible(true);
        });
    }
}
