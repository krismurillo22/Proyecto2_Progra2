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
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class FuncionesUsers {
    private RandomAccessFile users;
    
    public FuncionesUsers(){
        try{
            File mf = new File("Instagram");
            mf.mkdir();
            users=new RandomAccessFile("Instagram/users.ins","rw");
        }catch(IOException e){
            System.out.println("Error");
        }
    }
    
    public boolean buscar(String user)throws IOException{
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            long position = users.getFilePointer();
            String username = users.readUTF();
            users.readUTF();
            users.readUTF();
            users.readChar();
            users.readInt();
            users.readLong();
            boolean estado=users.readBoolean();
            if (username.equals(user) && estado == true) {
                users.seek(position);
                return true;
            }
        }
        return false;
    }
    
    public boolean buscarLogin(String user, String contra)throws IOException{
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            long position = users.getFilePointer();
            String username = users.readUTF();
            users.readUTF();
            String password=users.readUTF();
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
    public boolean agregarCuenta(String name, String password, char genero, String user, int edad)throws IOException{
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
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            long position = users.getFilePointer();
            String user = users.readUTF();
            users.readUTF();
            String pass=users.readUTF();
            users.readChar();
            users.readInt();
            users.readLong();
            boolean estado=users.readBoolean();
            
            if (user.equals(username) && pass.equals(password)) {
                users.seek(position);
                users.writeBoolean(!estado);
                System.out.println(estado);
                return true;
            }
        }
        return false;
    }
    
    public boolean EstadoCuenta(String username)throws IOException{
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            long position = users.getFilePointer();
            String user = users.readUTF();
            users.readUTF();
            users.readUTF();
            users.readChar();
            users.readInt();
            users.readLong();
            boolean estado=users.readBoolean();
            
            if (user.equals(username) && estado == true) {
                users.seek(position);
                return true;
            }
        }
        return false;
    }
    
    private String userFolder(String username){
        return "Instagram/"+username;
        
    }
    
    private void createUserFolders(String username)throws IOException{
        File edir=new File(userFolder(username));
        edir.mkdir();
        //Crear archivo del empleado
        this.followingFile(username);
        this.followersFile(username);
        this.instaFile(username);
    }
    
    private RandomAccessFile followingFile(String user)throws IOException{
        String dirPadre=userFolder(user);
        String path=dirPadre+"/following.ins";
        return new RandomAccessFile(path,"rw");
    }
    
    private RandomAccessFile followersFile(String user)throws IOException{
        String dirPadre=userFolder(user);
        String path=dirPadre+"/followers.ins";
        return new RandomAccessFile(path,"rw");
    }
    
    private RandomAccessFile instaFile(String user)throws IOException{
        String dirPadre=userFolder(user);
        String path=dirPadre+"/insta.ins";
        return new RandomAccessFile(path,"rw");
    }
    
    public void guardarImagen(File archivoOriginal, String username) throws IOException {
        File directorio = new File("Instagram/imagenes");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        if (archivoOriginal != null && getExtension(archivoOriginal.getName()).equalsIgnoreCase(".png")) {
            String nombreArchivo = username + ".png";
            Files.copy(archivoOriginal.toPath(), new File(directorio, nombreArchivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
            guardarRutaImagen(new File(directorio, nombreArchivo).getPath(), username);
        } else if (archivoOriginal == null) {
            String nombreArchivo = username + ".png";
            File imagenPredeterminada = new File("src/Iconos/FotoPerfilInsta.png");
            Files.copy(imagenPredeterminada.toPath(), new File(directorio, nombreArchivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
            guardarRutaImagen(new File(directorio, nombreArchivo).getPath(), username);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, sube un archivo en formato PNG.", "Formato Incorrecto", JOptionPane.ERROR_MESSAGE);
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

    private void guardarRutaImagen(String rutaImagen, String username) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile("Instagram/imagenes/imagenesPerfil.ins", "rw")) {
            raf.seek(raf.length());
            raf.writeUTF(username);
            raf.writeUTF(rutaImagen);
        }
    }
    
    public void leerRutasImagenes() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile("Instagram/imagenes/imagenesPerfil.ins", "r")) {
            while (raf.getFilePointer() < raf.length()) {
                String username = raf.readUTF();
                String rutaImagen = raf.readUTF();
            }
        }
    }
}
