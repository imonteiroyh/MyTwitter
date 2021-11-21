package Twitter;

import java.util.Vector;

import Exceptions.MFPException;
import Exceptions.PDException;
import Exceptions.PEException;
import Exceptions.PIException;
import Exceptions.SIException;
import Exceptions.UJCException;
import Exceptions.UNCException;
import Repository.IRepositorioUsuario;
import Structures.Perfil;
import Structures.Tweet;

public class MyTwitter implements ITwitter {
    private IRepositorioUsuario repositorio;

    public MyTwitter(IRepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void criarPerfil(Perfil usuario) throws PEException {
        try {
            repositorio.cadastrar(usuario);
        } catch (UJCException ujc) {
            throw new PEException(usuario.getUsuario());
        }
    }

    @Override
    public void cancelarPerfil(String usuario) throws PIException, PDException {
        Perfil perfil = repositorio.buscar(usuario);

        if (perfil == null) {
            throw new PIException(usuario);
        } else {
            if (perfil.isAtivo() == false) {
                throw new PDException(usuario);
            } else {
                perfil.setAtivo(false);

                try {
                    repositorio.atualizar(perfil);
                } catch (UNCException unc) {
                    System.out.println(unc.getMessage());
                }
            }
        }
    }

    @Override
    public void tweetar(String usuario, String mensagem) throws PIException, PDException, MFPException {
        int size = mensagem.length();
        Perfil perfil = repositorio.buscar(usuario);

        if (size < 1 || size > 140) {
            throw new MFPException();
        }

        if (perfil == null) {
            throw new PIException(usuario);
        }

        if (perfil.isAtivo() == false) {
            throw new PDException(usuario);
        }

        Tweet tweet = new Tweet();
        tweet.setUsuario(usuario);
        tweet.setMensagem(mensagem);
        perfil.addTweet(tweet);

        try {
            repositorio.atualizar(perfil);
        } catch (UNCException unc) {
            System.out.println(unc.getMessage());
        }

        for (Perfil seguidor : perfil.getSeguidores()) {
            seguidor.addTweet(tweet);

            try {
                repositorio.atualizar(seguidor);
            } catch (UNCException unc) {
                System.out.println(unc.getMessage());
            }
        }

    }

    @Override
    public Vector<Tweet> timeline(String usuario) throws PIException, PDException {
        Perfil perfil = repositorio.buscar(usuario);

        if (perfil == null) {
            throw new PIException(usuario);
        }

        if (perfil.isAtivo() == false) {
            throw new PDException(usuario);
        }

        return perfil.getTimeline();
    }

    @Override
    public Vector<Tweet> tweets(String usuario) throws PIException, PDException {
        Perfil perfil = repositorio.buscar(usuario);
        Vector<Tweet> tweetsDoUsuario = new Vector<Tweet>();

        if (perfil == null) {
            throw new PIException(usuario);
        }

        if (perfil.isAtivo() == false) {
            throw new PDException(usuario);
        }

        for (Tweet tweet : perfil.getTimeline()) {
            if (tweet.getUsuario().equals(usuario)) {
                tweetsDoUsuario.add(tweet);
            }
        }

        return tweetsDoUsuario;
    }

    @Override
    public void seguir(String seguidor, String seguido) throws PIException, PDException, SIException {
        Perfil follower = repositorio.buscar(seguidor);
        Perfil followed = repositorio.buscar(seguido);

        if (follower == null) {
            throw new PIException(seguidor);
        }

        if (followed == null) {
            throw new PIException(seguido);
        }

        if (follower.isAtivo() == false) {
            throw new PDException(seguidor);
        }

        if (followed.isAtivo() == false) {
            throw new PDException(seguido);
        }

        if (follower.equals(followed)) {
            throw new SIException();
        }

        follower.addSeguido(followed);
        followed.addSeguidor(follower);

        try {
            repositorio.atualizar(follower);
            repositorio.atualizar(followed);
        } catch (UNCException unc) {
            System.out.println(unc.getMessage());
        }

    }

    @Override
    public int numeroSeguidores(String usuario) throws PIException, PDException {
        Perfil perfil = repositorio.buscar(usuario);

        if (perfil == null) {
            throw new PIException(usuario);
        }

        if (perfil.isAtivo() == false) {
            throw new PDException(usuario);
        }

        Vector<Perfil> seguidores = perfil.getSeguidores();

        for (Perfil seguidor : seguidores) {
            if (seguidor.isAtivo() == false) {
                seguidores.remove(seguidor);
            }
        }

        return seguidores.size();
    }

    @Override
    public Vector<Perfil> seguidores(String usuario) throws PIException, PDException {
        Perfil perfil = repositorio.buscar(usuario);

        if (perfil == null) {
            throw new PIException(usuario);
        }

        if (perfil.isAtivo() == false) {
            throw new PDException(usuario);
        }

        Vector<Perfil> seguidores = perfil.getSeguidores();

        for (Perfil seguidor : seguidores) {
            if (seguidor.isAtivo() == false) {
                seguidores.remove(seguidor);
            }
        }

        return seguidores;
    }

    @Override
    public Vector<Perfil> seguidos(String usuario) throws PIException, PDException {
        Perfil perfil = repositorio.buscar(usuario);

        if (perfil == null) {
            throw new PIException(usuario);
        }

        if (perfil.isAtivo() == false) {
            throw new PDException(usuario);
        }

        Vector<Perfil> seguidos = perfil.getSeguidos();

        for (Perfil seguido : seguidos) {
            if (seguido.isAtivo() == false) {
                seguidos.remove(seguido);
            }
        }

        return seguidos;
    }

}
