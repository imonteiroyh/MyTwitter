package Exceptions;

public class PDException extends Exception {
    private String usuario;

    public PDException(String usuario) {
        super("Perfil desativado!");
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
}