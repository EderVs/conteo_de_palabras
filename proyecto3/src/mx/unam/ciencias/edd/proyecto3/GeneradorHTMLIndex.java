package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;

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

    private void generarBody() {
        html += htmlUtil.getOpenTag("body");
        html += htmlUtil.getCloseTag("body");
    }

    public void generarHTML() {
        html += htmlUtil.getDoctypeHTML();
        html += htmlUtil.getOpenTag("html");
        html += htmlUtil.getHead("Indice de archivos");
        generarBody();
        html += htmlUtil.getCloseTag("html");
    }

    public String getHTML() {
        return html;
    }
}
