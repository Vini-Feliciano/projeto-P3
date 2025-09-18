package org.upe.interfaces;

import java.util.List;
import java.util.Optional;

import org.upe.model.Usuario;

public interface UsuarioInterface {
  Usuario salvar(Usuario usuario);
  Usuario encontrarUsuario(String email);
  List<Usuario> carregar();
}