/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class FuncionesUsers {
    private ArrayList<FormatoUsers> cuentas;
        
    public FormatoUsers buscar(String user){
        for (FormatoUsers cuenta : cuentas) {
            if (cuenta != null && cuenta.getUser().equals(user)) {
                return cuenta;
            }
        }
        return null;
    }
    
    public boolean agregarCuenta(String name, String password, char genero, String user, int edad){
        if (buscar(user) == null) {
            cuentas.add(new FormatoUsers(name, password, genero, user, edad));
            return true;
        }
        return false;
    }
    
    public boolean revisarUsuario(String user, String password){
        for (FormatoUsers cuenta : cuentas) {
            if (cuenta != null) {
                if (user.equals(cuenta.getUser()) && password.equals(cuenta.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean desactivarCuenta(String user, String password){
        for (int i = 0; i < cuentas.size(); i++) {
            FormatoUsers cuenta = cuentas.get(i);
            if (cuenta != null && user.equals(cuenta.getUser()) && password.equals(cuenta.getPassword())) {
                cuentas.get(i).setActivo(false);
                return true;
            }
        }
        return false;
    }
    
}
