package Exceptions;

public class PEException extends Exception {
    private String usuario;

    public PEException(String usuario) {
        super("Perfil já existente!");
    }

    public String getUsuario() {
        return usuario;
    }
}