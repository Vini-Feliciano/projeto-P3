package org.upe.ui;

import org.upe.model.Usuario;
import org.upe.business.UsuarioBusiness;

public class UsuarioUI {

    private final UsuarioBusiness usuarioBusiness;
    private final InputHandler inputHandler;

    public UsuarioUI(UsuarioBusiness usuarioBusiness, InputHandler inputHandler) {
        this.usuarioBusiness = usuarioBusiness;
        this.inputHandler = inputHandler;
    }

    public void gerenciarUsuarios() {
        while (true) {
            System.out.println("\n--- Gerenciar Usuários ---");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Usuários");
            System.out.println("3. Atualizar Usuário");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = inputHandler.readIntInput();

            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    listarUsuarios();
                    break;
                case 3:
                    atualizarUsuario();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = inputHandler.readLine();
        System.out.print("Login: ");
        String login = inputHandler.readLine();
        System.out.print("Senha: ");
        String senha = inputHandler.readLine();
        System.out.print("É administrador? (s/n): ");
        boolean isAdmin = inputHandler.readLine().equalsIgnoreCase("s");

        try {
            usuarioBusiness.cadastrarUsuario(nome, login, senha, isAdmin);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarUsuarios() {
        System.out.println("\n--- Lista de Usuários ---");
        usuarioBusiness.listarTodosUsuarios().forEach(u -> {
            System.out.println(String.format("ID: %d, Nome: %s, Login: %s, Admin: %b",
                    u.getId(), u.getNome(), u.getEmail(), u.getAdmin()));
        });
    }

    private void atualizarUsuario() {
        System.out.print("Login do usuário para atualizar: ");
        String login = inputHandler.readLine();
        Usuario usuario = usuarioBusiness.buscarUsuario(login);

        if (usuario != null) {
            System.out.print("Novo nome (atual: " + usuario.getNome() + "): ");
            String nome = inputHandler.readLine();
            if (!nome.isEmpty()) usuario.setNome(nome);

            System.out.print("É administrador? (s/n) (atual: " + (usuario.getAdmin() ? "s" : "n") + "): ");
            String adminInput = inputHandler.readLine();
            if (!adminInput.isEmpty()) usuario.setAdmin(adminInput.equalsIgnoreCase("s"));

            usuarioBusiness.atualizarUsuario(usuario);
            System.out.println("Usuário atualizado com sucesso!");
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }
}
