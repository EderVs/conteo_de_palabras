package mx.unam.ciencias.edd.proyecto3;

public class ExcepcionArchivoNoValido extends RuntimeException {
    
    public ExcepcionArchivoNoValido() {
        super("Archivo no valido.");
    }

    public ExcepcionArchivoNoValido(String err) {
        super(err);
    }

}
