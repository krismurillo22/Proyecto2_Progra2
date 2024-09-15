/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2_miniwindow;

import Apps.CMDVisual;
import Apps.NavegadorArchivosUsuario;
import Apps.EditorDeTexto;
import Apps.ReproductorMusical;
import Apps.VisorDeImagenes;
import Instagram.MenuPrincipal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.io.File;

/**
 *
 * @author User
 */
public class EscritorioVisual extends JFrame{
    CMDVisual cmdV;
    MenuPrincipal menuInsta;
    NavegadorArchivosUsuario navegador;
    ReproductorMusical reproductor;
    EditorDeTexto editor;
    
    private String nombreUsuario;
     
        // Almacena el nombre del usuario
        
     public EscritorioVisual(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario; 
        // Configuración de la ventana principal
        setTitle("Escritorio de " + nombreUsuario);  // Personaliza el título con el nombre del usuario
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear un panel para simular el escritorio
        JPanel desktopPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Iconos/Fondo.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        desktopPanel.setLayout(null);

        // Añadir iconos al escritorio
        addIcon(desktopPanel, "Instagram", "src/Iconos/Insta.png", 50, 50, 70, 70, new InstagramAction());
        addIcon(desktopPanel, "CMD", "src/Iconos/CMD.png", 50, 200, 70, 70, new CMDAction());
        addIcon(desktopPanel, "Editor de texto", "src/Iconos/EditorTexto.png", 50, 350, 70, 70, new EditorTextoAction());
        addIcon(desktopPanel, "Navegador", "src/Iconos/Navegador.png", 50, 500, 70, 70, new NavegadorAction());
        addIcon(desktopPanel, "Reproductor de Musica", "src/Iconos/ReproductorMusica.png", 50, 650, 70, 70, new ReproductorMusicaAction());
        addIcon(desktopPanel, "Visor de Imagenes", "src/Iconos/VisorImagenes.png", 150, 50, 70, 70, new VisorImagenesAction());

        // Añadir paneles a la ventana principal
        add(desktopPanel, BorderLayout.CENTER);
    }

    private void addIcon(JPanel panel, String name, String iconPath, int x, int y, int width, int height, ActionListener action) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        JButton iconButton = new JButton(new ImageIcon(scaledImg));
        iconButton.setBounds(x, y, width, height);
        iconButton.setActionCommand(name);
        iconButton.addActionListener(action);
        iconButton.setFocusPainted(false);
        iconButton.setBorderPainted(false);
        iconButton.setContentAreaFilled(false);

        panel.add(iconButton);
    }
    
    private class InstagramAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuInsta=new MenuPrincipal();
            menuInsta.setVisible(true);
        }
    }

    private class CMDAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cmdV=new CMDVisual(nombreUsuario);
            cmdV.setVisible(true);
        }
    }

       private class EditorTextoAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Instanciar y mostrar el editor de texto con el nombre del usuario
            EditorDeTexto editor = new EditorDeTexto(nombreUsuario);
            editor.setVisible(true);
        }
    }

    private class NavegadorAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Usa el nombre del usuario autenticado
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            File carpetaUsuario = new File(System.getProperty("user.dir") + File.separator + "Z" + File.separator + nombreUsuario);
            if (carpetaUsuario.exists()) {
                NavegadorArchivosUsuario navegador = new NavegadorArchivosUsuario(carpetaUsuario);
                navegador.setVisible(true);
            } else {
                System.out.println("El directorio del usuario no existe: " + carpetaUsuario.getAbsolutePath());
            }
        } else {
            System.out.println("Error: El nombre de usuario es nulo o vacío.");
        }
    }
}


    private class ReproductorMusicaAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ReproductorMusical reproductor = new ReproductorMusical(nombreUsuario);
            reproductor.setVisible(true);
        }
    }
    private class VisorImagenesAction implements ActionListener {
         public void actionPerformed(ActionEvent e) {
            VisorDeImagenes visor = new VisorDeImagenes(nombreUsuario);
            visor.setLocationRelativeTo(null);
            visor.setVisible(true);
            }

    
}
}

