package br.univali.prog.univalips.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import br.univali.prog.univalips.dominio.Usuario;

public class DB extends SQLiteOpenHelper {

    private static final String nomeDB = "univalips.db";
    private static final int versaoDB = 1;
    private Context context;
    private SQLiteDatabase db;

    //region Implementação Obrigatória
    public DB(@Nullable Context context) {
        super(context, nomeDB, null, versaoDB);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //endregion

    //region Metodos
    private void abrirDB() throws SQLException {
        db = context.openOrCreateDatabase(nomeDB,Context.MODE_PRIVATE,null);
    }
    private void fecharDB() throws  SQLException{
        db.close();
    }
    public void criarDB() throws SQLException {
        abrirDB();

        String sql = SQL.SQL_TabelaUsuario();
        db.execSQL(sql);

        fecharDB();
    }
    public void deletarDB() throws SQLException{
        context.deleteDatabase(nomeDB);
    }


    private Cursor getCursor (String sql) throws  SQLException{
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }
    public String md5(String s) throws Exception{

        final String MD5 = "MD5";
        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest
                .getInstance(MD5);
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    }

    public boolean checkPassword(String psw, String pswMD5) throws Exception{
        return md5(psw).equals(pswMD5);
    }
    //endregion

    //region Funções Inserts
    public void inserirUsuario(@Nullable String email, @Nullable String senha) throws Exception{

        abrirDB();

        SQLiteStatement sqtmt = db.compileStatement(SQL.SQL_InsertUsuario());

        sqtmt.bindString(1,email);
        sqtmt.bindString(2,md5(senha));

        sqtmt.executeInsert();
        fecharDB();
    }

    //endregion

    //region Funções Updates
    public void atualizarUsuario(@Nullable String email, @Nullable String senha, int id) throws SQLException {

        abrirDB();

        SQLiteStatement sqtmt = db.compileStatement(SQL.SQL_UpdateUsuario());

        sqtmt.bindString(1,email);
        sqtmt.bindString(2,senha);

        sqtmt.bindLong(3, id);

        sqtmt.executeUpdateDelete();
        fecharDB();
    }

    //endregion

    //region Funções Selects

    public Usuario buscaUsuarioFromID(int idRv) throws SQLException{
        Usuario usuario;

        abrirDB();

        String sql = SQL.SQL_SelectUsuarioFromID();
        sql = sql.replace("?",String.valueOf(idRv));
        Cursor cursor = getCursor(sql);

        if(cursor == null || cursor.getCount() == 0){
            return null;
        }

        cursor.moveToFirst();

        int id = cursor.getInt(0);
        String email = cursor.getString(1);
        String senha = cursor.getString(2);

        db.close();

        usuario = new Usuario(id,email,senha);
        return usuario;
    }

    public List<Usuario> buscarUsuario() throws SQLException{
        ArrayList<Usuario> arrayList = new ArrayList<>();

        abrirDB();
        Cursor cursor = getCursor(SQL.SQL_SelectUsuario());

        if(cursor == null || cursor.getCount() == 0){
            return null;
        }

        cursor.moveToFirst();

        do{
            int id = cursor.getInt(0);
            String email = cursor.getString(1);
            String senha = cursor.getString(2);

            Usuario usuario = new Usuario(id,email,senha);
            arrayList.add(usuario);

        }while(cursor.moveToNext());
        fecharDB();
        return arrayList;
    }

    //endregion

    //region Funções Deletes
    public void excluirUsuario(int id) throws SQLException{
        abrirDB();

        SQLiteStatement sqtmt = db.compileStatement(SQL.SQL_DeleteUsuarioFromID());

        sqtmt.bindLong(1,id);

        sqtmt.executeUpdateDelete();
        fecharDB();
    }
    //endregion
}
