package Apps;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ReproductorMusical extends JFrame {

    private Player player;
    private FileInputStream fileInputStream;
    private JButton playButton, stopButton, pauseButton, addButton, removeButton, upButton, downButton;
    private JButton prevButton, nextButton;
    private JFileChooser fileChooser;
    private JList<String> songList;
    private DefaultListModel<String> listModel;
    private ArrayList<String> filePaths;
    private JLabel nowPlayingLabel;
    private JLabel timeLabel;
    private JLabel totalTimeLabel;
    private JSlider progressSlider;
    private boolean isPaused;
    private long pauseLocation;
    private long totalLength;
    private Timer timer;
    private int elapsedSeconds;
    private int totalDurationSeconds;
    private boolean isDraggingSlider = false;
    private int currentIndex = -1;
    private boolean isSeeking = false;
    private String nombreUsuario;

    public ReproductorMusical(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        
        // Configuración básica de la ventana
        setTitle("Reproductor de Música");
        setSize(800, 500);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        // Estilo oscuro para la ventana principal
        getContentPane().setBackground(new Color(25, 20, 20));

        // Añadir borde interno para una mejor estética
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Crear botones estilizados
        addButton = crearBotonEstilizado("ADD");
        removeButton = crearBotonEstilizado("REMOVE");
        upButton = crearBotonEstilizado("UP");
        downButton = crearBotonEstilizado("DOWN");

        playButton = new JButton("▶");
        pauseButton = new JButton("⏸");
        stopButton = new JButton("■");

        prevButton = new JButton("\u23EE");
        nextButton = new JButton("\u23ED");

        // Lista de canciones y array para almacenar las rutas
        listModel = new DefaultListModel<>();
        songList = new JList<>(listModel);
        filePaths = new ArrayList<>();
        JScrollPane scrollPane = new JScrollPane(songList);
        scrollPane.setPreferredSize(new Dimension(500, 150));

        // Estilo de la lista de canciones
        songList.setBackground(new Color(40, 40, 40));
        songList.setForeground(Color.WHITE);

        // Etiqueta de "Now Playing"
        nowPlayingLabel = new JLabel("Now Playing: ");
        nowPlayingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nowPlayingLabel.setForeground(Color.WHITE);

        // Etiqueta para mostrar el tiempo transcurrido
        timeLabel = new JLabel("0:00");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(Color.WHITE);

        // Etiqueta para mostrar el tiempo total de la canción
        totalTimeLabel = new JLabel("/ 0:00");
        totalTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalTimeLabel.setForeground(Color.WHITE);

        // JSlider para el progreso de la canción
        progressSlider = new JSlider(0, 100, 0);
        progressSlider.setValue(0);
        progressSlider.setPreferredSize(new Dimension(600, 30));
        progressSlider.setBackground(new Color(25, 25, 25));
        progressSlider.setForeground(Color.GREEN);

        // Añadir el listener al slider
        progressSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!progressSlider.getValueIsAdjusting()) {
                    if (isDraggingSlider && player != null && totalDurationSeconds > 0) {
                        int sliderValue = progressSlider.getValue();
                        long newPosition = (long) ((double) sliderValue / 100 * totalLength);

                        if (newPosition < totalLength) {
                            try {
                                isSeeking = true;
                                detenerReproduccion();
                                fileInputStream = new FileInputStream(filePaths.get(currentIndex));
                                fileInputStream.skip(newPosition);
                                player = new Player(fileInputStream);
                                isDraggingSlider = false;
                                isSeeking = false;
                                startTimer();
                                new Thread(() -> {
                                    try {
                                        player.play();
                                    } catch (JavaLayerException ex) {
                                        ex.printStackTrace();
                                    }
                                }).start();
                            } catch (IOException | JavaLayerException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } else {
                    isDraggingSlider = true;
                }
            }
        });

        // Estilo de los botones de reproducción
        playButton.setBackground(new Color(30, 215, 96));
        playButton.setForeground(Color.WHITE);
        pauseButton.setBackground(Color.DARK_GRAY);
        pauseButton.setForeground(Color.WHITE);
        stopButton.setBackground(Color.DARK_GRAY);
        stopButton.setForeground(Color.WHITE);
        prevButton.setBackground(Color.DARK_GRAY);
        prevButton.setForeground(Color.WHITE);
        nextButton.setBackground(Color.DARK_GRAY);
        nextButton.setForeground(Color.WHITE);

        // Configuración del layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel superior para los botones
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(new Color(25, 20, 20));
        buttonPanel.add(addButton);
        buttonPanel.add(upButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(downButton);

        // Añadir el panel de botones al layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(buttonPanel, gbc);

        // Añadir la lista de canciones
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        // Añadir el JSlider de progreso
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        add(progressSlider, gbc);

        // Panel para tiempo transcurrido y total
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BorderLayout());
        timePanel.setBackground(new Color(25, 20, 20));
        timePanel.add(timeLabel, BorderLayout.WEST);
        timePanel.add(totalTimeLabel, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        add(timePanel, gbc);

        // Panel inferior para los controles de reproducción
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(25, 20, 20));
        controlPanel.add(prevButton);
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(nextButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        add(controlPanel, gbc);

        // Etiqueta de Now Playing
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        add(nowPlayingLabel, gbc);

        // JFileChooser para seleccionar archivos MP3 desde la carpeta del usuario
        fileChooser = new JFileChooser(new File("Z" + File.separator + nombreUsuario));

        // Acción para agregar canciones
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String fileName = file.getName();
                    listModel.addElement(fileName);
                    filePaths.add(file.getAbsolutePath());
                }
            }
        });

        // Acción para eliminar canciones
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex != -1) {
                    listModel.remove(selectedIndex);
                    filePaths.remove(selectedIndex);
                }
            }
        });

        // Acción para mover canciones hacia arriba
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex > 0) {
                    String song = listModel.getElementAt(selectedIndex);
                    String path = filePaths.get(selectedIndex);
                    listModel.remove(selectedIndex);
                    filePaths.remove(selectedIndex);
                    listModel.add(selectedIndex - 1, song);
                    filePaths.add(selectedIndex - 1, path);
                    songList.setSelectedIndex(selectedIndex - 1);
                }
            }
        });

        // Acción para mover canciones hacia abajo
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex < listModel.size() - 1) {
                    String song = listModel.getElementAt(selectedIndex);
                    String path = filePaths.get(selectedIndex);
                    listModel.remove(selectedIndex);
                    filePaths.remove(selectedIndex);
                    listModel.add(selectedIndex + 1, song);
                    filePaths.add(selectedIndex + 1, path);
                    songList.setSelectedIndex(selectedIndex + 1);
                }
            }
        });

        // Acción para reproducir la canción seleccionada
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPaused) {
                    reanudarReproduccion();
                } else {
                    int selectedIndex = songList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        currentIndex = selectedIndex;
                        String filePath = filePaths.get(selectedIndex);
                        reproducirArchivo(filePath);
                    }
                }
            }
        });

        // Acción para pausar la reproducción
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausarReproduccion();
            }
        });

        // Acción para detener la reproducción
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detenerReproduccion();
            }
        });

        // Acción para reproducir la canción anterior
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    reproducirArchivo(filePaths.get(currentIndex));
                    songList.setSelectedIndex(currentIndex);
                }
            }
        });

        // Acción para reproducir la siguiente canción
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < listModel.size() - 1) {
                    currentIndex++;
                    reproducirArchivo(filePaths.get(currentIndex));
                    songList.setSelectedIndex(currentIndex);
                }
            }
        });

        setVisible(true);
    }

    // Método para crear un botón estilizado
    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(30, 215, 96));
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setBorderPainted(false);

        // Bordes redondeados para los botones
        boton.setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96), 2, true));

        // Añadir efecto hover (cambio de color al pasar el ratón)
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(50, 235, 116));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(30, 215, 96));
            }
        });

        return boton;
    }

    // Métodos para manejar la reproducción

    // Método para pausar la reproducción
// Método para pausar la reproducción
// Método para pausar la reproducción
private void pausarReproduccion() {
    if (player != null) {
        player.close();  // Detener el reproductor
        isPaused = true;  // Marcar que estamos en pausa
        stopTimer();  // Detener el temporizador
    }
}





    // Método para detener la reproducción
    private void detenerReproduccion() {
    if (player != null) {
        player.close();
        isPaused = false;
        nowPlayingLabel.setText("Now Playing: ");
        progressSlider.setValue(0);
        timeLabel.setText("0:00");
        stopTimer();  // Detener el temporizador cuando se detiene la canción
    }

    // Cerramos el FileInputStream solo cuando detenemos completamente
    try {
        if (fileInputStream != null) {
            fileInputStream.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    // Método para reanudar la reproducción desde donde se pausó
// Método para reanudar la reproducción desde donde se pausó
private void reanudarReproduccion() {
    try {
        // Reabrir el flujo desde el archivo, ya que el flujo anterior se cerró
        fileInputStream = new FileInputStream(filePaths.get(currentIndex));
        fileInputStream.skip(totalLength - pauseLocation);  // Saltar a la posición guardada
        player = new Player(fileInputStream);  // Crear un nuevo reproductor
        isPaused = false;

        new Thread(() -> {
            try {
                player.play();
            } catch (JavaLayerException ex) {
                ex.printStackTrace();
            }
        }).start();
        startTimer();  // Reiniciar el temporizador
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}





    // Método para reproducir el archivo MP3
    private void reproducirArchivo(String ruta) {
        try {
            detenerReproduccion();
            fileInputStream = new FileInputStream(ruta);
            totalLength = new File(ruta).length();
            player = new Player(fileInputStream);
            nowPlayingLabel.setText("Now Playing: " + new File(ruta).getName());
            totalDurationSeconds = obtenerDuracionArchivoMP3(ruta);
            totalTimeLabel.setText("/ " + formatTime(totalDurationSeconds));
            startTimer();

            new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException ex) {
                    ex.printStackTrace();
                }
            }).start();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la duración de un archivo MP3
    private int obtenerDuracionArchivoMP3(String ruta) {
        try (FileInputStream fis = new FileInputStream(ruta)) {
            Bitstream bitstream = new Bitstream(fis);
            Header header = bitstream.readFrame();
            int bitrate = header.bitrate();
            int fileSize = (int) new File(ruta).length();
            int durationInSeconds = (fileSize * 8) / bitrate;
            return durationInSeconds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    
// Iniciar el temporizador para actualizar el tiempo de la canción y el JSlider
// Iniciar el temporizador para actualizar el tiempo de la canción y el JSlider
private void startTimer() {
    timer = new Timer(1000, e -> {
        if (!isSeeking && !isPaused) {  // Solo actualizar si no estamos buscando en el slider ni en pausa
            elapsedSeconds++;
            int minutes = elapsedSeconds / 60;
            int seconds = elapsedSeconds % 60;
            timeLabel.setText(String.format("%d:%02d", minutes, seconds));

            int progress = (int) ((double) elapsedSeconds / totalDurationSeconds * 100);
            progressSlider.setValue(Math.min(progress, 100));
        }
    });
    timer.start();
}



    // Detener el temporizador cuando la canción se detiene o pausa
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    // Método para formatear el tiempo en minutos y segundos
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public static void main(String[] args) {
        String nombreUsuario = "nombreDelUsuario";  // Debes definir correctamente el nombre de usuario aquí.
        ReproductorMusical reproductor = new ReproductorMusical(nombreUsuario);
        reproductor.setLocationRelativeTo(null);
        reproductor.setVisible(true);
    }
}
