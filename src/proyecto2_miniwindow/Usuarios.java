package proyecto2_miniwindow;

import java.io.Serializable;

public class Usuarios implements Serializable {
    private String username;
    private String password;

    public Usuarios(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Método para obtener el nombre de usuario
    public String getUsername() {
        return username;
    }

    // Método para verificar la contraseña
    public boolean verificarPassword(String password) {
        return this.password.equals(password);
    }
}
