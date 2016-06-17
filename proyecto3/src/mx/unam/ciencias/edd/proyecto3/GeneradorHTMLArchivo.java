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
    private int total_palabras;

    public GeneradorHTMLArchivo(String archivo, Diccionario<String, Integer> palabras_dict) {
        this.html = "";
        this.htmlUtil = new HTMLUtil();
        this.archivo = archivo;
        this.palabras_dict = palabras_dict;
        this.palabras_rn = new ArbolRojinegro<Palabra>();
        this.palabras_avl = new ArbolAVL<Palabra>();
        this.palabras_orden = new Lista<Palabra>();
        this.total_palabras = 0;
        this.getListaOrden();
        this.getArbolesDePalabras(); 
    }

    private void getListaOrden() {
        for (String llave: palabras_dict.llaves()) {
            palabras_orden.agrega(new Palabra(llave, palabras_dict.get(llave)));
            total_palabras += palabras_dict.get(llave);
        }
        palabras_orden = Lista.mergeSort(palabras_orden).reversa();
    }

    private Lista<Palabra> desordenarLista(Lista<Palabra> l) {
        boolean b = false;
        Lista<Palabra> aux = new Lista<Palabra>();
        for (Palabra palabra: l) {
            if (b)
                aux.agrega(palabra);
            else
                aux.agregaInicio(palabra);
            b = !b;
        }
        return aux;
    }

    private void getArbolesDePalabras() {
        int i = 0;
        Lista<Palabra> aux = new Lista<Palabra>();
        for (Palabra palabra: palabras_orden) {
            if (i >= 15) {
                break;    
            }
            aux.agrega(palabra);
            i++;
        }
        aux = desordenarLista(aux);
        for (Palabra palabra: aux) {
            palabras_rn.agrega(palabra);
            palabras_avl.agrega(palabra);  
        }
    }

    private void getConteoDePalabras() {
        html += htmlUtil.getOpenTag("ol"); 
        for (Palabra palabra: palabras_orden) {
            html += htmlUtil.getOpenTag("li");
            html += htmlUtil.getOpenTag("span");
            html += palabra;
            html += htmlUtil.getCloseTag("span");
            html += ": " + palabra.getConteo();
            html += htmlUtil.getCloseTag("li");
        }
        html += htmlUtil.getCloseTag("ol");
    }

    private void generarCSS() {
        html += htmlUtil.getOpenTag("style");
        html += "header{height:100%;display:inline-block;margin:0;padding:20px;color:#263238}";
        html += "\nli span{font-weight:bold}";
        html += "\n#pastel div{display: inline-block;}";
        html += "\n#pastel span{margin-left: 5px;}";
        html += "\n#pastel, #arbolRojinegro, #arbolAVL{margin:0 auto;width: 1000px;}";
        html += htmlUtil.getCloseTag("style");
    }

    private void generarBody() {
        EstructurasDatosSVG edSVG = new EstructurasDatosSVG();
        PastelSVG<ArbolRojinegro<Palabra>> pastel = new PastelSVG<ArbolRojinegro<Palabra>>();
        SVGUtils svgUtils = new SVGUtils();
        String[] colores = {"#2E64FE","#B40404","#088A08","#DBA901","#7401DF","#424242","#E3F6CE","#FF4000","#04B4AE","#2A0A12","#D8D8D8","#151515","#01DF74","#0B2161","#3ADF00"};
        int i = 0;

        html += htmlUtil.getOpenTag("body");
        html += htmlUtil.getHeader("Archivo: " + archivo);
        html += htmlUtil.getOpenTag("div");
        html += htmlUtil.getOpenTag("a", "href='index.html'");
        html += "Regresar al index.";
        html += htmlUtil.getCloseTag("a");
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getOpenTag("section");
        // Conteo de las palabras.
        html += htmlUtil.getOpenTag("div", "id='palabras'");
        getConteoDePalabras();
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getOpenTag("div", "id='dibujos'");
        // Pastel
        html += htmlUtil.getOpenTag("div", "id='pastel'");
        html += htmlUtil.getOpenTag("div", "id='pastel_p'");
        html += pastel.generarPastel(250, 250, 250, palabras_rn, colores);
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getOpenTag("div", "id='pastel_palabras'");
        html += htmlUtil.getOpenTag("ul", "style='list-style: none;'");
        for (Palabra palabra: palabras_rn) {    
            html += htmlUtil.getOpenTag("li");
            html += "<svg class='rectangulo' width='16' height='16'>"+ svgUtils.rectangulo(16, 16, 0, 0, "fill='"+ colores[i] +"'") +"</svg>";
            html += htmlUtil.getOpenTag("span", "style='color:#000;'");
            html += palabra + ": " + String.format("%.2f" ,((palabra.getConteo() + 0.0)/total_palabras*100)) + "%";
            html += htmlUtil.getCloseTag("span");
            html += htmlUtil.getCloseTag("li" );
            i++;
        }
        html += htmlUtil.getCloseTag("ul");
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getCloseTag("div");
        // Arbol Rojinegro
        html += htmlUtil.getOpenTag("div", "id='arbolRojinegro'");
        html += edSVG.arbolBinario(palabras_rn, EstructurasDeDatos.ArbolRojinegro);
        html += htmlUtil.getCloseTag("div");
        // Arbol AVL
        html += htmlUtil.getOpenTag("div", "id='arbolAVL'");
        html += edSVG.arbolBinario(palabras_avl, EstructurasDeDatos.ArbolAVL);
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getCloseTag("section");
        html += htmlUtil.getCloseTag("body");
    }

    public void generarHTML() {
        html += htmlUtil.getDoctypeHTML();
        html += htmlUtil.getOpenTag("html");
        html += htmlUtil.getHead("Página de " + archivo);
        generarCSS();
        generarBody();
        html += htmlUtil.getCloseTag("html");
    }

    public String getHTML() {
        return this.html;
    }
}
