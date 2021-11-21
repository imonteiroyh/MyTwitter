package Repository;

import java.util.Vector;

import Exceptions.UJCException;
import Exceptions.UNCException;
import Structures.Perfil;

public class Repositorio implements IRepositorioUsuario {
    private Vector<Perfil> usuarios = new Vector<Perfil>();

    @Override
    public void cadastrar(Perfil usuario) throws UJCException {
        if (buscar(usuario.getUsuario()) != null) {
            throw new UJCException(usuario.getUsuario());
        } else {
            usuarios.add(usuario);
        }
    }

    @Override
    public Perfil buscar(String usuario) {
        for (Perfil findInUsuarios : usuarios) {
            if (findInUsuarios.getUsuario().equals(usuario)) {
                return findInUsuarios;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Perfil usuario) throws UNCException {
        Perfil perfil = buscar(usuario.getUsuario());

        if (perfil == null) {
            throw new UNCException(usuario.getUsuario());
        } else {
            int index = usuarios.indexOf(perfil);
            usuarios.set(index, perfil);
        }
    }
}