package org.upe.interfaces;

import java.time.LocalDate;
import java.util.List;

import org.upe.model.IndicadorBiomedico;

public interface IndicadorBiomedicoInterface {
    IndicadorBiomedico salvar(IndicadorBiomedico indicador);
    List<IndicadorBiomedico> encontrarPeloUsuario(long id);
    List<IndicadorBiomedico> carregar();
    List<IndicadorBiomedico> encontrarPorData(long UsuarioId, LocalDate comeco, LocalDate fim);
    IndicadorBiomedico buscarPorId(long id);
}
