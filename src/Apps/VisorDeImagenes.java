package Apps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class VisorDeImagenes extends JFrame {

    private JLabel labelImagen;
    private JButton btnAnterior, btnSiguiente;
    private File[] listaDeImagenes;
    private int indiceActual;
    private String directorio;
    private JPanel panelMiniaturas;

    public VisorDeImagenes(String nombreUsuario) {
        // Ruta del directorio basado en el nombre del usuario
        this.directorio = "Z" + File.separator + nombreUsuario + File.separator + "Mis Imágenes";

        // Configuración básica del JFrame
        setTitle("Visor de Imágenes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Establecer un fondo de color
        getContentPane().setBackground(new Color(40, 40, 40));

        // Cargar imágenes desde el directorio del usuario
        cargarImagenesDesdeDirectorio();

        if (listaDeImagenes == null || listaDeImagenes.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay imágenes en el directorio.");
        }

        // Mostrar la primera imagen
        indiceActual = 0;
        labelImagen = new JLabel();
        labelImagen.setHorizontalAlignment(JLabel.CENTER);
        actualizarImagen();

        // Establecer un borde alrededor de la imagen
        labelImagen.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));

        // Panel inferior con miniaturas y botones de navegación
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(50, 50, 50));

        // Crear el panel de miniaturas
        panelMiniaturas = new JPanel();
        panelMiniaturas.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelMiniaturas.setBackground(new Color(30, 30, 30));
        cargarMiniaturas();

        // Crear el panel de botones (flechas)
        JPanel panelFlechas = new JPanel(new BorderLayout());
        panelFlechas.setBackground(new Color(50, 50, 50));

        btnAnterior = crearBotonNavegacion("<");
        btnSiguiente = crearBotonNavegacion(">");

        panelFlechas.add(btnAnterior, BorderLayout.WEST);
        panelFlechas.add(btnSiguiente, BorderLayout.EAST);

        // Añadir el panel de miniaturas y las flechas al panel inferior
        panelInferior.add(btnAnterior, BorderLayout.WEST);
        panelInferior.add(panelMiniaturas, BorderLayout.CENTER);
        panelInferior.add(btnSiguiente, BorderLayout.EAST);

        // Acción del botón "Anterior"
        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indiceActual > 0) {
                    indiceActual--;
                    actualizarImagen();
                }
            }
        });

        // Acción del botón "Siguiente"
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indiceActual < listaDeImagenes.length - 1) {
                    indiceActual++;
                    actualizarImagen();
                }
            }
        });

        // Añadir componentes al JFrame
        add(labelImagen, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarImagenesDesdeDirectorio() {
        File carpeta = new File(directorio);
        listaDeImagenes = carpeta.listFiles((dir, name) -> {
            String nameLower = name.toLowerCase();
            return nameLower.endsWith(".jpg") || nameLower.endsWith(".png") || nameLower.endsWith(".gif");
        });
    }

   private void actualizarImagen() {
    if (listaDeImagenes != null && listaDeImagenes.length > 0) {
        ImageIcon icono = new ImageIcon(listaDeImagenes[indiceActual].getAbsolutePath());
        
        // Obtener el tamaño original de la imagen
        int originalWidth = icono.getIconWidth();
        int originalHeight = icono.getIconHeight();
        
        // Limitar el tamaño máximo a 800x500 sin distorsionar la relación de aspecto
        int maxWidth = 800;
        int maxHeight = 500;
        
        int newWidth = originalWidth;
        int newHeight = originalHeight;
        
        // Ajustar las dimensiones si la imagen es más grande que el visor
        if (originalWidth > maxWidth || originalHeight > maxHeight) {
            double widthRatio = (double) maxWidth / originalWidth;
            double heightRatio = (double) maxHeight / originalHeight;
            double bestRatio = Math.min(widthRatio, heightRatio); // Mantener relación de aspecto
            
            newWidth = (int) (originalWidth * bestRatio);
            newHeight = (int) (originalHeight * bestRatio);
        }
        
        // Escalar la imagen
        Image imagenEscalada = icono.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        labelImagen.setIcon(new ImageIcon(imagenEscalada));
        setTitle("Visor de Imágenes - " + listaDeImagenes[indiceActual].getName());
    } else {
        labelImagen.setIcon(null);
        setTitle("Visor de Imágenes - Sin imágenes");
    }
}

    private JButton crearBotonNavegacion(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(70, 70, 70)); // Color de fondo oscuro
        boton.setForeground(Color.WHITE); // Texto en blanco
        boton.setFont(new Font("Arial", Font.BOLD, 20)); // Fuente personalizada
        boton.setFocusPainted(false); // Desactivar borde al hacer clic
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambiar cursor al pasar el mouse
        boton.setPreferredSize(new Dimension(50, 40)); // Tamaño del botón

        // Estilo de bordes redondeados
        boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(90, 90, 90));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 70, 70));
            }
        });

        return boton;
    }

    private void cargarMiniaturas() {
        if (listaDeImagenes != null && listaDeImagenes.length > 0) {
            for (int i = 0; i < listaDeImagenes.length; i++) {
                ImageIcon icono = new ImageIcon(listaDeImagenes[i].getAbsolutePath());
                Image imagenMiniatura = icono.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH);
                JLabel labelMiniatura = new JLabel(new ImageIcon(imagenMiniatura));
                final int indice = i;
                labelMiniatura.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                labelMiniatura.setCursor(new Cursor(Cursor.HAND_CURSOR));
                labelMiniatura.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        indiceActual = indice;
                        actualizarImagen();
                    }
                });
                panelMiniaturas.add(labelMiniatura);
            }
        }
    }

    public static void main(String[] args) {
        String nombreUsuario = "usuarioEjemplo";  // Cambiar por el nombre del usuario actual
        VisorDeImagenes visor = new VisorDeImagenes(nombreUsuario);
        visor.setLocationRelativeTo(null);
        visor.setVisible(true);
    }
}
