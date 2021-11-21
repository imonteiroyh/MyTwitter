import java.util.Scanner;
import java.util.Vector;

import Repository.IRepositorioUsuario;
import Repository.Repositorio;
import Structures.Perfil;
import Structures.PessoaFisica;
import Structures.PessoaJuridica;
import Structures.Tweet;
import Twitter.MyTwitter;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MyTwitter myTwitter = new MyTwitter(new Repositorio());
        IRepositorioUsuario repositorio = new Repositorio();
        Vector<Tweet> timeline, tweets;
        String login = "", action = "", usuario, mensagem, seguido;
        long cpf, cnpj;
        boolean erro, out;

        System.out.println("Seja bem-vindo(a) ao MyTwitter!");

        while (login != "0") {
            menuLogin();
            login = scanner.nextLine();

            switch (login) {
                case "1":
                    String tipoPessoa = "0";
                    erro = false;

                    while (tipoPessoa.charAt(0) != '1' && tipoPessoa.charAt(0) != '2') {
                        if (erro == true) {
                            System.out.println("\nErro. Opção inválida!");
                        }

                        System.out.println("-------------------");
                        System.out.println("1. Pessoa Física");
                        System.out.println("2. Pessoa Jurídica");
                        System.out.println("-------------------");
                        System.out.print("Por favor, selecione uma opção: ");
                        tipoPessoa = scanner.nextLine();

                        if (tipoPessoa.length() == 0) {
                            tipoPessoa = "0";
                        }

                        erro = true;
                    }

                    System.out.print("Digite o nome do usuário: ");
                    usuario = scanner.nextLine();

                    if (tipoPessoa.charAt(0) == '1') {
                        System.out.print("Digite o CPF: ");
                        cpf = scanner.nextLong();
                        clearBuffer(scanner);

                        PessoaFisica pessoaFisica = new PessoaFisica(usuario);
                        pessoaFisica.setCpf(cpf);

                        try {
                            repositorio.cadastrar(pessoaFisica);
                        } catch (Exception exc) {
                        }

                        try {
                            myTwitter.criarPerfil(pessoaFisica);
                            System.out.println("\nPerfil de pessoa física criado!");
                        } catch (Exception exc) {
                            System.out.println("\nErro. " + exc.getMessage());
                        }
                    } else if (tipoPessoa.charAt(0) == '2') {
                        System.out.print("Digite o CNPJ: ");
                        cnpj = scanner.nextLong();
                        clearBuffer(scanner);

                        PessoaJuridica pessoaJuridica = new PessoaJuridica(usuario);
                        pessoaJuridica.setCnpj(cnpj);

                        try {
                            repositorio.cadastrar(pessoaJuridica);
                        } catch (Exception exc) {
                        }

                        try {
                            myTwitter.criarPerfil(pessoaJuridica);
                            System.out.println("\nPerfil de pessoa jurídica criado!");
                        } catch (Exception exc) {
                            System.out.println("\nErro. " + exc.getMessage());
                        }
                    }

                    break;

                case "2":
                    System.out.print("Digite o nome do usuário: ");
                    usuario = scanner.nextLine();
                    out = false;

                    if (verificaPerfil(usuario, repositorio) == false) {
                        out = true;
                        System.out.println("\nErro. Usuário não cadastrado.");
                    }

                    while (out == false) {
                        menuActions();
                        action = scanner.nextLine();

                        switch (action) {
                            case "1":
                                System.out.print("Digite uma mensagem: ");
                                mensagem = scanner.nextLine();

                                try {
                                    myTwitter.tweetar(usuario, mensagem);
                                    System.out.println("\nMensagem publicada com sucesso!");
                                } catch (Exception exc) {
                                    System.out.println("\nErro. " + exc.getMessage());
                                }

                                break;

                            case "2":
                                System.out.println("");

                                try {
                                    timeline = myTwitter.timeline(usuario);

                                    if (timeline.size() == 0) {
                                        System.out.println("Não existem tweets na sua timeline, publique algo!");
                                        break;
                                    }

                                    for (Tweet tweet : timeline) {
                                        System.out
                                                .println("Usuário @" + tweet.getUsuario() + ": " + tweet.getMensagem());
                                    }
                                } catch (Exception exc) {
                                    System.out.println("\nErro. " + exc.getMessage());
                                }

                                break;

                            case "3":
                                System.out.println("");

                                try {
                                    tweets = myTwitter.tweets(usuario);

                                    if (tweets.size() == 0) {
                                        System.out.println("Você não possui tweets, publique algo!");
                                        break;
                                    }

                                    for (Tweet tweet : tweets) {
                                        System.out
                                                .println("Usuário @" + tweet.getUsuario() + ": " + tweet.getMensagem());
                                    }
                                } catch (Exception exc) {
                                    System.out.println("\nErro. " + exc.getMessage());
                                }

                                break;

                            case "4":
                                System.out.print("Digite o nome do usuário a ser seguido: ");
                                seguido = scanner.nextLine();

                                try {
                                    myTwitter.seguir(usuario, seguido);
                                    System.out.println("\nAgora você segue o usuário @" + seguido + "!");
                                } catch (Exception exc) {
                                    System.out.println("\nErro. " + exc.getMessage());
                                }

                                break;

                            case "5":
                                int numSeguidores = 0;
                                try {
                                    numSeguidores = myTwitter.numeroSeguidores(usuario);

                                    System.out.println("\nVocê é seguido por " + numSeguidores + " usuário(s)!");

                                    if (numSeguidores == 0) {
                                        break;
                                    }
                                } catch (Exception exc) {
                                    System.out.println("\nErro. " + exc.getMessage());
                                }

                                if (numSeguidores == 1)
                                    System.out.println("O usuário que segue você é: ");
                                else
                                    System.out.println("Os usuários que seguem você são: ");

                                try {
                                    for (Perfil seguidor : myTwitter.seguidores(usuario)) {
                                        System.out.println("@" + seguidor.getUsuario() + " é seu seguidor.");
                                    }
                                } catch (Exception exc) {
                                    System.out.println("\nErro. " + exc.getMessage());
                                }

                                break;

                            case "6":
                                int numSeguidos = numeroSeguidos(usuario, myTwitter);
                                System.out.println("\nVocê segue " + numSeguidos + " usuário(s)!");

                                if (numeroSeguidos(usuario, myTwitter) == 0) {
                                    break;
                                }

                                if (numSeguidos == 1)
                                    System.out.println("O usuário que você segue é: ");
                                else
                                    System.out.println("Os usuários que você segue são: ");

                                try {
                                    for (Perfil seguidos : myTwitter.seguidos(usuario)) {
                                        System.out.println("Você segue @" + seguidos.getUsuario() + ".");
                                    }
                                } catch (Exception exc) {
                                    System.out.println("\nErro. " + exc.getMessage());
                                }
                                break;

                            case "7":
                                out = true;
                                break;

                            case "0":
                                System.out.print("\nPrograma encerrado!");
                                System.exit(0);

                            default:
                                System.out.println("\nErro. Opção inválida!");
                                break;
                        }
                    }
                    break;

                case "3":
                    System.out.print("Digite o nome do usuário: ");
                    usuario = scanner.nextLine();

                    try {
                        myTwitter.cancelarPerfil(usuario);
                        System.out.println("\nPerfil desativado com sucesso!");
                    } catch (Exception exc) {
                        System.out.println("\nErro. " + exc.getMessage());
                    }
                    break;

                case "0":
                    System.out.print("\nPrograma encerrado!");
                    System.exit(0);

                default:
                    System.out.println("\nErro. Opção inválida!");
                    break;
            }

        }

    }

    public static void clearBuffer(Scanner scanner) {
        scanner.nextLine();
    }

    public static void menuLogin() {
        System.out.println("---------------------");
        System.out.println("1 - Criar perfil");
        System.out.println("2 - Acessar perfil");
        System.out.println("3 - Desativar perfil");
        System.out.println("0 - Encerrar programa");
        System.out.println("---------------------");
        System.out.print("Por favor, selecione uma opção: ");
    }

    public static void menuActions() {
        System.out.println("------------------------");
        System.out.println("1 - Tweetar");
        System.out.println("2 - Ver sua timeline");
        System.out.println("3 - Ver seus tweets");
        System.out.println("4 - Seguir um perfil");
        System.out.println("5 - Listar seguidores");
        System.out.println("6 - Listar seguidos");
        System.out.println("7 - Sair");
        System.out.println("0 - Encerrar o programa");
        System.out.println("------------------------");
        System.out.print("Por favor, selecione uma opção: ");
    }

    public static boolean verificaPerfil(String usuario, IRepositorioUsuario repositorio) {
        if (repositorio.buscar(usuario) == null) {
            return false;
        }

        return true;
    }

    public static int numeroSeguidos(String usuario, MyTwitter myTwitter) {
        Vector<Perfil> seguidos = new Vector<Perfil>();

        try {
            seguidos = myTwitter.seguidos(usuario);

            for (Perfil seguido : myTwitter.seguidos(usuario)) {
                if (seguido.isAtivo() == false) {
                    seguidos.remove(seguido);
                }
            }
        } catch (Exception exc) {
        }

        return seguidos.size();
    }

}