package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.*;

/**
 * Clase para dibujar Estructuras de Datos en SVG.
 */
public class EstructurasDatosSVG<O extends Comparable<O>> {

	private SVGUtils utils;
	private final String xml;

	/**
     * Constructor único que inicializa los valores.
     */
	public EstructurasDatosSVG () {
		utils = new SVGUtils();
		xml = "";
	}	

	/**
	 * Realiza el svg de un ArbolBinario
	 * @param ab ArbolBinario de Integers
	 * @param arbol_a para saber que tipo de arbol es.
	 * @return el ArbolBinario en svg.
	 */
	public String arbolBinario (ArbolBinario<O> ab, EstructurasDeDatos arbol_a) {
		int padding = 15, largoSVG, altoSVG, radio;
		int iniX, iniY;
		String arbol;
		VerticeArbolBinario<O> max;

		if (ab.esVacio()) {
			return xml;
		}

		// Obteniendo el valor maximo del arbol.
		max = this.obtenerMaximo(ab.raiz());
		
		radio = (this.longitudString(max.get().toString())*5+padding*2)/2;
		largoSVG = this.obtenerLongitudSVGArbol(ab,radio);
		altoSVG = this.obtenerAlturaSVGArbol(ab,radio);

		iniX = largoSVG/2;
		iniY = radio*3;

		arbol = this.obtenerVertices(ab.raiz(), radio, largoSVG/2, iniX, iniY, arbol_a);
		return xml + "<svg width='"+ largoSVG +"' height='"+ altoSVG +"'>" + arbol + "</svg>";
	}

	/**
	 * Trae la longitud de un numero en termino de sus digitos.
	 * @param n numero.
	 * @return longitud de n.
	 */
	private int longitudNumero (int n) {
		int i = 1;
		while (n >= 10) {
			n /= 10;
			i++;
		}
		return i;
	}

    private int longitudString (String s) {
        int i = 0, length = s.length();        
        while (length > 0) {
            s = s.substring(0, --length);
            i++;
        }
        return i;
    }

	/**
	 * Auxiliar de arbolBinario. Trae el numero mas grande del subarbol.
	 * @param vertice donde inicia.
	 * @return el VerticeArbolBinario maximo.
	 */
	private VerticeArbolBinario<O> obtenerMaximo(VerticeArbolBinario<O> vertice) {
		VerticeArbolBinario<O> izq = null, der = null, max;
		if (vertice == null) {
			return null;
		}
		if (!vertice.hayIzquierdo() && !vertice.hayDerecho()) {
			return vertice;
		} else {
			if (vertice.hayIzquierdo()) {
				izq = this.obtenerMaximo(vertice.getIzquierdo());	
			}
			if (vertice.hayDerecho()) {
				der = this.obtenerMaximo(vertice.getDerecho());	
			}
		}

		if (izq != null && der != null) {
			max = ((longitudString(izq.get().toString()) >= (longitudString(der.get().toString())))? izq : der);
		} else {
			if (izq == null){
				max = der;
			} else {
				max = izq;
			}		
		}

		return ((longitudString(vertice.get().toString()) >= longitudString(max.get().toString()))? vertice : max);
	}

	/**
	 * Auxiliar de arbolBinario. Trae la longitud del SVG.
	 * @param ab ArbolBinario.
	 * @param radio de los vertices.
	 * @return longitud del SVG.
	 */
	private int obtenerLongitudSVGArbol (ArbolBinario<O> ab, int radio) {
		int numeroHojas = (int) Math.pow(2,ab.profundidad());
		return (numeroHojas+(numeroHojas/2)+2)*(radio*2);
	}

	/**
	 * Auxiliar de arbolBinario. Trae la altura del SVG.
	 * @param ab ArbolBinario.
	 * @param radio de los vertices.
	 * @return altura del SVG.
	 */
	private int obtenerAlturaSVGArbol (ArbolBinario<O> ab, int radio) {
		return (ab.profundidad()+3)*(radio*2);
	}

	/**
	 * Auxiliar de arbolBinario. Trae SVG con los vertices del arbol.
	 * @param vertice donde inicia.
	 * @param radio int de los vertices.
	 * @param i que indica el intervalo para el vertice actual.
	 * @param x la coordenada x.
	 * @param y la coordenada y.
	 * @param arbol_a para saber que tipo de arbol es.
	 * @return SVG de los vertices.
	 */
	private String obtenerVertices (VerticeArbolBinario<O> vertice, int radio, int i, int x, int y, EstructurasDeDatos arbol_a) {
		String arbol = "", color = "white", colorLetra = "black";
		i /= 2;
		// Recusivamente obteniendo los sub-arboles izquierdo y derecho.
		if (vertice.hayIzquierdo()) {
			arbol += utils.linea(x, y, x-i, y+radio*2);
			arbol += obtenerVertices(vertice.getIzquierdo(), radio, i, x-i, y+radio*2, arbol_a);
		}
		if (vertice.hayDerecho()) {
			arbol += utils.linea(x, y, x+i, y+radio*2);
			arbol += obtenerVertices(vertice.getDerecho(), radio, i, x+i, y+radio*2, arbol_a);
		}

		// Obteniendo si necesita color.
		if (vertice.toString().charAt(0) == ('R')) {
			color = "red";
			colorLetra = "white";
		}
		if (vertice.toString().charAt(0) == ('N')) {
			color = "black";
			colorLetra = "white";
		}

		arbol += utils.circuloConTexto(vertice.get().toString(), x, y, radio, color, colorLetra);
		if (arbol_a == EstructurasDeDatos.ArbolAVL) {
			arbol += utils.texto(vertice.toString().split(" ")[1], x+radio, y-(radio/2), "text-anchor='middle'");
		}
		return arbol;
	}
}    
