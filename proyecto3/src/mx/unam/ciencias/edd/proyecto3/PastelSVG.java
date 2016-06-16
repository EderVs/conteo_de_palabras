package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;

/**
  * Clase para generar un svg de gráfica de
  * pastel.
  **/
public class PastelSVG<T extends Coleccion<Palabra>> {

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
        String extra = "stroke='#fff' stroke-width='2'";
        return svgUtil.path(co1[0], co1[1], co2[0], co2[1], cx, cy, radio, color, extra);
    }

    public String generarPastel(double cx, double cy, double radio, T col, String[] colores) {
        int total = 0, i = 0;
        double porcentaje, angulo, anguloi = 0, angulof = 0;
        String svg = "";
        svg += "<svg width='500' height='500'>";
        for (Palabra palabra: col)
            total += palabra.getConteo();
        for (Palabra palabra: col) {
            porcentaje = palabra.getConteo() * 100.0 / total;
            angulo = porcentaje*360/100;
            anguloi = angulof;
            angulof += angulo;
            svg += getPedazo(anguloi, angulof, cx, cy, radio, colores[i]);
            i++;
        }
        return svg + "</svg>";
    }
}
