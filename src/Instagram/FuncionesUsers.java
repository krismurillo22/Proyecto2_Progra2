/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author User
 */
public class FuncionesUsers {

    private RandomAccessFile users, posts;

    public FuncionesUsers() {
        try {
            File mf = new File("Instagram");
            mf.mkdir();
            users = new RandomAccessFile("Instagram/users.ins", "rw");
            posts = new RandomAccessFile("Instagram/posts.ins", "rw");
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public boolean buscar(String user) throws IOException {
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            long position = users.getFilePointer();
            String username = users.readUTF();
            users.readUTF();
            users.readUTF();
            users.readChar();
            users.readInt();
            users.readLong();
            boolean estado = users.readBoolean();
            if (username.equals(user) && estado == true) {
                users.seek(position);
                return true;
            }
        }
        return false;
    }

    public boolean buscarLogin(String user, String contra) throws IOException {
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            long position = users.getFilePointer();
            String username = users.readUTF();
            users.readUTF();
            String password = users.readUTF();
            users.readChar();
            users.readInt();
            users.readLong();
            users.readBoolean();
            if (username.equals(user) && password.equals(contra)) {
                users.seek(position);
                return true;
            }
        }
        return false;
    }

    /*
    Formato users.ins
    
    String usuario
    String nombre
    String contra
    char genero
    int  edad
    Date entrada
    boolean estado
    
     */
    public boolean agregarCuenta(String name, String password, char genero, String user, int edad) throws IOException {
        if (buscar(user) == false) {
            users.seek(users.length());
            users.writeUTF(user); //usuario
            users.writeUTF(name);//Nombre
            users.writeUTF(password);//contrasena
            users.writeChar(genero);//genero
            users.writeInt(edad);//edad
            users.writeLong(Calendar.getInstance().getTimeInMillis());//Fecha de entrada
            users.writeBoolean(true);//estado
            createUserFolders(user);
            return true;
        }
        return false;
    }

    //revisar
    public boolean DesActivarCuenta(String username, String password) throws IOException {
        if (EstadoCuenta(username)) {
            String user = users.readUTF();
            users.readUTF();
            String pass = users.readUTF();
            users.readChar();
            users.readInt();
            users.readLong();
            long position = users.getFilePointer();
            boolean estado = users.readBoolean();
            if (user.equals(username) && pass.equals(password)) {
                users.seek(position);
                users.writeBoolean(false);
                System.out.println(estado);
                System.out.println("False");
                return true;
            }

        } else {
            users.seek(0);
            while (users.getFilePointer() < users.length()) {
                String user = users.readUTF();
                users.readUTF();
                String pass = users.readUTF();
                users.readChar();
                users.readInt();
                users.readLong();
                long position = users.getFilePointer();
                boolean estado = users.readBoolean();

                if (user.equals(username) && pass.equals(password)) {
                    users.seek(position);
                    users.writeBoolean(true);
                    System.out.println(estado);
                    System.out.println(true);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean EstadoCuenta(String username) throws IOException {
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            long position = users.getFilePointer();
            String user = users.readUTF();
            users.readUTF();
            users.readUTF();
            users.readChar();
            users.readInt();
            users.readLong();
            boolean estado = users.readBoolean();

            if (user.equals(username) && estado == true) {
                users.seek(position);
                return true;
            }
        }
        return false;
    }

    public String userFolder(String username) {
        return "Instagram/" + username;

    }

    private void createUserFolders(String username) throws IOException {
        File edir = new File(userFolder(username));
        edir.mkdir();
        //Crear archivo del empleado
        this.followingFile(username);
        this.followersFile(username);
        this.instaFile(username);
        //this.contadorUser(username);
        File mkdir = new File(userFolder(username) + "/posts");
        mkdir.mkdirs();
    }

    /*
    Formato following y followers
    String username
    boolean estado (si dejo de seguir o no)
     */
    private RandomAccessFile followingFile(String user) throws IOException {
        String dirPadre = userFolder(user);
        String path = dirPadre + "/following.ins";
        return new RandomAccessFile(path, "rw");
    }

    private RandomAccessFile followersFile(String user) throws IOException {
        String dirPadre = userFolder(user);
        String path = dirPadre + "/followers.ins";
        return new RandomAccessFile(path, "rw");
    }

    private RandomAccessFile instaFile(String user) throws IOException {
        String dirPadre = userFolder(user);
        String path = dirPadre + "/insta.ins";
        return new RandomAccessFile(path, "rw");
    }

    public int obtenerContador(String user, int index) throws IOException {
        try (RandomAccessFile contador = contadorUser(user)) {
            long posicion = 0;
            switch (index) {
                case 0: // followers
                    posicion = 0;
                    break;
                case 1: // followings
                    posicion = 4;
                    break;
                case 2: // posts
                    posicion = 8;
                    break;
                default:
                    throw new IllegalArgumentException("Índice no válido: " + index);
            }

            if (posicion >= contador.length()) {
                throw new IOException("El archivo es más corto de lo esperado.");
            }

            contador.seek(posicion);
            return contador.readInt();
        }
    }

    public void SeguirUser(String userSigue, String userASeguir) throws IOException {
        RandomAccessFile userSigueF = this.followingFile(userSigue);
        RandomAccessFile userASeguirF = this.followersFile(userASeguir);

        if (verificarSiSigue(userSigue, userASeguir)) {
            userSigueF.seek(0);
            while (userSigueF.getFilePointer() < userSigueF.length()) {
                String userSigueS = userSigueF.readUTF();
                long pos = userSigueF.getFilePointer();
                boolean estado = userSigueF.readBoolean();

                if (userSigueS.equals(userASeguir) && estado) {
                    JOptionPane.showMessageDialog(null, "Se dejó de seguir a " + userASeguir, "", JOptionPane.INFORMATION_MESSAGE);
                    userSigueF.seek(pos);
                    userSigueF.writeBoolean(false);
                    this.incrementarContador(userSigue, 1, -1);
                }
            }

            userASeguirF.seek(0);
            while (userASeguirF.getFilePointer() < userASeguirF.length()) {
                String usuario = userASeguirF.readUTF();
                long pos = userASeguirF.getFilePointer();
                boolean seguidor = userASeguirF.readBoolean();

                if (usuario.equals(userSigue) && seguidor) {
                    userASeguirF.seek(pos);
                    userASeguirF.writeBoolean(false);
                    this.incrementarContador(userASeguir, 0, -1);
                }
            }
        } else {
            userSigueF.seek(userSigueF.length());
            userSigueF.writeUTF(userASeguir);
            userSigueF.writeBoolean(true);
            JOptionPane.showMessageDialog(null, "Se empezó a seguir a " + userASeguir, "", JOptionPane.INFORMATION_MESSAGE);
            this.incrementarContador(userSigue, 1, 1);

            boolean yaEsSeguidor = false;
            userASeguirF.seek(0);
            while (userASeguirF.getFilePointer() < userASeguirF.length()) {
                String usuario = userASeguirF.readUTF();
                boolean seguidor = userASeguirF.readBoolean();
                if (usuario.equals(userSigue) && seguidor) {
                    yaEsSeguidor = true;
                    break;
                }
            }

            if (!yaEsSeguidor) {
                userASeguirF.seek(userASeguirF.length());
                userASeguirF.writeUTF(userSigue);
                userASeguirF.writeBoolean(true);
                this.incrementarContador(userASeguir, 0, 1);
            }
        }
    }

    public boolean verificarSiSigue(String userSigue, String userASeguir) throws IOException {
        try (RandomAccessFile userSigueF = this.followingFile(userSigue)) {
            userSigueF.seek(0);
            while (userSigueF.getFilePointer() < userSigueF.length()) {
                String usuario = userSigueF.readUTF();
                boolean sigue = userSigueF.readBoolean();
                if (usuario.equals(userASeguir) && sigue) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    Formato insta.ins
    
    String dueño del post
    String path del post
    String userQueLoEscribio
    String comentario
    Long fechaComentario
     */
    public void Comentarios(String duenoPost, String pathPost, String username, String comentario) throws IOException {
        RandomAccessFile userFile = this.instaFile(duenoPost);
        if (buscar(username) != false) {
            userFile.seek(userFile.length());
            userFile.writeUTF(duenoPost); //dueno del post
            userFile.writeUTF(pathPost); //path del post
            userFile.writeUTF(username); //userQueLoEscribio
            userFile.writeUTF(comentario); //comentario
            userFile.writeLong(Calendar.getInstance().getTimeInMillis()); //Fecha
        }
    }

    public void mostrarComentarios(String pathPost, JTextArea txtAreaComentarios, String username) {
        try {
            RandomAccessFile userFile = this.instaFile(username);
            ArrayList<String> comentarios = new ArrayList<>();
            ArrayList<Long> fechas = new ArrayList<>();
            while (userFile.getFilePointer() < userFile.length()) {
                String dueno = userFile.readUTF();
                String postPath = userFile.readUTF();
                String user = userFile.readUTF();
                String comentario = userFile.readUTF();
                long fecha = userFile.readLong();

                if (postPath.equals(pathPost)) {
                    comentarios.add("\n@" + user + " escribió: \n" + comentario);
                    fechas.add(fecha);
                }
            }

            for (int i = 0; i < fechas.size(); i++) {
                for (int j = i + 1; j < fechas.size(); j++) {
                    if (fechas.get(j) > fechas.get(i)) {
                        long tempFecha = fechas.get(i);
                        fechas.set(i, fechas.get(j));
                        fechas.set(j, tempFecha);
                        String tempComentario = comentarios.get(i);
                        comentarios.set(i, comentarios.get(j));
                        comentarios.set(j, tempComentario);
                    }
                }
            }
            txtAreaComentarios.setText("");
            SimpleDateFormat formatoBonito = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
            for (int i = 0; i < comentarios.size(); i++) {
                Date date = new Date(fechas.get(i));
                String fechaFormateada = formatoBonito.format(date);
                txtAreaComentarios.append(comentarios.get(i) + " - " + fechaFormateada + "\n");
            }

            SwingUtilities.invokeLater(() -> {
                JScrollPane scrollPane = (JScrollPane) txtAreaComentarios.getParent().getParent();
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMinimum());
            });
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al leer comentarios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String imprimirUser(String user) throws IOException {
        if (buscar(user)) {
            String username = users.readUTF();
            String nombre = users.readUTF();
            users.readUTF();
            char genero = users.readChar();
            int edad = users.readInt();
            Long fecha = users.readLong();
            boolean estado = users.readBoolean();
            if (user.equals(username) && estado == true) {
                String generoS = (genero == 'M') ? "Masculino" : "Femenino";
                Date date = new Date(fecha);
                SimpleDateFormat formatoBonito = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
                String fechaFormateada = formatoBonito.format(date);
                return "\n> Nombre: " + nombre + "\n> Genero: " + generoS + "\n> Edad: " + edad + "\n> Fecha de Ingreso: " + fechaFormateada;
            }
        }
        return "No contiene informacion dicho user.";
    }

    private RandomAccessFile contadorUser(String user) throws IOException {
        String dirPadre = userFolder(user);
        String path = dirPadre + "/contador.ins";
        inicializarContador(user, path);
        return new RandomAccessFile(path, "rw");
    }

    /*
    Formato contador.ins
    
    int cantidadFollowers
    int cantidadFollowings
    int cantidadPosts
     */
    private void inicializarContador(String user, String path) throws IOException {
        RandomAccessFile contador = new RandomAccessFile(path, "rw");
        if (contador.length() == 0) {
            contador.writeInt(0);
            contador.writeInt(0);
            contador.writeInt(0);
        }

        contador.close();
    }

    public void incrementarContador(String user, int index, int cantidad) throws IOException {
        RandomAccessFile contador = contadorUser(user);
        long posicion = 0;
        switch (index) {
            case 0:
                posicion = 0;
                break;
            case 1:
                posicion = 4;
                break;
            case 2:
                posicion = 8;
                break;
        }
        contador.seek(posicion);
        int cantidadActual = contador.readInt();
        contador.seek(posicion);
        contador.writeInt(cantidadActual + cantidad);

    }

    //Esto es para lo de buscar usuarios
    public ArrayList<String> buscarUsuarios(String userBusqueda) throws IOException {
        ArrayList<String> resultados = new ArrayList<>();
        try (RandomAccessFile usersFile = new RandomAccessFile("Instagram/users.ins", "r")) {
            while (usersFile.getFilePointer() < usersFile.length()) {
                String username = usersFile.readUTF();
                usersFile.readUTF();
                usersFile.readUTF();
                usersFile.readChar();
                usersFile.readInt();
                usersFile.readLong();
                usersFile.readBoolean();

                if (username.contains(userBusqueda)) {
                    resultados.add(username);
                }
            }
        }
        return resultados;
    }

    public ArrayList<String> buscarComHashtag(String hashtag) throws IOException {
        ArrayList<String> comentariosConHashtag = new ArrayList<>();
        while (users.getFilePointer() < users.length()) {
            String user = users.readUTF();
            users.readUTF();
            users.readUTF();
            users.readChar();
            users.readInt();
            users.readLong();
            users.readBoolean();
            RandomAccessFile archivoComentariosUsuario = instaFile(user);

            while (archivoComentariosUsuario.getFilePointer() < archivoComentariosUsuario.length()) {
                String duenoPost = archivoComentariosUsuario.readUTF();
                archivoComentariosUsuario.readUTF();
                String userQueLoEscribio = archivoComentariosUsuario.readUTF();
                String comentario = archivoComentariosUsuario.readUTF();
                archivoComentariosUsuario.readLong();

                if (comentario.contains(hashtag)) {
                    comentariosConHashtag.add("\nUsuario: " + userQueLoEscribio + " comentó en el post de " + duenoPost + ": \n" + comentario);
                }
            }
        }
        return comentariosConHashtag;
    }

    //Foto Perfil
    public void guardarImagen(File archivoOriginal, String username) throws IOException {
        File directorio = new File("Instagram/imagenes");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        if (archivoOriginal != null) {
            String extension = getExtension(archivoOriginal.getName());
            if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")) {
                String nombreArchivo = username + extension;
                Files.copy(archivoOriginal.toPath(), new File(directorio, nombreArchivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, sube un archivo en formato PNG o JPG.", "Formato Incorrecto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            String nombreArchivo = username + ".png";
            File imagenPredeterminada = new File("src/Iconos/FotoPerfilInsta.png");
            Files.copy(imagenPredeterminada.toPath(), new File(directorio, nombreArchivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private String getExtension(String nombreArchivo) {
        int i = nombreArchivo.lastIndexOf('.');
        if (i > 0) {
            return nombreArchivo.substring(i);
        } else {
            return "";
        }
    }

    public String obtenerRutaImagen(String username) {
        File directorio = new File("Instagram/imagenes");

        File imagenPNG = new File(directorio, username + ".png");
        if (imagenPNG.exists()) {
            return imagenPNG.getAbsolutePath();
        }

        File imagenJPG = new File(directorio, username + ".jpg");
        if (imagenJPG.exists()) {
            return imagenJPG.getAbsolutePath();
        }

        File imagenPredeterminada = new File("src/Iconos/FotoPerfilInsta.png");
        return imagenPredeterminada.getAbsolutePath();
    }

    /*
    Formato posts.ins
    
    String username
    String pathImagen
     */
    public boolean agregarPost(String username, String pathImagen) throws IOException {
        if (buscar(username) != false) {
            posts.seek(posts.length());
            posts.writeUTF(username); //usuario
            posts.writeUTF(pathImagen); //url imagen
            return true;
        }
        return false;
    }

    public void guardarPost(String username, File archivoImagen) throws IOException {
        String directorio = userFolder(username + "/posts");
        File carpeta = new File(directorio);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        String extension = getExtension(archivoImagen.getName());
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")) {
            int contadorPosts = obtenerContador(username, 2);
            String nombreArchivo = "imagen_" + contadorPosts + extension;
            File archivoDestino = new File(carpeta, nombreArchivo);
            Files.copy(archivoImagen.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.incrementarContador(username, 2, 1);

            String rutaCompleta = archivoDestino.getAbsolutePath();
            System.out.println("Ruta de la imagen: " + rutaCompleta);
            if (agregarPost(username, rutaCompleta)) {
                JOptionPane.showMessageDialog(null, "Imagen subida correctamente.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Formato de archivo no soportado. Solo se permiten PNG o JPG.", "Formato Incorrecto", JOptionPane.ERROR_MESSAGE);
        }
    }

}
