package br.com.rafaelvi.ceep.ui.activity;

import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.rafaelvi.ceep.R;
import br.com.rafaelvi.ceep.model.Nota;

public class FormularioNotaActivity extends AppCompatActivity {

    public static final String TITULO_ADICIONA = "Adicionar nota";
    public static final String TITULO_ALTERA = "Alterar nota";
    private int posicaoRecebida = POSICAO_INVALIDA; //Posição default inválida
    private TextView titulo;
    private TextView descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adiciona_nota);
        setTitle(TITULO_ADICIONA);
        inicializaCampos();

        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)){
            setTitle(TITULO_ALTERA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            preencheCampos(notaRecebida);
        }
    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adiciona_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==  R.id.menu_adiciona_nota_ic_salva){
            Nota notaCriada = getNota();
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota notaCriada) {
        Intent intent = new Intent();
        intent.putExtra(CHAVE_NOTA, notaCriada);
        intent.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, intent);
    }

    private @NonNull Nota getNota() {
        return new Nota(titulo.getText().toString(),
                descricao.getText().toString());
    }
}