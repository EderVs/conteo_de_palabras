package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;

/**
  * Clase que sirve para llevar el HTML que
  * que se generará por archivo.
  **/
public class GeneradorHTML {
    
    private String html;
    private HTMLUtil htmlUtil;
    private String archivo;
    private Diccionario<String, Integer> palabras_dict;
    private ArbolRojinegro<Palabra> palabras_rn;
    private ArbolAVL<Palabra> palabras_avl;

    public GeneradorHTML(String archivo, Diccionario<String, Integer> palabras_dict) {
        html = "";
        htmlUtil = new HTMLUtil();
        this.archivo = archivo;
        this.palabras_dict = palabras_dict;
        palabras_rn = new ArbolRojinegro<Palabra>();
        palabras_avl = new ArbolAVL<Palabra>();
        getArbolesDePalabras();
    }

    private void getArbolesDePalabras() {
        Palabra actual;
        for (String llave: palabras_dict.llaves()) {
            actual = new Palabra(llave, palabras_dict.get(llave));
            palabras_rn.agrega(actual);
            palabras_avl.agrega(actual);
        }  
    }

    private void getConteoDePalabras() {
        Lista<Palabra> palabra_orden = new Lista<Palabra>();
        html += htmlUtil.getOpenTag("ol");
        for (Palabra palabra: palabras_avl)
            palabra_orden.agregaInicio(palabra);
        for (Palabra palabra: palabra_orden) {
            html += htmlUtil.getOpenTag("li");
            html += palabra;
            html += htmlUtil.getCloseTag("li");
        }
        html += htmlUtil.getCloseTag("ol");
    }

    private void generarBody() {
        html += htmlUtil.getOpenTag("body");
        html += htmlUtil.getHeader("Archivo: " + archivo);
        html += htmlUtil.getOpenTag("section");
        html += htmlUtil.getOpenTag("div");
        getConteoDePalabras();
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getOpenTag("div");
        html += "Aquí va la grafica de pastel";
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getOpenTag("div");
        html += "Aquí va el árbol rojinegro";
        html += htmlUtil.getOpenTag("div");
        html += htmlUtil.getCloseTag("div");
        html += "Aquí va el árbol AVL";
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getCloseTag("section");
        html += htmlUtil.getCloseTag("body");
    }

    public void generarHTML() {
        html += htmlUtil.getDoctypeHTML();
        html += htmlUtil.getOpenTag("html");
        html += htmlUtil.getHead("Página de " + archivo);
        generarBody();
        html += htmlUtil.getCloseTag("html");
    }

    public String getHTML() {
        return this.html;
    }
}
