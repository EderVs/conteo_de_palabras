package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;

/**
 * Clase para dibujar Estructuras de Datos en SVG.
 */
public class EstructurasDatosSVG {

    /**
     * Clase auxiliar para dibujar las graficas.
     */
	private class VerticeCoordenada implements Comparable<VerticeCoordenada> {
		
		VerticeGrafica<String> vertice;
		double x;
		double y;

		/**
	     * Crea el vertice coordenada a partir de un vertice y sus coordenadas.
	     * @param vertice VerticeGrafica
	     * @param x Coordenada x
	     * @param y Coordenada y
	     */
		public VerticeCoordenada(VerticeGrafica<String> vertice, double x, double y) {
			this.vertice = vertice;
			this.x = x;
			this.y = y;
		}

		/**
	     * Compara entre dos VerticeCoordenada.
	     */
		@Override public int compareTo(VerticeCoordenada vc) {
			return this.vertice.getElemento().compareTo(vc.vertice.getElemento());
		}

		/*
		 * Compara dos VerticeCoordeanda si son iguales
		 */
		public boolean equals(VerticeCoordenada vc) {
            return vc.vertice.getElemento().equals(this.vertice.getElemento());
        }
	}

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
	public String arbolBinario (ArbolBinario<Palabra> ab, EstructurasDeDatos arbol_a) {
		int padding = 4, largoSVG, altoSVG, radio;
		int iniX, iniY;
		String arbol;
		VerticeArbolBinario<Palabra> max;

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
	 * Realiza el svg de una Grafica.
	 * @param g Grafica de Integers.
	 * @return la Grafica en svg.
	 */
	public String grafica (Grafica<String> g) {
		String grafica;
		int padding = 5, radio;
		int perimetro;
        String max;
		double radioG;
		double largoSVG, altoSVG;

		max = this.obtenerMaximo(g);

		radio = (this.longitudString(max)*5+padding*2)/2;
		perimetro = g.getElementos()*radio*3;
		radioG = perimetro / 3.1416;

		largoSVG = altoSVG = radioG*2 + radio*2.0*2.0;

		grafica = this.obtenerVertices(g, radioG, radio, largoSVG/2, altoSVG/2);
		return xml + "<svg width='"+ largoSVG +"' height='"+ altoSVG +"'>" + grafica + "</svg>";
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
	private VerticeArbolBinario<Palabra> obtenerMaximo(VerticeArbolBinario<Palabra> vertice) {
		VerticeArbolBinario<Palabra> izq = null, der = null, max;
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
	 * Auxiliar de grafica. Trae el numero mas grande de la grafica.
	 * @param g Grafica.
	 * @return palabra de mayor longitud en la Grafica.
	 */
	private String obtenerMaximo (Grafica<String> g) {
		String max = "";
		for (String i:g) {
			max = i;
			break;
		}
		for (String i: g) {
			if (longitudString(max) < longitudString(i)) {
				max = i;
			}
		}
		return max;
	}

	/**
	 * Auxiliar de arbolBinario. Trae la longitud del SVG.
	 * @param ab ArbolBinario.
	 * @param radio de los vertices.
	 * @return longitud del SVG.
	 */
	private int obtenerLongitudSVGArbol (ArbolBinario<Palabra> ab, int radio) {
		int numeroHojas = (int) Math.pow(2,ab.profundidad());
		return (numeroHojas+(numeroHojas/16)+2)*(radio*2);
	}

	/**
	 * Auxiliar de arbolBinario. Trae la altura del SVG.
	 * @param ab ArbolBinario.
	 * @param radio de los vertices.
	 * @return altura del SVG.
	 */
	private int obtenerAlturaSVGArbol (ArbolBinario<Palabra> ab, int radio) {
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
	private String obtenerVertices (VerticeArbolBinario<Palabra> vertice, int radio, int i, int x, int y, EstructurasDeDatos arbol_a) {
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

    /**
	 * Auxiliar de Grafica. Trae SVG con los vertices y aristas de la grafica.
	 * @param g Grafica.
	 * @param radioG de la circunferencia donde van los vertices.
	 * @param radio de los vertices.
	 * @param x la coordenada x.
	 * @param y la coordenada y.
	 * @return SVG de los vertices y aristas.
	 */
	private String obtenerVertices (Grafica<String> g, double radioG, int radio, double x, double y)  {
		String vertices = "", aristas = "", color = "white", colorLetra = "black";
		double angulo = Math.toRadians(360 / g.getElementos());
		double anguloi = 0, xi, yi;
		int i = 0;
		VerticeCoordenada coordenadai;
		VerticeGrafica<String> vi = null;
		VerticeCoordenada[] coordenadas = new VerticeCoordenada[g.getElementos()];
		Arreglos arr = new Arreglos();

		// Obteniendo Vertices y asignarles una coordenada.
		for (String v: g) {
			xi = radioG*Math.cos(anguloi);
			yi = radioG*Math.sin(anguloi);
			vertices += utils.circuloConTexto(v, x+xi, y+yi, radio, color, colorLetra);

			vi = g.vertice(v);
			coordenadai = new VerticeCoordenada(vi, x+xi, y+yi);
			coordenadas[i] = coordenadai;

			anguloi += angulo;
			i += 1;
		}


		// Obteniendo aristas.
		arr.quickSort(coordenadas);
		for (VerticeCoordenada v: coordenadas) {
			for (VerticeGrafica<String> vecino: v.vertice.vecinos()) {
				coordenadai = new VerticeCoordenada(vecino, 0, 0);
				coordenadai = coordenadas[arr.busquedaBinaria(coordenadas, coordenadai)];
				aristas += utils.linea(v.x, v.y, coordenadai.x, coordenadai.y);
			}
		}

		return aristas + vertices;
	}
}    
