package org.upe.model;

import java.util.ArrayList;
import java.util.List;

public class SecaoTreino {
  private long id;
  private long usuarioId;
  private String nome;
  private List<PlanoTreino> planoTreinos;

  public SecaoTreino(long id, long usuarioId, String nome, List<PlanoTreino> planosTreinos) {
    this.id = id;
    this.usuarioId = usuarioId;
    this.nome = nome;
    this.planoTreinos = new ArrayList<>(planosTreinos);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public List<PlanoTreino> getPlanoTreinos() {
    return planoTreinos;
  }

  public void setPlanoTreinos(List<PlanoTreino> planoTreinos) {
    this.planoTreinos = planoTreinos;
  }

  
}