package org.upe.business;

import org.upe.model.Usuario;
import org.upe.interfaces.UsuarioInterface;

import java.util.List;
import java.util.Optional;

public class UsuarioBusiness {

    private final UsuarioInterface usuarioInterface;

    public UsuarioBusiness(UsuarioInterface usuarioInterface) {
        this.usuarioInterface = usuarioInterface;
    }

    public Usuario cadastrarUsuario(String nome, String email, String senha, boolean isAdmin) {
        
        List<Usuario> usuarios = listarTodosUsuarios();

        for (Usuario usuario : usuarios) {
          if (usuario.getEmail().equals(email)){
            throw new Error("Usuário já cadastrado");
          }
        }

        Usuario novoUsuario = new Usuario(0, nome, email, senha, isAdmin);
        return usuarioInterface.salvar(novoUsuario);
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Usuario usuario = usuarioInterface.encontrarUsuario(email);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        return null;
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioInterface.carregar();
    }

    public Usuario atualizarUsuario(Usuario usuario) {
        return usuarioInterface.salvar(usuario);
    }

    public boolean isAdmin(Usuario usuario) {
        return usuario.getAdmin();
    }

    public Usuario buscarUsuario(String email) {
        return usuarioInterface.encontrarUsuario(email);
    }
}
