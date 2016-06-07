package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;


/**
  * <p>Proyecto 3</p>
  *
  * @author Eder Vs
  * @version 1.0
  * @since 14/06/2016
  *
  */
public class Proyecto3 {
    public static void main(String[] args) {
        BufferedReader br = null;
        Diccionario<String, Diccionario<String, Integer>> archivos_palabras;
        Diccionario<String, Integer> palabras;
        Lista<String> archivos = new Lista<String>();
        String linea_actual, texto, directorio = null;
        String[] palabras_actual;  
        
        // Separa los archivos del directorio
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                directorio = args[++i];
            } else {
                archivos.agrega(args[i]);
            }
        }

        // Validando que el directorio existe
        if (directorio == null) {
            System.out.println("No fue agregado un directorio de destino");
            System.exit(1);
        }
        File directorio_file = new File(directorio); 
        if (!directorio_file.exists()) {
            System.out.println("El directorio '"+ directorio +"' no existe");
            System.exit(1);
        }

        // Creando el diccionario que va a guardar las palabras y su conteo por archivos.
        archivos_palabras = new Diccionario<String, Diccionario<String, Integer>>(archivos.getLongitud());

        // Obteniendo las palabras por archivo
        for (String archivo: archivos) {
            try {
                br = new BufferedReader(new FileReader(archivo));
                archivos_palabras.agrega(archivo, new Diccionario<String, Integer>());
                palabras = archivos_palabras.get(archivo);

                while ((linea_actual = br.readLine()) != null) {
                    palabras_actual = linea_actual.replaceAll("[*\\p{L}\\p{Nd}]-", " ").split(" ");
                    for (String palabra: palabras_actual) { 
                        if (palabras.contiene(palabra)) {
                            palabras.agrega(palabra, palabras.get(palabra) + 1);
                        } else {
                            palabras.agrega(palabra, 1);
                        }
                    }
                }
                    
            } catch (IOException e) {
                System.out.println("El archivo '" + archivo + "' no existe");
                System.exit(1);
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    System.out.println("Hubo un problema con el archivo " + archivo);
                    System.exit(1);
                }
            }
        }

        // Imprimir el diccionario de la manera que quiero
        for (String s: archivos_palabras.llaves()) {
            System.out.println("-----" + s + "-----");
            Diccionario <String, Integer> lala = archivos_palabras.get(s);
            for (String llave: lala.llaves()) {
                System.out.println(llave + ": " + lala.get(llave));
            }
        }
        
    }
}
