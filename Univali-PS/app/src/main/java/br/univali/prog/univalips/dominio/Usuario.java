package br.univali.prog.univalips.dominio;

public class Usuario {

    public final int id;
    public final String email;
    public final String senha;

    public Usuario(int id, String email, String senha) {
        this.id = id;
        this.email = email;
        this.senha = senha;
    }
}
