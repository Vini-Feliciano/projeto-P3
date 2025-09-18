package org.upe.interfaces;

import org.upe.model.SecaoTreino;

import java.util.List;
import java.util.Optional;

public interface SecaoDeTreinoInterface {
    SecaoTreino salvar(SecaoTreino secaoTreino);
    List<SecaoTreino> encontrarPorUsuario(long usuarioId);
    SecaoTreino encontrarTreino(long id);
    List<SecaoTreino> carregar();
}
