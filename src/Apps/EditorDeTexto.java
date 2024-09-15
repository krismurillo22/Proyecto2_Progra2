package Apps;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class EditorDeTexto extends JFrame {

    private JTextPane textPane;
    private JComboBox<String> fontComboBox;
    private JComboBox<Integer> sizeComboBox;
    private DefaultStyledDocument doc;
    private UndoManager undoManager;
    private JPanel colorPanel;
    private ArrayList<Color> selectedColors;
    private String nombreUsuario;
    private File currentFile;

    public EditorDeTexto(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        setTitle("Editor de texto");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        undoManager = new UndoManager();
        selectedColors = new ArrayList<>();
        initComponents();
        createMenuBar();  // Llamamos al método para crear la barra de menú
    }

    private void initComponents() {
        textPane = new JTextPane();
        doc = new DefaultStyledDocument();
        textPane.setDocument(doc);
        textPane.getDocument().addUndoableEditListener(undoManager);
        JScrollPane scrollPane = new JScrollPane(textPane);

        // Barra de herramientas
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Botones de cortar, copiar, pegar, deshacer y rehacer
        JButton cutButton = new JButton(new DefaultEditorKit.CutAction());
        cutButton.setText("Cortar");

        JButton copyButton = new JButton(new DefaultEditorKit.CopyAction());
        copyButton.setText("Copiar");

        JButton pasteButton = new JButton(new DefaultEditorKit.PasteAction());
        pasteButton.setText("Pegar");

        JButton undoButton = new JButton("Deshacer");
        undoButton.addActionListener(e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        });

        JButton redoButton = new JButton("Rehacer");
        redoButton.addActionListener(e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        });

        // Botones de formato de texto
        JButton boldButton = new JButton(new StyledEditorKit.BoldAction());
        boldButton.setText("B");
        boldButton.setFont(new Font("Serif", Font.BOLD, 16));

        JButton italicButton = new JButton(new StyledEditorKit.ItalicAction());
        italicButton.setText("I");
        italicButton.setFont(new Font("Serif", Font.ITALIC, 16));

        JButton underlineButton = new JButton(new StyledEditorKit.UnderlineAction());
        underlineButton.setText("U");
        underlineButton.setFont(new Font("Serif", Font.PLAIN, 16));

        // Botones de alineación de texto
        JButton alignLeftButton = new JButton(new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT));
        alignLeftButton.setText("Izquierda");

        JButton alignCenterButton = new JButton(new StyledEditorKit.AlignmentAction("Centro", StyleConstants.ALIGN_CENTER));
        alignCenterButton.setText("Centro");

        JButton alignRightButton = new JButton(new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT));
        alignRightButton.setText("Derecha");

        JButton justifyButton = new JButton(new StyledEditorKit.AlignmentAction("Justificar", StyleConstants.ALIGN_JUSTIFIED));
        justifyButton.setText("Justificar");

        // ComboBox para fuentes
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fonts);
        fontComboBox.setSelectedItem("Lucida Calligraphy");
        fontComboBox.setMaximumSize(new Dimension(250, 30));
        fontComboBox.addActionListener(e -> setFontStyle());

        // ComboBox para tamaño
        Integer[] sizes = {42, 48, 64, 92, 144, 190, 240, 300};
        sizeComboBox = new JComboBox<>(sizes);
        sizeComboBox.setSelectedItem(48);
        sizeComboBox.setMaximumSize(new Dimension(100, 30));
        sizeComboBox.addActionListener(e -> setFontStyle());

        // Panel de selección de colores
        colorPanel = new JPanel(new GridLayout(2, 8, 2, 2));
        colorPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        colorPanel.setPreferredSize(new Dimension(300, 50));

        // Inicializar colorPanel con botones vacíos
        for (int i = 0; i < 16; i++) {
            JButton emptyButton = new JButton();
            emptyButton.setBackground(Color.WHITE);
            emptyButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            colorPanel.add(emptyButton);
        }

        // Botón para elegir color personalizado
        JButton colorChooserButton = new JButton("Elegir Color");
        colorChooserButton.addActionListener(e -> elegirColor());

        // Crear un panel lateral izquierdo para las etiquetas y combo boxes
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 1, 5, 5));
        leftPanel.add(new JLabel("Fuente"));
        leftPanel.add(new JLabel("Tamaño"));
        leftPanel.add(new JLabel("Color"));

        // Crear un panel para los combo boxes y añadirlos con su layout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1, 5, 5));
        centerPanel.add(fontComboBox);
        centerPanel.add(sizeComboBox);
        centerPanel.add(colorChooserButton);

        // Añadir los componentes a la barra de herramientas de manera ordenada
        toolBar.add(cutButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);
        toolBar.add(undoButton);
        toolBar.add(redoButton);
        toolBar.addSeparator();

        toolBar.add(boldButton);
        toolBar.add(italicButton);
        toolBar.add(underlineButton);
        toolBar.addSeparator();

        toolBar.add(alignLeftButton);
        toolBar.add(alignCenterButton);
        toolBar.add(alignRightButton);
        toolBar.add(justifyButton);
        toolBar.addSeparator();

        toolBar.add(leftPanel);
        toolBar.add(centerPanel);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(new JLabel("Colores utilizados: "));
        toolBar.add(colorPanel);

        // Añadir componentes al JFrame
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Botones Aceptar y Cancelar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton acceptButton = new JButton("Aceptar");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Método para crear la barra de menú con las opciones de Guardar, Nuevo y Abrir
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");

        // Opción "Nuevo"
        JMenuItem newItem = new JMenuItem("Nuevo");
        newItem.addActionListener(e -> nuevoDocumento());
        fileMenu.add(newItem);

        // Opción "Abrir"
        JMenuItem openItem = new JMenuItem("Abrir");
        openItem.addActionListener(e -> abrirDocumento());
        fileMenu.add(openItem);

        // Opción "Guardar"
        JMenuItem saveItem = new JMenuItem("Guardar");
        saveItem.addActionListener(e -> guardarDocumento());
        fileMenu.add(saveItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void nuevoDocumento() {
        textPane.setText("");  // Limpia el texto para un nuevo documento
        currentFile = null;    // No hay archivo actual al crear un nuevo documento
        setTitle("Editor de texto - Nuevo Documento");
    }

   private void abrirDocumento() {
        JFileChooser fileChooser = new JFileChooser(new File("Z" + File.separator + nombreUsuario));  // Ruta del usuario
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto", "txt");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textPane.read(reader, null);
                setTitle("Editor de texto - " + currentFile.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void guardarDocumento() {
        if (currentFile == null) {
            JFileChooser fileChooser = new JFileChooser(new File("Z" + File.separator + nombreUsuario));  // Ruta del usuario
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto", "txt");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showSaveDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
            }
        }

        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textPane.write(writer);
                setTitle("Editor de texto - " + currentFile.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void elegirColor() {
        Color newColor = JColorChooser.showDialog(this, "Seleccionar Color", Color.BLACK);
        if (newColor != null) {
            selectedColors.add(newColor);
            actualizarPanelDeColores();
            setTextColor(newColor);
        }
    }

    private void actualizarPanelDeColores() {
        colorPanel.removeAll();
        int colorCount = 0;
        for (Color color : selectedColors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            colorButton.addActionListener(e -> setTextColor(color));
            colorPanel.add(colorButton);
            colorCount++;
            if (colorCount >= 16) break; // Limitar a 16 colores
        }

        // Rellenar los espacios vacíos con botones en blanco
        for (int i = colorCount; i < 16; i++) {
            JButton emptyButton = new JButton();
            emptyButton.setBackground(Color.WHITE);
            emptyButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            colorPanel.add(emptyButton);
        }

        colorPanel.revalidate();
        colorPanel.repaint();
    }

    private void setFontStyle() {
        String selectedFont = (String) fontComboBox.getSelectedItem();
        int selectedSize = (Integer) sizeComboBox.getSelectedItem();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributes, selectedFont);
        StyleConstants.setFontSize(attributes, selectedSize);
        doc.setCharacterAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attributes, false);
    }

    private void setTextColor(Color color) {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, color);
        doc.setCharacterAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attributes, false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String nombreUsuario = "nombreDelUsuario";  // Debes definir correctamente el nombre de usuario aquí.
            EditorDeTexto editor = new EditorDeTexto(nombreUsuario);
            editor.setVisible(true);
        });
    }
}
