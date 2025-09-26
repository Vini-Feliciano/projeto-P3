package org.upe.interfaces;

import java.util.List;

import org.upe.model.SecaoTreino;

public interface SecaoDeTreinoInterface {
    SecaoTreino salvar(SecaoTreino secaoTreino);
    List<SecaoTreino> encontrarPorUsuario(long usuarioId);
    SecaoTreino encontrarTreino(long id);
    List<SecaoTreino> carregar();
}
