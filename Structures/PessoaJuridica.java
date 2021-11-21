package Structures;

public class PessoaJuridica extends Perfil {
    private long cnpj;

    public PessoaJuridica(String usuario) {
        super(usuario);
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }

    public long getCnpj() {
        return cnpj;
    }

}
