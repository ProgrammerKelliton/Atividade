package com.example.roteirodeteste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    EditText edit_codigo, edit_nome, edit_telefone, edit_email;
    Button salvar, limpar, excluir;
    ListView listViewContatos;
    SQLite db = new SQLite(MainActivity.this);

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_codigo = findViewById(R.id.codigo);
        edit_nome = findViewById(R.id.nome);
        edit_telefone = findViewById(R.id.telefone);
        edit_email = findViewById(R.id.email);

        salvar = findViewById(R.id.salvar);
        limpar = findViewById(R.id.limpar);
        excluir = findViewById(R.id.excluir);
        listViewContatos = findViewById(R.id.listViewContatos);
        listarClientes();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String codigo = edit_codigo.getText().toString();
                    String nome = edit_nome.getText().toString();
                    String telefone = edit_telefone.getText().toString();
                    String email = edit_email.getText().toString();

                    if (nome.isEmpty() && (telefone.isEmpty())) {
                        edit_nome.setError("Esse campo é obrigatório!");
                        edit_telefone.setError("Este camoo é obrigatório");
                    } else if (codigo.isEmpty()) {
                        //insert
                        db.addContato(new Contato(nome, telefone, email));
                        Toast.makeText(MainActivity.this, "Contato adicionado com sucesso", Toast.LENGTH_SHORT).show();
                        listarClientes();
                        limparCampos();

                    } else {
                        //update
                        db.atualizaContato(new Contato(Integer.parseInt(codigo), nome, telefone, email));
                        Toast.makeText(MainActivity.this, "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show();
                        limparCampos();
                        listarClientes();
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = edit_codigo.getText().toString();

                if(codigo.isEmpty()){
                    Toast.makeText(MainActivity.this, "Nenhum Contato está selecionado", Toast.LENGTH_SHORT).show();
                }else{
                    Contato contato = new Contato();
                    contato.setCodigo(Integer.parseInt(codigo));
                    db.apagarContato(contato);

                    Toast.makeText(MainActivity.this, "Contato excluído com sucesso", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    listarClientes();

                }
            }
        });

        listViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conteudo = (String) listViewContatos.getItemAtPosition(position);
                String codigo = conteudo.substring(0, conteudo.indexOf("-"));
                Contato contato = db.selecionarContato(Integer.parseInt(codigo));

                edit_codigo.setText(String.valueOf(contato.getCodigo()));
                edit_nome.setText(contato.getNome());
                edit_telefone.setText(contato.getTelefone());
                edit_email.setText(contato.getEmail());

            }
        });
    }

    public void listarClientes() {

        List<Contato> contatos = db.listaTodosContatos();

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        listViewContatos.setAdapter(adapter);

        for (Contato c : contatos) {
            arrayList.add(c.getCodigo() + "-" + c.getNome() + "-" + c.getTelefone() +"\nEmail: " + c.getEmail());
            adapter.notifyDataSetChanged();
            //Log.i("saida", "Lista: " + c.getNome());
        }
    }
    public void limparCampos() {

        edit_codigo.setText("");
        edit_nome.setText("");
        edit_telefone.setText("");
        edit_email.setText("");

        edit_nome.requestFocus();

    }
}