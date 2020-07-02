package br.univali.prog.univalips;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.univali.prog.univalips.dominio.Usuario;
import br.univali.prog.univalips.persistencia.DB;

public class MainActivity extends AppCompatActivity {

    Button btnEntrar,btnRegistrar;
    EditText etEmail, etSenha;
    Enum selecao;
    DB db;

    private enum Selecao{
        ENTRAR,REGISTRO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(getApplicationContext());

        btnEntrar = findViewById(R.id.btn_loginEntrar);
        btnRegistrar = findViewById(R.id.btn_loginRegistrar);

        selecao = Selecao.ENTRAR;
    }

    private void exibirMensagem(String msg,int tempo){
        Toast.makeText(getApplicationContext(),msg,tempo).show();
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
        if(etEmail.getText() == null || etSenha.getText() == null){
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
            db.inserirUsuario(email,senha);
        }catch (Exception e){
            exibirMensagem(e.getMessage(),1);
        }
    }
    
    public void login(){
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        
        try{
            List<Usuario> usuarioList = db.buscarUsuario();

            if(usuarioList == null){
                exibirMensagem("Nenhum email cadastrado",1);
                return;
            }

            for (Usuario usuario :
                    usuarioList) {
                if(email.equals(usuario.email)){
                    if(senha.equals(usuario.senha)){
                        abrirTelaMenuPrincipal();
                        return;
                    }
                    exibirMensagem("Senha incorreta",0);
                    return;
                }
            }
            exibirMensagem("Email incorreto",0);
        }catch (Exception e){
            exibirMensagem(e.getMessage(),1);
        }
    }

    public void abrirTelaMenuPrincipal(){

    }
}