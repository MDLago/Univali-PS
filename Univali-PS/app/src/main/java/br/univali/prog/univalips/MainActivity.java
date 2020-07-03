package br.univali.prog.univalips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.univali.prog.univalips.dominio.Usuario;
import br.univali.prog.univalips.persistencia.DB;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar,btnRegistrar, btnConfirmar;
    private EditText etEmail, etSenha;
    private Enum selecao;
    private DB db;

    private static final String CHANNEL_ID = "1706";

    private enum Selecao{
        ENTRAR,REGISTRO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(getApplicationContext());
        db.criarDB();

        btnEntrar = findViewById(R.id.btn_loginEntrar);
        btnRegistrar = findViewById(R.id.btn_loginRegistrar);
        btnConfirmar = findViewById(R.id.btn_loginConfirmar);

        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);

        selecao = Selecao.ENTRAR;
    }

    private void exibirMensagem(String msg,int tempo){
        Toast.makeText(getApplicationContext(),msg,tempo).show();
    }

    private boolean isEditTextEmpty(EditText et) {
        return et.getText().toString().trim().length() == 0;
    }

    private boolean isEmailJaCadastrado(String email) throws Exception{

        List<Usuario> usuarios = db.buscarUsuario();

        if(usuarios == null){
            return false;
        }

        for (Usuario usuario :
                usuarios) {
            if(usuario.email.equals(email)){
                return true;
            }
        }

        return false;
    }

    private void notificacao(String msg){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nome = "Univali PS";
            String descricao = msg;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel canal = new NotificationChannel(CHANNEL_ID, nome, importance);
            canal.setDescription(descricao);
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(canal);
        }

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);

        Notification n  = new Notification.Builder(this,CHANNEL_ID)
                .setContentTitle("Univali PS")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher))
                .setContentText(msg)
                .setContentIntent(p)
                .setAutoCancel(true).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }

    public void botaoEntrarSelecionado(View v){
        btnRegistrar.setBackground(getResources().getDrawable(R.drawable.botao_arredondado_claro));
        btnRegistrar.setTextColor(getResources().getColor(R.color.fontePrimary));;
        btnEntrar.setBackground(getResources().getDrawable(R.drawable.botao_arredondado_azul));
        btnEntrar.setTextColor(getResources().getColor(R.color.branco));;

        selecao = Selecao.ENTRAR;
    }

    public void botaoRegistrarSelecionado(View v){
        btnRegistrar.setBackground(getResources().getDrawable(R.drawable.botao_arredondado_azul));
        btnRegistrar.setTextColor(getResources().getColor(R.color.branco));
        btnEntrar.setBackground(getResources().getDrawable(R.drawable.botao_arredondado_claro));
        btnEntrar.setTextColor(getResources().getColor(R.color.colorPrimary));

        selecao = Selecao.REGISTRO;
    }

    public void botaoConfirmar(View v){

        if(isEditTextEmpty(etEmail) || isEditTextEmpty(etSenha)){
            exibirMensagem("Existe campos vazios, favor preencher",1);
            return;
        }

        if(selecao == Selecao.REGISTRO){
            registrar();
        }else{
           login(); 
        }
    }

    private void registrar(){
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        try {
            if(isEmailJaCadastrado(email)){
                notificacao("Email já cadastrado");
                return;
            }else{
                db.inserirUsuario(email,senha);
            }
        }catch (Exception e){
            exibirMensagem(e.getMessage(),1);
        }

        notificacao("Usuario Cadastrado com sucesso");
        etEmail.setText("");
        etSenha.setText("");
        botaoEntrarSelecionado(null);
    }
    
    public void login(){
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        
        try{
            List<Usuario> usuarioList = db.buscarUsuario();

            senha = db.md5(senha);

            if(usuarioList == null){
                notificacao("Nenhum email cadastrado");
                return;
            }

            for (Usuario usuario :
                    usuarioList) {
                if(email.equals(usuario.email)){
                    if(senha.equals(usuario.senha)){
                        abrirTelaMenuPrincipal();
                        return;
                    }
                    notificacao("Senha incorreta");
                    return;
                }
            }
            notificacao("Email incorreto");
        }catch (Exception e){
            exibirMensagem(e.getMessage(),1);
        }
    }

    public void abrirTelaMenuPrincipal(){
        notificacao("teste");
    }
}