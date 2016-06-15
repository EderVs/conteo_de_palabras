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

    private double[] getCoordenadas(double cx, double cy, double radio, double angulo) {
        double[] coordenadas = new double[2];
        angulo = Math.toRadians(angulo);
        coordenadas[0] = cx + radio * Math.cos(angulo);
        coordenadas[1] = cy + radio * Math.sin(angulo);
        return coordenadas;
    }

    private String getPedazo(double angulo1, double angulo2, double cx, double cy, double radio, String color) {
        SVGUtils svgUtil = new SVGUtils();
        double[] co1 = getCoordenadas(cx, cy, radio, angulo1);
        double[] co2 = getCoordenadas(cx, cy, radio, angulo2);
        String svg = "";
        String extra = "stroke='#fff' stroke-width='2'";
        svg += svgUtil.path(co1[0], co1[1], co2[0], co2[1], cx, cy, radio, color, extra);
        return svg;
    }

    private void generarPastel() {
        int total = 0;
        double cx = 250, cy = 250, radio = 250;
        double porcentaje, angulo, anguloi = 0, angulof = 0;
        int i = 0;
        html += "<svg width='500' height='500'>";
        String[] colores = {"#2E64FE","#B40404","#088A08","#DBA901","#7401DF","#424242","#E3F6CE","#FF4000","#04B4AE","#2A0A12","#D8D8D8","#151515","#01DF74","#0B2161","#3ADF00"};
        for (Palabra palabra: palabras_rn)
            total += palabra.getConteo();
        //html += "<path d='M130, 120 L230,120 A100,100 0 0, 1 49.09830056250526,178.77852522924732 z' fill='#0140CA' stroke='#fff' stroke-width='2'></path>";
        System.out.println("hola");
        for (Palabra palabra: palabras_rn) {
            porcentaje = palabra.getConteo() * 100 / total;
            angulo = porcentaje*360/100;
            System.out.println("porcentaje:" + porcentaje + "-angulo:" + angulo);
            anguloi = angulof;
            angulof = angulof + angulo;
            html += getPedazo(anguloi, angulof, cx, cy, radio, colores[i]);
            i++;
        }
        html += "</svg>";
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
        generarPastel();
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
