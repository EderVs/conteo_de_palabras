package mx.unam.ciencias.edd.proyecto3;

public class ExcepcionDirectorioNoValido extends RuntimeException {
    
    public ExcepcionDirectorioNoValido() {
        super("Directorio no valido.");
    }

    public ExcepcionDirectorioNoValido(String err) {
        super(err);
    }

}
