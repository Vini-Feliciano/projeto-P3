package org.upe.interfaces;

import org.upe.model.IndicadorBiomedico;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IndicadorBiomedicoInterface {
    IndicadorBiomedico salvar(IndicadorBiomedico indicador);
    List<IndicadorBiomedico> encontrarPeloUsuario(long id);
    List<IndicadorBiomedico> carregar();
    List<IndicadorBiomedico> encontrarPorData(long UsuarioId, LocalDate comeco, LocalDate fim);
    IndicadorBiomedico buscarPorId(long id);
}
