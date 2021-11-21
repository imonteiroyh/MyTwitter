package Repository;

import Exceptions.UJCException;
import Exceptions.UNCException;
import Structures.Perfil;

public interface IRepositorioUsuario {
    public void cadastrar(Perfil usuario) throws UJCException;

    public Perfil buscar(String usuario);

    public void atualizar(Perfil usuario) throws UNCException;

}