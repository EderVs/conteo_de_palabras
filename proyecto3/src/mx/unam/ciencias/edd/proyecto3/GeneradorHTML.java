package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.EstructurasDatosSVG;
import mx.unam.ciencias.edd.proyecto2.EstructurasDeDatos;

/**
  * Clase que sirve para llevar el HTML que
  * que se generará por archivo.
  **/
public class GeneradorHTML {
    
    private String html;
    private HTMLUtil htmlUtil;
    private String archivo;
    private Diccionario<String, Integer> palabras_dict;
    private Lista<Palabra> palabras_orden;
    private ArbolRojinegro<Palabra> palabras_rn;
    private ArbolAVL<Palabra> palabras_avl;

    public GeneradorHTML(String archivo, Diccionario<String, Integer> palabras_dict) {
        this.html = "";
        this.htmlUtil = new HTMLUtil();
        this.archivo = archivo;
        this.palabras_dict = palabras_dict;
        this.palabras_rn = new ArbolRojinegro<Palabra>();
        this.palabras_avl = new ArbolAVL<Palabra>();
        this.palabras_orden = new Lista<Palabra>();
        this.getListaOrden();
        this.getArbolesDePalabras();
    }

    private void getListaOrden() {
        for (String llave: palabras_dict.llaves())
            palabras_orden.agrega(new Palabra(llave, palabras_dict.get(llave)));
        palabras_orden = Lista.mergeSort(palabras_orden).reversa();
    }

    private void getArbolesDePalabras() {
        int i = 0;
        for (Palabra palabra: palabras_orden) {
            if (i >= 15) {
                break;    
            }
            palabras_rn.agrega(palabra);
            palabras_avl.agrega(palabra);
            i++;
        }  
    }

    private void getConteoDePalabras() {
        html += htmlUtil.getOpenTag("ol"); 
        for (Palabra palabra: palabras_orden) {
            html += htmlUtil.getOpenTag("li");
            html += palabra;
            html += htmlUtil.getCloseTag("li");
        }
        html += htmlUtil.getCloseTag("ol");
    }

    private void generarPastel() {
        int total = 0;
        for (Palabra palabra: palabras_rn)
            total += palabra.getConteo();
        
    }

    private void generarBody() {
        EstructurasDatosSVG edSVG = new EstructurasDatosSVG();

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
        html += edSVG.arbolBinario(palabras_rn, EstructurasDeDatos.ArbolRojinegro);
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getOpenTag("div");
        html += edSVG.arbolBinario(palabras_avl, EstructurasDeDatos.ArbolAVL);
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
