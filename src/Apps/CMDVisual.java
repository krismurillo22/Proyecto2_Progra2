/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Apps;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author User
 */
public class CMDVisual extends JFrame{
    CMDFunciones funciones;
    private JTextArea TextAreaCMD;
    private JScrollPane jScrollPane1;  
    
    public CMDVisual() {
        initGui();
        funciones = new CMDFunciones(System.getProperty("user.dir"));
        TextAreaCMD.setText("Microsoft Windows [Version 1.0.0.0]\n" + "(c) Kristian Murillo, Victoria Murillo. All rights reserved.\n\n" 
                    + funciones.getPathActual()+">");
    } 
    
    private void imprimir(String mensaje) {
        String newText = TextAreaCMD.getText();
        newText += "\n"+mensaje;
        TextAreaCMD.setText(newText);
    }
    
    private void Funciones() {
        String textnormal = "";
        String textoalrevez = "";
        String textCMD = TextAreaCMD.getText();    
        for (int i = textCMD.length() - 1; i >= 0; i--) {
            if (textCMD.charAt(i) == '>') {
                break;
            }
            textoalrevez += textCMD.charAt(i);//Lo pone del final al inicio
        }
        for (int i = textoalrevez.length()-1;i >= 0; i--) {
            textnormal += textoalrevez.charAt(i);//Lo pone ya normal
        }
        
        String comandos[] = textnormal.trim().split(" ");
        
        switch (comandos[0].toLowerCase()) {
            case "mkdir":
                if (comandos.length == 1) {
                    imprimir("Ingrese un directorio existente.");
                    break;
                }
                imprimir(funciones.Mkdir(funciones.getPathActual()+"/"+comandos[1]));
                break;
            
            case "mfile":
                if (comandos.length == 1) {
                    imprimir("Ingrese el nombre del archivo.");
                    break;
                }
                imprimir(funciones.Mfile(funciones.getPathActual() + "/" + comandos[1]));
                break;
                
            case "rm":
                if (comandos.length == 1) {
                    imprimir("Ingrese que desea eliminar.");
                    break;
                }
                File destino = new File(funciones.getPathActual() + "/" + comandos[1]);
                imprimir(funciones.Rm(destino));
                break;
                
            case "cd":
                if (comandos.length == 1) {
                    imprimir("Ingrese un directorio para cambiar.");
                    break;
                }
                funciones.Cd(comandos[1]);
                break;
                
            case "...":
                funciones.Cd("..");
                break;
                
            case "dir" :
                imprimir(funciones.Dir(funciones.getPathActual()));
                break;
             
            case "date":
                imprimir(funciones.Date());
                break;
                
            case "time":
                imprimir(funciones.Time());
                break;
                
            case "wr":
                if (comandos.length < 3) {
                    imprimir("Ingrese el archivo y contenido que desea escribir: \nEjemplo: wr <archivo> <mensaje>");
                    break;
                }
                String mensaje = "";
                for (int i = 2; i<comandos.length;i++){
                    mensaje += comandos[i] + " ";
                }
                imprimir(funciones.Escribir(mensaje, funciones.getPathActual() + "/" + comandos[1]));
                break;
            
            case "rd":
            if (comandos.length == 1) {
                imprimir("Ingrese el archivo que desea leer:");
                break;
            }
            imprimir(funciones.leer(funciones.getPathActual() + "/" + comandos[1].trim()));
            break;
            
            case "exit":
                dispose();
                break;
                
            default:
                imprimir("Su comando ingresado no existe.");
                break;
        }
    }
    
    private void KeyPressed(java.awt.event.KeyEvent evt) {
        int[] codigosProhibidos = {27, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 19, 155, 127, 36, 33, 34, 35, 16, 38, 37, 40, 38, 39, 17, 18};
        for (int codigo:codigosProhibidos) {
            if (evt.getKeyCode() == codigo){
                return;
            }        
        }
        if (evt.getKeyCode() == 8) {
            String newText = TextAreaCMD.getText();
            if (newText.charAt(newText.length() - 1) == '>') {
                
            } else {
                newText = newText.substring(0, newText.length()-1);
                TextAreaCMD.setText(newText);
            }
            return;
        } else if (evt.getKeyCode() == 10) {
            Funciones();
            TextAreaCMD.setText(TextAreaCMD.getText() + "\n" + funciones.getPathActual() + ">");
            return;
        }
        
        String newText = TextAreaCMD.getText();
        newText += evt.getKeyChar();
        TextAreaCMD.setText(newText);
    }                                  

    private void initGui() {
        TextAreaCMD = new javax.swing.JTextArea(5, 20);
        TextAreaCMD.setEditable(false);
        TextAreaCMD.setBackground(java.awt.Color.BLACK);
        TextAreaCMD.setForeground(java.awt.Color.WHITE);
        TextAreaCMD.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 12));
        TextAreaCMD.setLineWrap(true);
        
        TextAreaCMD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeyPressed(evt);
            }
        });
        
        jScrollPane1 = new javax.swing.JScrollPane(TextAreaCMD);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        add(jScrollPane1);
        setSize(700, 500);
        setLocationRelativeTo(null);
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CMDVisual main = new CMDVisual();
                main.setVisible(true);
            }
        });
    }
           
}
