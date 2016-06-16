package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;

/**
  * Clase que sirve para llevar el HTML que
  * que se generará por archivo.
  **/
public class GeneradorHTMLArchivo {
    
    private String html;
    private HTMLUtil htmlUtil;
    private String archivo;
    private Diccionario<String, Integer> palabras_dict;
    private Lista<Palabra> palabras_orden;
    private ArbolRojinegro<Palabra> palabras_rn;
    private ArbolAVL<Palabra> palabras_avl;

    public GeneradorHTMLArchivo(String archivo, Diccionario<String, Integer> palabras_dict) {
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
            html += palabra + ": " + palabra.getConteo();
            html += htmlUtil.getCloseTag("li");
        }
        html += htmlUtil.getCloseTag("ol");
    }

    private void generarBody() {
        EstructurasDatosSVG edSVG = new EstructurasDatosSVG();
        PastelSVG<ArbolRojinegro<Palabra>> pastel = new PastelSVG<ArbolRojinegro<Palabra>>();
        String[] colores = {"#2E64FE","#B40404","#088A08","#DBA901","#7401DF","#424242","#E3F6CE","#FF4000","#04B4AE","#2A0A12","#D8D8D8","#151515","#01DF74","#0B2161","#3ADF00"};
        int i = 0;

        html += htmlUtil.getOpenTag("body");
        html += htmlUtil.getHeader("Archivo: " + archivo);
        html += htmlUtil.getOpenTag("section");
        // Conteo de las palabras.
        html += htmlUtil.getOpenTag("div");
        getConteoDePalabras();
        html += htmlUtil.getCloseTag("div");
        // Pastel
        html += htmlUtil.getOpenTag("div");
        html += htmlUtil.getOpenTag("div");
        html += pastel.generarPastel(250, 250, 250, palabras_rn, colores);
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getOpenTag("div");
        html += htmlUtil.getOpenTag("ul", "style='list-style: none;'");
        for (Palabra palabra: palabras_rn) {    
            html += htmlUtil.getOpenTag("li");
            html += htmlUtil.getOpenTag("div", "style='background-color:"+ colores[i] +"; display:inline'") + "&nbsp&nbsp&nbsp&nbsp";
            html += htmlUtil.getCloseTag("div");
            html += htmlUtil.getOpenTag("span", "style='color:#000;'");
            html += palabra;
            html += htmlUtil.getCloseTag("span");
            html += htmlUtil.getCloseTag("li" );
            i++;
        }
        html += htmlUtil.getCloseTag("ul");
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getCloseTag("div");
        // Arbol Rojinegro
        html += htmlUtil.getOpenTag("div");
        html += edSVG.arbolBinario(palabras_rn, EstructurasDeDatos.ArbolRojinegro);
        html += htmlUtil.getCloseTag("div");
        // Arbol AVL
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
