package com.example.roteirodeteste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLite extends SQLiteOpenHelper {

    private static final int VERSAO_BANCO = 1;
    private static final String BANCO_CONTATO = "db_contato";
    private static final String TABELA_CONTATO = "tb_contato";
    private static final String COLUNA_CODIGO = "codigo";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_TELEFONE = "telefone";
    private static final String COLUNA_EMAIL = "email";
    private static Contato contato;

    public SQLite(@Nullable Context context) {
        super(context, BANCO_CONTATO, null, VERSAO_BANCO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABELA_CONTATO + "(" +
                COLUNA_CODIGO + " INTEGER PRIMARY KEY, " +
                COLUNA_NOME + " TEXT, " +
                COLUNA_TELEFONE + " TEXT, " +
                COLUNA_EMAIL + " TEXT)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addContato(Contato contato) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUNA_NOME, contato.getNome());
        values.put(COLUNA_TELEFONE, contato.getTelefone());
        values.put(COLUNA_EMAIL, contato.getEmail());

        db.insert(TABELA_CONTATO, null, values);
    }

    public void atualizaContato(Contato contato) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUNA_NOME, contato.getNome());
            values.put(COLUNA_TELEFONE, contato.getTelefone());
            values.put(COLUNA_EMAIL, contato.getEmail());

            db.update(TABELA_CONTATO, values, COLUNA_CODIGO + " = ?",
                    new String[]{String.valueOf(contato.getCodigo())});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void apagarContato(Contato contato) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            db.delete(TABELA_CONTATO, COLUNA_CODIGO + " = ?",
                    new String[]{String.valueOf(contato.getCodigo())});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Contato selecionarContato(int codigo) {
        try {

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABELA_CONTATO, new String[]{COLUNA_CODIGO, COLUNA_NOME,
                            COLUNA_TELEFONE, COLUNA_EMAIL}, COLUNA_CODIGO + " = ?",
                    new String[]{String.valueOf(codigo)}, null, null, null, null);

            if (cursor != null) {
                cursor.moveToNext();
            }

            contato = new Contato(Integer.parseInt(cursor.getString(0)
            ), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contato;
    }

    public List<Contato> listaTodosContatos() {
        List<Contato> listaContatos = new ArrayList<Contato>();

        try {
            String query = "SELECT * FROM " + TABELA_CONTATO;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Contato contato = new Contato();
                    contato.setCodigo(Integer.parseInt(cursor.getString(0)));
                    contato.setNome(cursor.getString(1));
                    contato.setTelefone(cursor.getString(2));
                    contato.setEmail(cursor.getString(3));
                    listaContatos.add(contato);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaContatos;
    }
}