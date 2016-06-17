package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;

/**
  * Clase que sirve para generar las etiquetas
  * genericas utilizadas en el proyecto.
  **/
public class HTMLUtil {
   
    public String getDoctypeHTML() {
        return "<!DOCTYPE html>";
    }

    public String getTitle(String titulo) {
        return "<title>" + titulo + "</title>";
    }

    public String getHead(String titulo) {
        return "<head><meta charset='UTF-8'>" + getTitle(titulo) + "</head>";
    }

    public String getHead(String titulo, String css) {
        return "<head><meta charset='UTF-8'>" + getCSS(css) + getTitle(titulo) + "</head>";

    }

    public String getP(String texto) {
        return "<p>" + texto + "</p>";
    }

    public String getHeader(String header) {
        return "<header><h1>" + header  + "</h1></header>";
    }

    public String getOpenTag(String tag, String extra) {
        return "<" + tag + " " + extra + " >";
    }

    public String getOpenTag(String tag) {
        return "<" + tag + ">";
    }

    public String getCloseTag(String tag) {
        return "</" + tag + ">";
    }

    public String getCSS(String css) {
        return "<link rel='stylesheet' type='text/css' href='"+css+"'>";
    }
}
