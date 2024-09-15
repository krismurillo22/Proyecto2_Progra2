/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Apps;

/**
 *
 * @author Administrator
 */
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class NavegadorArchivosUsuario extends JFrame {

    private JTree tree;
    private DefaultTreeModel treeModel;
    private File rootDirectory;
    private File currentDirectory;
    private JPanel panelArchivos;
    private File archivoCopiado;

    public NavegadorArchivosUsuario(File directorioUsuario) {
        this.rootDirectory = directorioUsuario;

        setTitle("Navegador de Archivos");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear barra lateral de accesos rápidos con botones grandes
        JPanel panelLateral = new JPanel();
        panelLateral.setPreferredSize(new Dimension(150, getHeight()));
        panelLateral.setBackground(Color.BLACK); // Fondo oscuro
        panelLateral.setLayout(new GridLayout(6, 1, 10, 10));

        // Botones para accesos rápidos con estilo oscuro
        agregarBotonLateral(panelLateral, "Imágenes", Color.GRAY);
        agregarBotonLateral(panelLateral, "Música", Color.GRAY);
        agregarBotonLateral(panelLateral, "Videos", Color.GRAY);
        agregarBotonLateral(panelLateral, "Documentos", Color.GRAY);
        agregarBotonLateral(panelLateral, "Descargas", Color.GRAY);

        add(panelLateral, BorderLayout.WEST);

        // Crear el árbol de archivos desde el directorio raíz del usuario
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDirectory.getName());
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        cargarDirectorio(rootDirectory, rootNode);

        // Listener para detectar cambios en la selección del árbol
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode == null) return;

                File directorioSeleccionado = new File(rootDirectory, getRutaDesdeNodo(selectedNode));
                mostrarContenidoCarpeta(directorioSeleccionado);
            }
        });

        JScrollPane scrollTree = new JScrollPane(tree);
        scrollTree.getViewport().setBackground(Color.BLACK); // Fondo oscuro para el área de archivos
        tree.setBackground(Color.BLACK); // Fondo oscuro para el árbol
        tree.setForeground(Color.WHITE); // Texto blanco para el árbol
        add(scrollTree, BorderLayout.CENTER);

        // Crear el área central para mostrar archivos
        panelArchivos = new JPanel();
        panelArchivos.setLayout(new GridLayout(0, 5, 15, 15)); // Estilo de cuadrícula
        panelArchivos.setBackground(Color.DARK_GRAY); // Fondo oscuro
        JScrollPane scrollArchivos = new JScrollPane(panelArchivos);
        add(scrollArchivos, BorderLayout.EAST);

        // Barra de botones para operaciones de archivos con estilo oscuro
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 4));
        panelBotones.setBackground(Color.BLACK); // Fondo oscuro para los botones

        JButton copiarButton = new JButton("Copiar");
        copiarButton.setBackground(Color.DARK_GRAY);
        copiarButton.setForeground(Color.WHITE);
        copiarButton.addActionListener(e -> copiarArchivo());
        panelBotones.add(copiarButton);

        JButton pegarButton = new JButton("Pegar");
        pegarButton.setBackground(Color.DARK_GRAY);
        pegarButton.setForeground(Color.WHITE);
        pegarButton.addActionListener(e -> pegarArchivo());
        panelBotones.add(pegarButton);

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.setBackground(Color.DARK_GRAY);
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.addActionListener(e -> eliminarArchivo());
        panelBotones.add(eliminarButton);

        JButton renombrarButton = new JButton("Renombrar");
        renombrarButton.setBackground(Color.DARK_GRAY);
        renombrarButton.setForeground(Color.WHITE);
        renombrarButton.addActionListener(e -> renombrarArchivo());
        panelBotones.add(renombrarButton);

        add(panelBotones, BorderLayout.SOUTH); // Agregar la barra de botones
    }

    // Agregar botones al panel lateral con colores personalizados
    private void agregarBotonLateral(JPanel panel, String nombreBoton, Color colorFondo) {
        JButton boton = new JButton(nombreBoton);
        boton.setPreferredSize(new Dimension(150, 50));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        panel.add(boton);

        // Acción del botón para cambiar la carpeta mostrada
        boton.addActionListener(e -> mostrarContenidoCarpeta(new File(rootDirectory, nombreBoton)));
    }

    // Cargar directorios y archivos en el árbol de navegación
    private void cargarDirectorio(File directorio, DefaultMutableTreeNode node) {
    if (directorio == null || !directorio.exists()) {
        System.out.println("Directorio no válido o no existe: " + directorio);
        return;
    }
    
    File[] archivos = directorio.listFiles();
    if (archivos != null) {
        Arrays.sort(archivos);  // Ordenar archivos por nombre
        for (File archivo : archivos) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(archivo.getName());
            node.add(childNode);
            if (archivo.isDirectory()) {
                cargarDirectorio(archivo, childNode);  // Llamada recursiva para cargar subdirectorios
            }
        }
    } else {
        System.out.println("No se pudo listar archivos para: " + directorio.getAbsolutePath());
    }
}

    // Obtener la ruta desde el nodo seleccionado en el árbol
    private String getRutaDesdeNodo(DefaultMutableTreeNode node) {
        Object[] ruta = node.getUserObjectPath();
        StringBuilder rutaCompleta = new StringBuilder();
        for (Object parte : ruta) {
            rutaCompleta.append(parte.toString()).append(File.separator);
        }
        return rutaCompleta.toString();
    }

    // Mostrar el contenido de la carpeta seleccionada en la vista central
    private void mostrarContenidoCarpeta(File carpeta) {
        currentDirectory = carpeta;
        panelArchivos.removeAll();
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos);
            for (File archivo : archivos) {
                JLabel label = new JLabel(archivo.getName());
                label.setForeground(Color.WHITE); // Texto en blanco
                label.setHorizontalTextPosition(SwingConstants.CENTER);
                label.setVerticalTextPosition(SwingConstants.BOTTOM);
                panelArchivos.add(label);
            }
        }
        panelArchivos.revalidate();
        panelArchivos.repaint();
    }

    // Copiar un archivo
    private void copiarArchivo() {
        String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a copiar:");
        if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
            File archivo = new File(currentDirectory, nombreArchivo);
            if (archivo.exists()) {
                archivoCopiado = archivo;
                JOptionPane.showMessageDialog(this, "Archivo '" + nombreArchivo + "' copiado.");
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no existe.");
            }
        }
    }

    // Pegar un archivo copiado
    private void pegarArchivo() {
        if (archivoCopiado != null) {
            File archivoDestino = new File(currentDirectory, archivoCopiado.getName());
            try {
                Files.copy(archivoCopiado.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(this, "Archivo pegado en " + currentDirectory.getName());
                mostrarContenidoCarpeta(currentDirectory);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al pegar el archivo.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ningún archivo copiado.");
        }
    }

    // Eliminar un archivo
    private void eliminarArchivo() {
        String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a eliminar:");
        if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
            File archivo = new File(currentDirectory, nombreArchivo);
            if (archivo.exists()) {
                if (archivo.delete()) {
                    JOptionPane.showMessageDialog(this, "Archivo '" + nombreArchivo + "' eliminado.");
                    mostrarContenidoCarpeta(currentDirectory);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el archivo.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no existe.");
            }
        }
    }

    // Renombrar un archivo
    private void renombrarArchivo() {
        String nombreViejo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a renombrar:");
        String nombreNuevo = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre del archivo:");
        if (nombreViejo != null && nombreNuevo != null && !nombreViejo.trim().isEmpty() && !nombreNuevo.trim().isEmpty()) {
            File archivoViejo = new File(currentDirectory, nombreViejo);
            File archivoNuevo = new File(currentDirectory, nombreNuevo);
            if (archivoViejo.exists()) {
                if (archivoViejo.renameTo(archivoNuevo)) {
                    JOptionPane.showMessageDialog(this, "Archivo renombrado con éxito.");
                    mostrarContenidoCarpeta(currentDirectory);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo renombrar el archivo.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no existe.");
            }
        }
    }

    public static void abrirNavegadorParaUsuario(File carpetaUsuario) {
        SwingUtilities.invokeLater(() -> {
            new NavegadorArchivosUsuario(carpetaUsuario).setVisible(true);
        });
    }
}
