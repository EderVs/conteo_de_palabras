package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.Normalizer;
import java.text.Normalizer.Form;


/**
  * <p>Proyecto 3</p>
  *
  * @author Eder Vs
  * @version 1.0
  * @since 14/06/2016
  *
  */
public class Proyecto3 {

    public static void mostrarError(String error) {
        System.out.println(error);
        System.exit(1);
    }

    public static void main(String[] args) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        Diccionario<String, Diccionario<String, Integer>> archivos_palabras;
        Diccionario<String, Integer> palabras;
        Lista<String> archivos = new Lista<String>();
        String linea_actual, texto, directorio = null;
        String[] palabras_actual;
        GeneradorHTMLArchivo generador;
        File archivo_actual;

        // Separa los archivos del directorio
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                if (args.length > i+1) {
                    directorio = args[++i];
                } 
            } else {
                archivos.agrega(args[i]);
            }
        }
 
        // Validando que fue agregado un directorio donde poner los archivos.
        if (directorio == null) {
            mostrarError("No fue agregado un directorio de destino");
        }
        // Validando que el directorio existe
        File directorio_file = new File(directorio); 
        if (!directorio_file.exists()) {
            mostrarError("El directorio '"+ directorio +"' no existe");
        }
        // Validando que fueron agregados archivos donde leer palabras.
        if (archivos.esVacio()) {
            mostrarError("No fueron agregados archivos donde leer palabras");
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
                    palabras_actual = Normalizer.normalize(linea_actual, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll("[*\\p{L}\\p{Nd}]-", " ").replaceAll("\\p{Punct}","").toLowerCase().split(" ");
                    for (String palabra: palabras_actual) { 
                        if (palabras.contiene(palabra)) {
                            palabras.agrega(palabra, palabras.get(palabra) + 1);
                        } else {
                            palabras.agrega(palabra, 1);
                        }
                    }
                }
                // Generando el archivo html
                try {
                    archivo_actual = new File(directorio + "/" + archivo + ".html");
                    bw = new BufferedWriter(new FileWriter(archivo_actual));
                    generador = new GeneradorHTMLArchivo(archivo, palabras);
                    generador.generarHTML();
                    bw.write(generador.getHTML());
                    bw.close();
                } catch (IOException e) {
                   System.out.println("Sucedio un error al momento de crear el archivo" + archivo + ".html");
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
    }
}
