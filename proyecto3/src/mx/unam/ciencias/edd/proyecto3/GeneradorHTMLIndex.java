package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.IOException;
import java.io.File;

/**
  * Clase para mostrar el indice que tiene una especie
  * de indice hacia los demás archivos.
  **/
public class GeneradorHTMLIndex {
    
    private String html;
    private Diccionario<String, Diccionario<String, Integer>> archivos_palabras;
    private HTMLUtil htmlUtil;
    private String directorio;

    public GeneradorHTMLIndex(Diccionario<String, Diccionario<String, Integer>> archivos_palabras, String directorio) {
        this.html = "";
        this.archivos_palabras = archivos_palabras;
        this.htmlUtil = new HTMLUtil();
        this.directorio = directorio;
    }

    private void generarLinks() {
        html += htmlUtil.getOpenTag("ul");
        File archivo_actual;
        for (String llave: archivos_palabras.llaves()) {
            archivo_actual = new File(llave);
            html += htmlUtil.getOpenTag("li");
            html += htmlUtil.getOpenTag("a", "href='"+ archivo_actual.getName() +".html'");
            html += archivo_actual.getName() + ": " + archivos_palabras.get(llave).getElementos() + " palabras.";
            html += htmlUtil.getCloseTag("a");
            html += htmlUtil.getCloseTag("li");
        }
        html += htmlUtil.getCloseTag("ul");
    }

    private void generarGrafica() {
        Grafica<String> g = new Grafica<String>();
        EstructurasDatosSVG edSVG = new EstructurasDatosSVG();
        File archivo_actual;
        Diccionario<String, Conjunto<String>> conjuntos = new Diccionario<String, Conjunto<String>>(archivos_palabras.getElementos());
        Conjunto<String> actual;
        // Vertices
        for (String llave: archivos_palabras.llaves()) {
            archivo_actual = new File(llave);
            g.agrega(archivo_actual.getName());
            actual = new Conjunto<String>(archivos_palabras.get(llave).getElementos());
            for (String palabra: archivos_palabras.get(llave).llaves()) {
                if (palabra.length() >= 5) {
                    actual.agrega(palabra);
                }
            }
            conjuntos.agrega(archivo_actual.getName(), actual);
        }
        // Aristas
        for (String v: g) {
            for (String vi: g) {
                if (!v.equals(vi)) {
                    if (conjuntos.get(v).interseccion(conjuntos.get(vi)).getElementos() >= 10 && !g.sonVecinos(v, vi)) {
                        g.conecta(v, vi);
                    } 
                }
            }
        }
        html += edSVG.grafica(g);
    }

    private void generarCSS() {
        html += htmlUtil.getOpenTag("style");
        html += "#grafica{margin:0 auto;width: 1000px;}"; 
        html += htmlUtil.getCloseTag("style");
    }

    private void generarBody() {
        html += htmlUtil.getOpenTag("body");
        html += htmlUtil.getHeader("Proyecto 3. Estructuras de datos.");
        html += htmlUtil.getOpenTag("section");
        html += htmlUtil.getOpenTag("div");
        generarLinks();
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getP("Grafica donde cada archivo es un vértice, y dos archivos son vecinos si ambos contienen al menos 10 palabaras en común, con al menos 5 caracteres cada una.");
        html += htmlUtil.getOpenTag("div", "id='grafica'");
        generarGrafica();
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getCloseTag("section");
        html += htmlUtil.getCloseTag("body");
    }

    public void generarHTML() {
        html += htmlUtil.getDoctypeHTML();
        html += htmlUtil.getOpenTag("html");
        html += htmlUtil.getHead("Indice de archivos");
        generarCSS();
        generarBody();
        html += htmlUtil.getCloseTag("html");
    }

    public String getHTML() {
        return html;
    }
}
