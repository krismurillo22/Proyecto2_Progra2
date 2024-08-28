/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.util.Calendar;

/**
 *
 * @author User
 */
public class FormatoUsers {
    public String Nombre;
    public String username;
    public char genero;
    public String password;
    public Calendar fecha;
    public int edad;
    public boolean activo;
    
    public FormatoUsers(String nombre, String pass, char genero, String user, int edad){
        Nombre=nombre;
        username=user;
        password=pass;
        this.genero=genero;
        fecha = Calendar.getInstance();
        this.edad=edad;
        activo=true;
    }
    
    public void setUser(String user){
        username=user;
    }
    public String getUser(){
        return username;
    }
    public void setPassword(String pass){
        password=pass;
    }
    public String getPassword(){
        return password;
    }
    
    public int getEdad() {
        return edad;
    }
    
    public Calendar getFecha() {
        return fecha;
    }

    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return Nombre;
    }

    public char getGenero() {
        return genero;
    }
}
