/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2_miniwindow;

import Apps.CMDVisual;
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

/**
 *
 * @author User
 */
public class EscritorioVisual extends JFrame{
    CMDVisual cmdV;
    MenuPrincipal menuInsta;
    public EscritorioVisual() {
        // Configuración de la ventana principal
        setTitle("Escritorio");
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
            cmdV=new CMDVisual();
            cmdV.setVisible(true);
        }
    }

    private class EditorTextoAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para abrir Editor de Texto
            JOptionPane.showMessageDialog(null, "Abriendo Editor de Texto...");
        }
    }

    private class NavegadorAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para abrir Navegador
            JOptionPane.showMessageDialog(null, "Abriendo Navegador...");
        }
    }

    private class ReproductorMusicaAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para abrir Reproductor de Música
            JOptionPane.showMessageDialog(null, "Abriendo Reproductor de Música...");
        }
    }

    private class VisorImagenesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para abrir Visor de Imágenes
            JOptionPane.showMessageDialog(null, "Abriendo Visor de Imágenes...");
        }
    }

    
}
