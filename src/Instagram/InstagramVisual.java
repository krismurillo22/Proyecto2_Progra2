/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;

/**
 *
 * @author User
 */
public class InstagramVisual extends JFrame {

    private JPanel panelMenu, panelContenido, panelPerfil, panelUserBuscado, panelHashtags, panelComentar;
    private CardLayout cardLayout;
    private JPopupMenu subMenuEditarPerfil;
    FuncionesUsers funciones;
    MenuPrincipal menu;
    private String userBuscado;
    private String rutaImagenSeleccionada;
    private String usuario;

    public InstagramVisual(MenuPrincipal menuprincipal) throws IOException {
        menu = menuprincipal;
        userBuscado = "";
        funciones = new FuncionesUsers();
        setTitle("Bienvenido");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelMenu = new JPanel();
        panelMenu.setBackground(Color.WHITE);
        panelMenu.setLayout(new GridLayout(0, 1, 0, 10));

        String[] opcionesMenu = {
            "Perfil del usuario",
            "Cargar imágenes",
            "Comentar a las imágenes",
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
            JPanel panelResultados;
            try {
                panelResultados = crearPanelBuscarPersonas();
                panelContenido.add(panelResultados, "ResultadosBusqueda");
                cardLayout.show(panelContenido, "ResultadosBusqueda");
            } catch (IOException ex) {
                Logger.getLogger(InstagramVisual.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        entrarCuenta.addActionListener(e -> {
            usuario = JOptionPane.showInputDialog(this, "Ingrese el usuario que desea buscar: ", "Ingresar usuario.", JOptionPane.INFORMATION_MESSAGE);
            try {
                if (usuario != null) {
                    if (usuario.equals(menu.usernameActual)) {
                        JOptionPane.showMessageDialog(this, "No se puede buscar ni seguir a su propio usuario.", "ERROR", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (funciones.buscar(usuario)) {
                            userBuscado = usuario;
                            panelUserBuscado = crearPanelUserBuscado();
                            panelContenido.add(panelUserBuscado, "Entrar a una cuenta");
                            cardLayout.show(panelContenido, "Entrar a una cuenta");
                        } else {
                            JOptionPane.showMessageDialog(this, "El usuario buscado no existe.", "ERROR", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(InstagramVisual.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        desactivarCuenta.addActionListener(e -> {
            String estado="";
            try {
                estado = (funciones.EstadoCuenta(menu.usernameActual)) ? "desactivar" : "activar";
            } catch (IOException ex) {
                Logger.getLogger(InstagramVisual.class.getName()).log(Level.SEVERE, null, ex);
            }
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas "+estado+" tu cuenta?", estado+" cuenta", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                String texto = JOptionPane.showInputDialog(this, "Ingrese su contraseña: ", "Confirmar contraseña.", JOptionPane.INFORMATION_MESSAGE);
                try {
                    if (funciones.DesActivarCuenta(menu.usernameActual, texto)) {
                        String mensaje = (funciones.EstadoCuenta(menu.usernameActual)) ? "Se activo su cuenta." : "Se desactivo su cuenta.";
                        JOptionPane.showMessageDialog(this, mensaje, "", JOptionPane.INFORMATION_MESSAGE);
                    } else {
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
        panelUserBuscado = crearPanelUserBuscado();

        panelContenido.add(panelPerfil, "Perfil del usuario");
        panelContenido.add(panelCargarImagenes, "Cargar imágenes");
        panelContenido.add(panelUserBuscado, "Entrar a una cuenta");
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
                        menu=new MenuPrincipal();
                        menu.setVisible(true);
                    }
                } else if (text.equals("Editar Perfil")) {
                    subMenuEditarPerfil.show(boton, boton.getWidth(), 0);
                } else if (text.equals("Perfil del usuario")) {
                    try {
                        panelPerfil = crearPanelPerfil();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    panelContenido.add(panelPerfil, "Perfil del usuario");
                    cardLayout.show(panelContenido, "Perfil del usuario");

                } else if (text.equals("Comentar a las imágenes")) {
                    try {
                        panelComentar = crearPanelComentar();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    panelContenido.add(panelComentar, "Comentar a las imágenes");
                    cardLayout.show(panelContenido, "Comentar a las imágenes");

                } else if (text.equals("Búsqueda de InstaHashtags")) {
                    try {
                        panelHashtags = crearPanelBuscarHashtags();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    panelContenido.add(panelHashtags, "Búsqueda de InstaHashtags");
                    cardLayout.show(panelContenido, "Búsqueda de InstaHashtags");
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

    private JPanel crearPanelPerfil() throws IOException {
        JPanel panelPerfil = new JPanel();
        panelPerfil.setLayout(new BorderLayout());

        JPanel panelInfo = new JPanel();
        panelInfo.setBackground(new Color(234, 213, 228));
        panelInfo.setLayout(new BorderLayout());
        panelInfo.setPreferredSize(new Dimension(1200, 150));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon iconoOriginal = new ImageIcon(funciones.obtenerRutaImagen(menu.usernameActual));
        Image imagenOriginal = iconoOriginal.getImage();
        Image imagenEscalada = imagenOriginal.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JLabel lblFotoPerfil = new JLabel(new ImageIcon(imagenEscalada));
        lblFotoPerfil.setPreferredSize(new Dimension(100, 100));
        JPanel panelFotoPerfil = new JPanel();
        panelFotoPerfil.setBackground(Color.WHITE);
        panelFotoPerfil.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelFotoPerfil.add(lblFotoPerfil);

        JPanel panelInfoUsuario = new JPanel();
        panelInfoUsuario.setBackground(Color.WHITE);
        panelInfoUsuario.setLayout(new BoxLayout(panelInfoUsuario, BoxLayout.Y_AXIS));
        panelInfoUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfoUsuario.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        int seguidores = funciones.obtenerContador(menu.usernameActual, 0);
        int seguidos = funciones.obtenerContador(menu.usernameActual, 1);
        JLabel lblUsername = new JLabel("@" + menu.usernameActual);
        JLabel lblSeguidores = new JLabel(seguidores + " seguidores");
        JLabel lblSeguidos = new JLabel(seguidos + " seguidos");

        lblUsername.setFont(new Font("Arial", Font.BOLD, 18));
        lblSeguidores.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSeguidos.setFont(new Font("Arial", Font.PLAIN, 14));

        panelInfoUsuario.add(lblUsername);
        panelInfoUsuario.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfoUsuario.add(lblSeguidores);
        panelInfoUsuario.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfoUsuario.add(lblSeguidos);

        panelInfo.add(panelFotoPerfil, BorderLayout.WEST);
        panelInfo.add(panelInfoUsuario, BorderLayout.CENTER);

        JPanel panelPosts = new JPanel();
        panelPosts.setLayout(new GridBagLayout());
        panelPosts.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        int gridx = 0;
        int gridy = 0;

        String directorioPosts = funciones.userFolder(menu.usernameActual + "/posts");
        File carpetaPosts = new File(directorioPosts);

        if (carpetaPosts.exists() && carpetaPosts.isDirectory()) {
            File[] archivosImagenes = carpetaPosts.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));

            if (archivosImagenes != null) {
                Arrays.sort(archivosImagenes, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                for (File archivoImagen : archivosImagenes) {
                    ImageIcon iconoPost = new ImageIcon(archivoImagen.getAbsolutePath());
                    Image imagenPost = iconoPost.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    JLabel lblPost = new JLabel(new ImageIcon(imagenPost));
                    lblPost.setPreferredSize(new Dimension(200, 200));

                    lblPost.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            JframePost(menu.usernameActual, iconoPost, archivoImagen.getAbsolutePath());
                        }
                    });
                    gbc.gridx = gridx;
                    gbc.gridy = gridy;
                    panelPosts.add(lblPost, gbc);
                    gridx++;
                    if (gridx % 3 == 0) {
                        gridx = 0;
                        gridy++;
                    }
                }
            }
        } else {
            JLabel lblSinPosts = new JLabel("No hay imágenes para mostrar.");
            lblSinPosts.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            panelPosts.add(lblSinPosts, gbc);
        }

        panelPerfil.add(panelInfo, BorderLayout.NORTH);
        panelPerfil.add(new JScrollPane(panelPosts), BorderLayout.CENTER);

        return panelPerfil;
    }

    private void JframePost(String username, ImageIcon imagen, String pathImagen) {
        JFrame frameImagen = new JFrame("Imagen");
        frameImagen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameImagen.setSize(500, 800);
        frameImagen.setLocationRelativeTo(null);
        frameImagen.setLayout(new BorderLayout());

        JPanel panelUsername = new JPanel();
        panelUsername.setBackground(Color.WHITE);
        panelUsername.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUsername = new JLabel("@" + username);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 16));
        panelUsername.add(lblUsername);

        JPanel panelImagen = new JPanel();
        panelImagen.setBackground(Color.WHITE);
        JLabel lblImagenGrande = new JLabel();
        Image imagenOriginal = imagen.getImage();
        Image imagenRedimensionada = imagenOriginal.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        lblImagenGrande.setIcon(new ImageIcon(imagenRedimensionada));
        lblImagenGrande.setPreferredSize(new Dimension(500, 500));
        panelImagen.add(lblImagenGrande);

        JPanel panelComentarios = new JPanel();
        panelComentarios.setBackground(Color.WHITE);
        panelComentarios.setLayout(new BoxLayout(panelComentarios, BoxLayout.Y_AXIS));
        JLabel lblComentarios = new JLabel("Comentarios:");
        lblComentarios.setFont(new Font("Arial", Font.BOLD, 18));
        panelComentarios.add(lblComentarios);

        JTextArea txtAreaComentarios = new JTextArea(10, 40);
        txtAreaComentarios.setLineWrap(true);
        txtAreaComentarios.setWrapStyleWord(true);
        txtAreaComentarios.setEditable(false);
        JScrollPane scrollPaneComentarios = new JScrollPane(txtAreaComentarios);
        panelComentarios.add(scrollPaneComentarios);
        funciones.mostrarComentarios(pathImagen, txtAreaComentarios, username);

        JPanel panelEnviar = new JPanel();
        panelEnviar.setBackground(Color.WHITE);
        panelEnviar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JTextField txtFieldNuevoComentario = new JTextField(30);
        JButton btnEnviar = new JButton();
        btnEnviar.setBackground(new Color(177, 22, 110));

        ImageIcon iconoEnviar = new ImageIcon("src/Iconos/enviar.png");
        Image imagenEnviar = iconoEnviar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnEnviar.setIcon(new ImageIcon(imagenEnviar));

        btnEnviar.addActionListener(e -> {
            String nuevoComentario = txtFieldNuevoComentario.getText();
            if (nuevoComentario.length() > 140) {
                JOptionPane.showMessageDialog(frameImagen, "El comentario no puede exceder los 140 caracteres.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (!nuevoComentario.isEmpty()) {
                try {
                    txtAreaComentarios.setText("");
                    funciones.Comentarios(username, pathImagen, menu.usernameActual, nuevoComentario);
                    funciones.mostrarComentarios(pathImagen, txtAreaComentarios, username);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al subir comentario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                txtFieldNuevoComentario.setText("");
                
            }
        });

        panelEnviar.add(txtFieldNuevoComentario);
        panelEnviar.add(btnEnviar);

        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BorderLayout());
        panelSur.add(panelComentarios, BorderLayout.CENTER);
        panelSur.add(panelEnviar, BorderLayout.SOUTH);

        frameImagen.add(panelUsername, BorderLayout.NORTH);
        frameImagen.add(panelImagen, BorderLayout.CENTER);
        frameImagen.add(panelSur, BorderLayout.SOUTH);

        frameImagen.setVisible(true);
    }

    private JPanel crearPanelCargarImagenes() {
        JPanel panelCargarImagenes = new JPanel(new BorderLayout());
        JPanel panelInstrucciones = new JPanel();
        panelInstrucciones.setLayout(new BorderLayout());
        panelInstrucciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTituloInstrucciones = new JLabel("Instrucciones", SwingConstants.CENTER);
        lblTituloInstrucciones.setFont(new Font("Arial", Font.BOLD, 20));
        panelInstrucciones.add(lblTituloInstrucciones, BorderLayout.NORTH);

        JTextArea textAreaInstrucciones = new JTextArea("Solo se aceptan formatos PNG y JPG.\nLa foto debe ser cuadrada.\nPara subir la imagen al perfil, presione el botón 'Subir'.");
        textAreaInstrucciones.setFont(new Font("Arial", Font.PLAIN, 18));
        textAreaInstrucciones.setEditable(false);
        textAreaInstrucciones.setBackground(panelInstrucciones.getBackground());
        textAreaInstrucciones.setLineWrap(true);
        textAreaInstrucciones.setWrapStyleWord(true);
        panelInstrucciones.add(textAreaInstrucciones, BorderLayout.CENTER);

        JPanel panelImagenConCuadricula = new JPanel();
        panelImagenConCuadricula.setBackground(Color.WHITE);
        panelImagenConCuadricula.setLayout(new OverlayLayout(panelImagenConCuadricula));
        panelImagenConCuadricula.setPreferredSize(new Dimension(520, 520));
        panelImagenConCuadricula.setMaximumSize(new Dimension(520, 520));
        panelImagenConCuadricula.setMinimumSize(new Dimension(520, 520));
        panelImagenConCuadricula.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelImagenConCuadricula.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel lblImagen = new JLabel();
        lblImagen.setAlignmentX(0.5f);
        lblImagen.setAlignmentY(0.5f);
        lblImagen.setPreferredSize(new Dimension(520, 520));
        lblImagen.setMaximumSize(new Dimension(520, 520));
        lblImagen.setMinimumSize(new Dimension(520, 520));

        JPanel panelCuadricula = new JPanel(new GridLayout(3, 3, 0, 0));
        panelCuadricula.setOpaque(false);
        panelCuadricula.setPreferredSize(new Dimension(520, 520));
        panelCuadricula.setMaximumSize(new Dimension(520, 520));
        panelCuadricula.setMinimumSize(new Dimension(520, 520));

        for (int i = 0; i < 9; i++) {
            JLabel cuadro = new JLabel();
            cuadro.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            panelCuadricula.add(cuadro);
        }

        panelImagenConCuadricula.add(panelCuadricula);
        panelImagenConCuadricula.add(lblImagen);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton btnAñadir = new JButton("Añadir");
        JButton btnSubir = new JButton("Subir");
        JButton btnCancelar = new JButton("Cancelar");

        Font botonFont = new Font("Arial", Font.PLAIN, 20);

        btnAñadir.setFont(botonFont);
        btnSubir.setFont(botonFont);
        btnCancelar.setFont(botonFont);
        btnAñadir.setBackground(new Color(177, 22, 110));
        btnAñadir.setForeground(Color.WHITE);
        btnAñadir.setPreferredSize(new Dimension(280, 60));
        btnSubir.setBackground(new Color(177, 22, 110));
        btnSubir.setForeground(Color.WHITE);
        btnSubir.setPreferredSize(new Dimension(280, 60));
        btnCancelar.setBackground(new Color(177, 22, 110));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setPreferredSize(new Dimension(280, 60));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(btnAñadir, gbc);

        gbc.gridy = 1;
        panelBotones.add(btnSubir, gbc);

        gbc.gridy = 2;
        panelBotones.add(btnCancelar, gbc);

        btnAñadir.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(panelCargarImagenes);
            if (result == JFileChooser.APPROVE_OPTION) {
                File archivoImagen = fileChooser.getSelectedFile();
                ImageIcon imagenSeleccionada = new ImageIcon(archivoImagen.getAbsolutePath());
                rutaImagenSeleccionada = archivoImagen.getAbsolutePath();
                lblImagen.setIcon(new ImageIcon(imagenSeleccionada.getImage().getScaledInstance(520, 520, Image.SCALE_SMOOTH)));
            }
        });

        btnSubir.addActionListener(e -> {
            if (rutaImagenSeleccionada != null && !rutaImagenSeleccionada.isEmpty()) {
                File archivoImagen = new File(rutaImagenSeleccionada);
                try {
                    funciones.guardarPost(menu.usernameActual, archivoImagen);
                    lblImagen.setIcon(null);
                    rutaImagenSeleccionada = null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panelCargarImagenes, "Error al subir la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panelCargarImagenes, "Seleccione una imagen antes de subir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> {
            lblImagen.setIcon(null);
            rutaImagenSeleccionada = null;
        });
        panelCargarImagenes.add(panelInstrucciones, BorderLayout.NORTH);
        panelCargarImagenes.add(panelImagenConCuadricula, BorderLayout.CENTER);
        panelCargarImagenes.add(panelBotones, BorderLayout.EAST);

        return panelCargarImagenes;
    }

    private JPanel crearPanelUserBuscado() throws IOException {
        JPanel panelPerfil = new JPanel();
        panelPerfil.setLayout(new BorderLayout());

        JPanel panelInfo = new JPanel();
        panelInfo.setBackground(new Color(247, 244, 215));
        panelInfo.setLayout(new BorderLayout());
        panelInfo.setPreferredSize(new Dimension(1000, 150));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ImageIcon iconoOriginal = new ImageIcon(funciones.obtenerRutaImagen(userBuscado));
        Image imagenOriginal = iconoOriginal.getImage();
        Image imagenEscalada = imagenOriginal.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JLabel lblFotoPerfil = new JLabel(new ImageIcon(imagenEscalada));
        lblFotoPerfil.setPreferredSize(new Dimension(100, 100));
        JPanel panelFotoPerfil = new JPanel();
        panelFotoPerfil.setBackground(new Color(247, 244, 215));
        panelFotoPerfil.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelFotoPerfil.add(lblFotoPerfil);

        JPanel panelInfoUsuario = new JPanel();
        panelInfoUsuario.setBackground(new Color(247, 244, 215));
        panelInfoUsuario.setLayout(new BoxLayout(panelInfoUsuario, BoxLayout.Y_AXIS));
        panelInfoUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfoUsuario.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        int seguidores = funciones.obtenerContador(userBuscado, 0);
        int seguidos = funciones.obtenerContador(userBuscado, 1);
        JLabel lblUsername = new JLabel("@" + userBuscado);
        JLabel lblSeguidores = new JLabel(seguidores + " seguidores");
        JLabel lblSeguidos = new JLabel(seguidos + " seguidos");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 18));
        lblSeguidores.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSeguidos.setFont(new Font("Arial", Font.PLAIN, 16));

        String boton = (funciones.verificarSiSigue(menu.usernameActual, userBuscado)) ? "Dejar de seguir" : "Seguir";
        JButton btnSeguir = new JButton(boton);
        btnSeguir.setBackground(new Color(177, 22, 110));
        btnSeguir.setForeground(Color.WHITE);
        btnSeguir.setFont(new Font("Arial", Font.PLAIN, 14));

        btnSeguir.addActionListener(e -> {
            try {
                funciones.SeguirUser(menu.usernameActual, userBuscado);
                String nuevoTexto = (funciones.verificarSiSigue(menu.usernameActual, userBuscado)) ? "Dejar de seguir" : "Seguir";
                btnSeguir.setText(nuevoTexto);
            } catch (IOException ex) {
                Logger.getLogger(InstagramVisual.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar seguir/dejar de seguir al usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelInfoUsuario.add(lblUsername);
        panelInfoUsuario.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfoUsuario.add(lblSeguidores);
        panelInfoUsuario.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfoUsuario.add(lblSeguidos);

        JPanel panelUserAndButton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelUserAndButton.setBackground(new Color(247, 244, 215));
        panelUserAndButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelUserAndButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panelUserAndButton.add(lblUsername);
        panelUserAndButton.add(btnSeguir);

        panelInfoUsuario.remove(lblUsername);
        panelInfoUsuario.add(panelUserAndButton, 0);

        JTextArea textAreaDerecho = new JTextArea();
        textAreaDerecho.setPreferredSize(new Dimension(400, 300));
        textAreaDerecho.setBackground(panelInfo.getBackground());
        textAreaDerecho.setBorder(BorderFactory.createEmptyBorder());
        textAreaDerecho.setLineWrap(true);
        textAreaDerecho.setWrapStyleWord(true);
        textAreaDerecho.setEditable(false);
        textAreaDerecho.setFont(new Font("Arial", Font.BOLD, 16));
        textAreaDerecho.setText(funciones.imprimirUser(userBuscado));

        panelInfo.add(panelFotoPerfil, BorderLayout.WEST);
        panelInfo.add(panelInfoUsuario, BorderLayout.CENTER);
        panelInfo.add(textAreaDerecho, BorderLayout.EAST);

        JPanel panelPosts = new JPanel();
        panelPosts.setLayout(new GridBagLayout());
        panelPosts.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        int gridx = 0;
        int gridy = 0;

        String directorioPosts = funciones.userFolder(userBuscado + "/posts");
        File carpetaPosts = new File(directorioPosts);

        if (carpetaPosts.exists() && carpetaPosts.isDirectory()) {
            File[] archivosImagenes = carpetaPosts.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));

            if (archivosImagenes != null) {
                Arrays.sort(archivosImagenes, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                for (File archivoImagen : archivosImagenes) {
                    ImageIcon iconoPost = new ImageIcon(archivoImagen.getAbsolutePath());
                    Image imagenPost = iconoPost.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    JLabel lblPost = new JLabel(new ImageIcon(imagenPost));
                    lblPost.setPreferredSize(new Dimension(200, 200));

                    lblPost.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            JframePost(userBuscado, iconoPost, archivoImagen.getAbsolutePath());
                        }
                    });
                    gbc.gridx = gridx;
                    gbc.gridy = gridy;
                    panelPosts.add(lblPost, gbc);
                    gridx++;
                    if (gridx % 3 == 0) {
                        gridx = 0;
                        gridy++;
                    }
                }
            }
        } else {
            JLabel lblSinPosts = new JLabel("No hay imágenes para mostrar.");
            lblSinPosts.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            panelPosts.add(lblSinPosts, gbc);
        }
        panelPerfil.add(panelInfo, BorderLayout.NORTH);
        panelPerfil.add(new JScrollPane(panelPosts), BorderLayout.CENTER);

        return panelPerfil;
    }

    public JPanel crearPanelBuscarPersonas() throws IOException {
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new BorderLayout());

        JPanel panelBusquedaSuperior = new JPanel();
        panelBusquedaSuperior.setLayout(new BorderLayout(10, 10));
        JLabel labelInstrucciones = new JLabel("Ingrese que personas desea buscar:");
        labelInstrucciones.setHorizontalAlignment(JLabel.LEFT);

        JTextField textFieldBusqueda = new JTextField(30);
        textFieldBusqueda.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton btnBuscar = new JButton();
        ImageIcon iconoBuscar = new ImageIcon("src/Iconos/enviar.png");
        Image imagenBuscar = iconoBuscar.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        btnBuscar.setIcon(new ImageIcon(imagenBuscar));
        btnBuscar.setPreferredSize(new Dimension(40, 40));
        btnBuscar.setBackground(new Color(177, 22, 110));
        btnBuscar.addActionListener(e -> {
            String textoBusqueda = textFieldBusqueda.getText();
            if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
                try {
                    ArrayList<String> resultados = funciones.buscarUsuarios(textoBusqueda);
                    String resultadosTexto = "";
                    for (String username : resultados) {
                        boolean loSigo = funciones.verificarSiSigue(menu.usernameActual, username);
                        resultadosTexto += username
                                + " - "
                                + (loSigo ? "LO SIGUES" : "NO LO SIGUES")
                                + "\n";
                    }
                    JTextArea textAreaDetalles = (JTextArea) ((JScrollPane) panelBusqueda.getComponent(1)).getViewport().getView();
                    textAreaDetalles.setText(resultadosTexto);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ocurrió un error al buscar los usuarios.");
                }
            }
        });
        JPanel panelInputBusqueda = new JPanel(new BorderLayout(5, 5));
        panelInputBusqueda.add(textFieldBusqueda, BorderLayout.CENTER);
        panelInputBusqueda.add(btnBuscar, BorderLayout.EAST);

        panelBusquedaSuperior.add(labelInstrucciones, BorderLayout.NORTH);
        panelBusquedaSuperior.add(panelInputBusqueda, BorderLayout.CENTER);

        JTextArea textAreaDetalles = new JTextArea(15, 30);
        textAreaDetalles.setLineWrap(true);
        textAreaDetalles.setWrapStyleWord(true);
        textAreaDetalles.setEditable(false);
        textAreaDetalles.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPaneDetalles = new JScrollPane(textAreaDetalles);

        panelBusqueda.add(panelBusquedaSuperior, BorderLayout.NORTH);
        panelBusqueda.add(scrollPaneDetalles, BorderLayout.CENTER);

        return panelBusqueda;
    }

    public JPanel crearPanelBuscarHashtags() throws IOException {
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new BorderLayout());

        JPanel panelBusquedaSuperior = new JPanel();
        panelBusquedaSuperior.setLayout(new BorderLayout(10, 10));
        JLabel labelInstrucciones = new JLabel("Ingrese que hashtag desea buscar:");
        labelInstrucciones.setHorizontalAlignment(JLabel.LEFT);

        JTextField textFieldBusqueda = new JTextField(30);
        textFieldBusqueda.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton btnBuscar = new JButton();
        ImageIcon iconoBuscar = new ImageIcon("src/Iconos/enviar.png");
        Image imagenBuscar = iconoBuscar.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        btnBuscar.setIcon(new ImageIcon(imagenBuscar));
        btnBuscar.setPreferredSize(new Dimension(40, 40));
        btnBuscar.setBackground(new Color(177, 22, 110));

        btnBuscar.addActionListener(e -> {
            String hashtagBusqueda = textFieldBusqueda.getText();
            if (hashtagBusqueda != null && !hashtagBusqueda.trim().isEmpty()) {
                try {
                    ArrayList<String> comentariosConHashtag = funciones.buscarComHashtag(hashtagBusqueda);
                    String resultadosTexto = "";
                    for (String comentario : comentariosConHashtag) {
                        resultadosTexto += comentario + "\n";
                    }

                    JTextArea textAreaDetalles = (JTextArea) ((JScrollPane) panelBusqueda.getComponent(1)).getViewport().getView();
                    textAreaDetalles.setText(resultadosTexto);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ocurrió un error al buscar los comentarios.");
                }
            }
        });
        JPanel panelInputBusqueda = new JPanel(new BorderLayout(5, 5));
        panelInputBusqueda.add(textFieldBusqueda, BorderLayout.CENTER);
        panelInputBusqueda.add(btnBuscar, BorderLayout.EAST);

        panelBusquedaSuperior.add(labelInstrucciones, BorderLayout.NORTH);
        panelBusquedaSuperior.add(panelInputBusqueda, BorderLayout.CENTER);

        JTextArea textAreaDetalles = new JTextArea(15, 30);
        textAreaDetalles.setLineWrap(true);
        textAreaDetalles.setWrapStyleWord(true);
        textAreaDetalles.setEditable(false);
        textAreaDetalles.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPaneDetalles = new JScrollPane(textAreaDetalles);

        panelBusqueda.add(panelBusquedaSuperior, BorderLayout.NORTH);
        panelBusqueda.add(scrollPaneDetalles, BorderLayout.CENTER);

        return panelBusqueda;
    }

    public JPanel crearPanelComentar() throws IOException {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        try {
            RandomAccessFile postsFile = new RandomAccessFile("Instagram/posts.ins", "r");
            ArrayList<JPanel> postPanels = new ArrayList<>();

            while (postsFile.getFilePointer() < postsFile.length()) {
                String username = postsFile.readUTF();
                String pathImagen = postsFile.readUTF();
                if (funciones.buscar(username) && (funciones.verificarSiSigue(menu.usernameActual, username) || menu.usernameActual.equals(username))) {
                    JPanel postPanel = new JPanel();
                    postPanel.setLayout(new BorderLayout());
                    postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    postPanel.setBackground(Color.WHITE);
                    JLabel usernameLabel = new JLabel(username);
                    usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    postPanel.add(usernameLabel, BorderLayout.NORTH);

                    ImageIcon icon = new ImageIcon(pathImagen);
                    Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                    JLabel imagenLabel = new JLabel(new ImageIcon(scaledImage));
                    imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    postPanel.add(imagenLabel, BorderLayout.CENTER);

                    imagenLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JframePost(username, icon, pathImagen);
                        }
                    });
                    postPanels.add(postPanel);
                }
            }

            postsFile.close();

            for (int i = postPanels.size() - 1; i >= 0; i--) {
                panel.add(postPanels.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        return containerPanel;
    }
}
