/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Apps;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 *
 * @author User
 */
public class CMDFunciones {
    private File mf;
    
    public CMDFunciones(String dir) {
        mf = new File(dir);
    }
    
    public String Mkdir(String dir) {
        String mensaje = "";
        File carpeta = new File(dir);
        if (carpeta.exists()) {
            mensaje = "Esta carpeta ya existe.";
            return mensaje;
        } else {
            mensaje = "Carpeta creada existosamente.";
            carpeta.mkdir();
            return mensaje;
        }
    }
    
    public String Mfile(String file) {
        String mensaje = "";
        File archivo = new File(file);
        if (archivo.exists()) {
            mensaje = "Este archivo ya existe.";
            return mensaje;
        } else {
            mensaje = "Archivo creado existosamente";
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                mensaje = "Error: no se pudo crear";
            }
            return mensaje;
        }
    }
    
    public void antidoto(File vaciar) {
        if (vaciar.isDirectory()) {
            for (File vacio : vaciar.listFiles()) {
                vacio.delete();
            }
        }

    }

    public String Rm(File nombre) {
        if (nombre.isDirectory()) {
            for (File child : nombre.listFiles()) {
                if (child.isDirectory()) {
                    antidoto(child);
                    child.delete();
                } else {
                    child.delete();
                }
            }
            nombre.delete();
            return "Carpeta eliminada";
        }

        if (nombre.isFile()) {
            nombre.delete();
            return "Archivo eliminado";
        }
        return "Error";
    }
    
    public String Cd(String path) {
        if (path.charAt(0) != '/') {
            File newDir = new File(mf.getAbsolutePath() + "/" + path);
            if (!newDir.isDirectory()) {
                return "La direccion tiene que ser una carpeta.";
            }
            mf = newDir;
            return "";
        }
        mf = new File(path);
        return "";
    }
    
    public String Dir(String path) { //Lista de archivos
        String listas = "";
        File lista = new File(path);
        if (lista.isDirectory()) {
            for (File archivo : lista.listFiles()) {
                listas += "\n -"+archivo.getName();
            }
            return listas;
        } else {
            return "ERROR: antes debe haber seleccionado un directorio.";
        }
    }
    
    public String Date(){
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        return dia+"-"+mes+"-"+year;
    }
    
    public String Time(){
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        int segundo = calendario.get(Calendar.SECOND);
        return hora+":"+minuto+":"+segundo;
    }
    
    public String Escribir(String mensaje, String path) {
        File nombre = new File(path);
        String salida = "";
        if (nombre.exists()) {
            if (nombre.isFile()) {
                try {
                    FileWriter texto = new FileWriter(nombre);
                    texto.write(mensaje);
                    texto.flush();
                } catch (IOException e) {
                    salida = "ERROR: no se pudo escribir en el archivo.";
                }
                salida = "Escrito correctamente.";
                return salida;
            } else {
                salida = "ERROR: debe seleccionar un archivo";
                return salida;
            }
        } else {
            salida = "ERROR: El archivo seleccionado no existe.";
            return salida;
        }
    }
    
    public String leer(String path) {
        File nombre = new File(path);
        String mensaje = "";
        if (nombre.exists()) {
            if (nombre.isFile()) {
                try {
                    FileReader fr = new FileReader(nombre);
                    String contenido = "";
                    for (int i = fr.read(); i != -1; i = fr.read()) {
                        contenido += (char) i;
                    }
                    return contenido;
                } catch (IOException e) {
                    mensaje = "ERROR: no se pudo leer su archivo.";
                    return mensaje;
                }
            } else {
                mensaje = "ERROR: debe seleccionar un archivo.";
                return mensaje;
            }
        } else {
            mensaje = "ERROR: El archivo seleccionado no existe.";
            return mensaje;
        }
    }
    
    public String getPathActual() {
        try {
            return mf.getCanonicalPath();
        } catch (Exception e) {
            return mf.getAbsolutePath();
        }

    }
    
}
