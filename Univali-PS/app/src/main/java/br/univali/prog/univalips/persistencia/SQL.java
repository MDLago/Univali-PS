package br.univali.prog.univalips.persistencia;

public abstract class SQL {

    //region Criação
    public static String SQL_TabelaUsuario() {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE IF NOT EXISTS usuario (");
        sql.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sql.append("email VARCHAR(100), ");
        sql.append("senha VARCHAR(32)");
        sql.append(");");

        return sql.toString();
    }

    //endregion

    //region INSERTS
    public static String SQL_InsertUsuario(){

        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO usuario (email,senha) ");
        sql.append("VALUES (?,?)");

        return sql.toString();
    }


    //endregion

    //region UPDATES
    public static String SQL_UpdateUsuario(){
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE usuario SET ");
        sql.append("email = ?,");
        sql.append("senha = ?");

        sql.append(" WHERE _id = ?");

        return sql.toString();
    }

    //endregion

    //region SELECTS

    public static String SQL_SelectUsuario(){
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM usuario");

        return sql.toString();
    }

    public static String SQL_SelectUsuarioFromID(){
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM usuario ");
        sql.append("WHERE _id = ?");

        return sql.toString();
    }

    //endregion

    //region DELETES
    public static String SQL_DeleteUsuarioFromID(){
        StringBuilder sql = new StringBuilder();

        sql.append("DELETE FROM usuario ");
        sql.append("WHERE _id = ?");

        return sql.toString();
    }

    //endregion

}
