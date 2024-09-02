/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;

/**
 *
 * @author User
 */
public class InstagramVisual extends JFrame{
    private JPanel panelMenu, panelContenido, panelPerfil;
    private CardLayout cardLayout;
    private JPopupMenu subMenuEditarPerfil;
    FuncionesUsers funciones;
    MenuPrincipal menu;

    public InstagramVisual(MenuPrincipal menuprincipal) {
        menu= menuprincipal;
        funciones=new FuncionesUsers();
        setTitle("Insta GUI");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear panel de menú lateral
        panelMenu = new JPanel();
        panelMenu.setBackground(Color.WHITE);
        panelMenu.setLayout(new GridLayout(0, 1, 0, 10)); // 0 filas, 1 columna, 10 píxeles de espaciado vertical

        // Botones del menú lateral
        String[] opcionesMenu = {
            "Perfil del usuario", 
            "Cargar imágenes", 
            "Comentar a las imágenes", 
            "Seguir o dejar de seguir", 
            "Editar Perfil", 
            "Búsqueda de InstaHashtags", 
            "Cerrar sesión"
        };

        for (String opcion : opcionesMenu) {
            JButton boton = crearBoton(opcion);
            panelMenu.add(boton);
        }

        subMenuEditarPerfil = new JPopupMenu();
        subMenuEditarPerfil.setBackground(Color.WHITE);
        JMenuItem buscarPersonas = crearMenuItem("Buscar personas");
        JMenuItem entrarCuenta = crearMenuItem("Entrar a una cuenta");
        JMenuItem desactivarCuenta = crearMenuItem("Desactivar/Activar cuenta");
        subMenuEditarPerfil.add(buscarPersonas);
        subMenuEditarPerfil.add(entrarCuenta);
        subMenuEditarPerfil.add(desactivarCuenta);

        buscarPersonas.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Buscar personas seleccionado");
            cardLayout.show(panelContenido, "Buscar personas");
        });

        entrarCuenta.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Entrar a una cuenta seleccionado");
            cardLayout.show(panelContenido, "Entrar a una cuenta");
        });

        desactivarCuenta.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas desactivar/activar tu cuenta?", "Desactivar/Activar cuenta", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    String texto = JOptionPane.showInputDialog (this, "Ingrese su contraseña: ", "Confirmar contraseña.",JOptionPane.INFORMATION_MESSAGE);
                    try {
                        if (funciones.DesActivarCuenta(menu.usernameActual, texto)){
                            String mensaje=(funciones.EstadoCuenta(menu.usernameActual))? "Se activo su cuenta.": "Se desactivo su cuenta.";
                            JOptionPane.showMessageDialog(this, mensaje , "", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(InstagramVisual.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            cardLayout.show(panelContenido, "Desactivar/Activar cuenta");
        });
        
        add(panelMenu, BorderLayout.WEST);

        panelContenido = new JPanel();
        cardLayout = new CardLayout();
        panelContenido.setLayout(cardLayout);

        panelPerfil = crearPanelPerfil();
        JPanel panelCargarImagenes = crearPanelCargarImagenes();
        
        panelContenido.add(panelPerfil, "Perfil del usuario");
        panelContenido.add(panelCargarImagenes, "Cargar imágenes");
        add(panelContenido, BorderLayout.CENTER);
    }

    private JButton crearBoton(String text) {
        JButton boton = new JButton(text);
        boton.setForeground(Color.BLACK);
        boton.setBackground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.PLAIN, 16));
        boton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        boton.setPreferredSize(new Dimension(200, 40));
        boton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Cerrar sesión")) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                } else if (text.equals("Editar Perfil")) {
                    // Mostrar el submenú junto al botón
                    subMenuEditarPerfil.show(boton, boton.getWidth(), 0);
                } else {
                    cardLayout.show(panelContenido, text);
                }
            }
        });
        
        return boton;
    }

    private JMenuItem crearMenuItem(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(new Font("Arial", Font.PLAIN, 18));
        item.setBackground(Color.WHITE);
        item.setForeground(Color.BLACK);
        item.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        return item;
    }

    private JPanel crearPanelPerfil() {
        JPanel panelPerfil = new JPanel();
        panelPerfil.setLayout(new BorderLayout());

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BorderLayout());
        panelInfo.setPreferredSize(new Dimension(1200, 150));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon iconoOriginal = new ImageIcon("Instagram/imagenes/" + menu.usernameActual + ".png");
        Image imagenOriginal = iconoOriginal.getImage();
        Image imagenEscalada = imagenOriginal.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel lblFotoPerfil = new JLabel(new ImageIcon(imagenEscalada));
        lblFotoPerfil.setPreferredSize(new Dimension(100, 100));
        JPanel panelFotoPerfil = new JPanel();
        panelFotoPerfil.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelFotoPerfil.add(lblFotoPerfil);

        JPanel panelInfoUsuario = new JPanel();
        panelInfoUsuario.setLayout(new BoxLayout(panelInfoUsuario, BoxLayout.Y_AXIS));
        panelInfoUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUsername = new JLabel("@" + menu.usernameActual);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnSeguir = new JButton("Seguir");
        btnSeguir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSeguir.setPreferredSize(new Dimension(80, 30));

        JPanel panelUsernameSeguir = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // 10 píxeles de espacio horizontal
        panelUsernameSeguir.add(lblUsername);
        panelUsernameSeguir.add(btnSeguir);

        JLabel lblSeguidores = new JLabel("59.3M seguidores");
        lblSeguidores.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel lblSeguidos = new JLabel("640 seguidos");
        lblSeguidos.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel panelSeguidoresSeguidos = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelSeguidoresSeguidos.add(lblSeguidores);
        panelSeguidoresSeguidos.add(Box.createHorizontalStrut(20));
        panelSeguidoresSeguidos.add(lblSeguidos);

        JLabel lblNombreCompleto = new JLabel("Chris Hemsworth");
        lblNombreCompleto.setFont(new Font("Arial", Font.PLAIN, 14));
        panelInfoUsuario.add(panelUsernameSeguir);
        panelInfoUsuario.add(Box.createVerticalStrut(10)); // Espacio vertical entre username y seguidores
        panelInfoUsuario.add(panelSeguidoresSeguidos);
        panelInfoUsuario.add(lblNombreCompleto);

        // Añadir paneles al panel principal
        panelInfo.add(panelFotoPerfil, BorderLayout.WEST);
        panelInfo.add(panelInfoUsuario, BorderLayout.CENTER);


        // Panel inferior para los posts
        JPanel panelPosts = new JPanel();
        panelPosts.setLayout(new GridLayout(0, 3, 5, 5));  // 3 columnas, filas dinámicas
        panelPosts.setBackground(Color.WHITE);

        // Añadir posts de ejemplo
        for (int i = 1; i <= 9; i++) {
            JLabel lblPost = new JLabel(new ImageIcon("ruta/a/tu/imagen/post.png"));
            lblPost.setPreferredSize(new Dimension(200, 200)); // Tamaño del post
            panelPosts.add(lblPost);
        }

        // Añadir los paneles al panel de perfil
        panelPerfil.add(panelInfo, BorderLayout.NORTH);
        panelPerfil.add(new JScrollPane(panelPosts), BorderLayout.CENTER);

        return panelPerfil;
    }
    
    private JPanel crearPanelCargarImagenes() {
        JPanel panelCargarImagenes = new JPanel();
        panelCargarImagenes.setLayout(new BorderLayout());

        JLabel lblSeleccionarImagen = new JLabel("Seleccione una imagen para cargar:");
        lblSeleccionarImagen.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        JLabel lblVistaPrevia = new JLabel("", SwingConstants.CENTER);

        btnSeleccionarImagen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                ImageIcon imagenSeleccionada = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
                lblVistaPrevia.setIcon(new ImageIcon(imagenSeleccionada.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
            }
        });

        panelCargarImagenes.add(lblSeleccionarImagen, BorderLayout.NORTH);
        panelCargarImagenes.add(btnSeleccionarImagen, BorderLayout.CENTER);
        panelCargarImagenes.add(lblVistaPrevia, BorderLayout.SOUTH);

        return panelCargarImagenes;
    }
}
