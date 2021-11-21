package Structures;

public class PessoaFisica extends Perfil {
    private long cpf;

    public PessoaFisica(String usuario) {
        super(usuario);
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    public long getCpf() {
        return cpf;
    }

}
