package proyecto2_miniwindow;

import java.io.*;
import java.util.*;

public class SistemaArchivos {
    private List<Usuarios> usuarios;
    private Usuarios usuarioActual;
    private static final String ROOT_DIRECTORY = System.getProperty("user.dir") + File.separator + "Z";  // Directorio raíz llamado Z

    public SistemaArchivos() {
        usuarios = new ArrayList<>();
        verificarDirectorioRaiz();  // Verificar que el directorio Z exista o crearlo
        cargarUsuarios();  // Cargar los usuarios existentes desde el archivo
        crearAdminPorDefecto();  // Crear el usuario administrador por defecto si no existe
    }

    // Verificar que el directorio raíz Z exista, si no, crearlo
    private void verificarDirectorioRaiz() {
        File rootDir = new File(ROOT_DIRECTORY);
        if (!rootDir.exists()) {
            if (rootDir.mkdirs()) {
                System.out.println("Directorio raíz Z creado exitosamente.");
            } else {
                System.out.println("Error al crear el directorio raíz Z.");
            }
        }
    }

    // Crear el usuario administrador por defecto si no existe
    private void crearAdminPorDefecto() {
        if (!usuarioExiste("admin")) {
            crearUsuario("admin", "admin123");  // Crear un administrador con contraseña por defecto
            System.out.println("Usuario administrador creado por defecto.");
        }
    }

    // Crear un nuevo usuario y su subdirectorio en el directorio raíz Z
    public void crearUsuario(String username, String password) {
        File userDir = new File(ROOT_DIRECTORY + File.separator + username);  // Crear directorio del usuario en Z
        if (!userDir.exists()) {
            userDir.mkdirs();  // Crear el directorio raíz del usuario
            crearDirectoriosBasicos(userDir);  // Crear las carpetas básicas (Mis Documentos, Música, Mis Imágenes)
        }
        Usuarios nuevoUsuario = new Usuarios(username, password);
        usuarios.add(nuevoUsuario);
        guardarUsuarios();  // Guardar la lista de usuarios actualizada
    }

    // Método para crear las carpetas básicas dentro del directorio del usuario
    private void crearDirectoriosBasicos(File userDir) {
        new File(userDir, "Mis Documentos").mkdirs();
        new File(userDir, "Música").mkdirs();
        new File(userDir, "Mis Imágenes").mkdirs();
        System.out.println("Carpetas básicas creadas en " + userDir.getAbsolutePath());
    }

    // Verificar las credenciales del usuario
    public boolean verificarUsuario(String username, String password) {
        for (Usuarios usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.verificarPassword(password)) {
                usuarioActual = usuario;  // Almacena el usuario actual
                return true;
            }
        }
        return false;
    }

    // Cargar usuarios desde un archivo
    private void cargarUsuarios() {
        File archivoUsuarios = new File(ROOT_DIRECTORY + File.separator + "usuarios.dat");
        if (archivoUsuarios.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoUsuarios))) {
                usuarios = (List<Usuarios>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar usuarios: " + e.getMessage());
            }
        }
    }

    // Guardar usuarios en un archivo
    private void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ROOT_DIRECTORY + File.separator + "usuarios.dat"))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    // Verificar si un usuario ya existe
    public boolean usuarioExiste(String username) {
        for (Usuarios usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Obtener el usuario actual (después de iniciar sesión)
    public Usuarios getUsuarioActual() {
        return usuarioActual;
    }
}
