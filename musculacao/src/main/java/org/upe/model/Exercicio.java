package org.upe.model;

public class Exercicio {
    private long id;
    private String nome;
    private String descricao;
    private String gifPath;

    public Exercicio(long id, String nome, String descricao, String gifPath) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.gifPath = gifPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGifPath() {
        return gifPath;
    }

    public void setGifPath(String gifPath) {
        this.gifPath = gifPath;
    }

    @Override
    public String toString() {
        return "Exercicio [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", gifPath=" + gifPath + "]";
    }
    
}