package br.com.rafaelvi.ceep.ui.activity;

import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.rafaelvi.ceep.R;
import br.com.rafaelvi.ceep.dao.NotaDAO;
import br.com.rafaelvi.ceep.dao.recyclerciew.helper.callback.NotaItemTouchHelperCallback;
import br.com.rafaelvi.ceep.model.Nota;
import br.com.rafaelvi.ceep.ui.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {


    public static final String TITULO = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        setTitle(TITULO);
        List<Nota> todasNotas = getNotas();
        configuraRecyclerView(todasNotas);
        configuraBotao();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehAdicionaNotaRequest(requestCode, data) && resultadoOk(resultCode)) {
            Nota nota = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adicionaNota(nota);
        }

        if (ehAlteraNotaRequest(requestCode, data) && resultadoOk(resultCode)) {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicaoRecebida =  data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            if (posicaoRecebida > POSICAO_INVALIDA) {
                alteraNota(posicaoRecebida, notaRecebida);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private void alteraNota(int posicao, Nota nota) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehAlteraNotaRequest(int requestCode, @Nullable Intent data) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA &&
                temNota(data) && data.hasExtra(CHAVE_POSICAO);
    }

    private void configuraBotao() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(view -> vaiParaFormularioNotaActivityInsere());
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent abrirFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(abrirFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }


    private void adicionaNota(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehAdicionaNotaRequest(int requestCode, @Nullable Intent data) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA && temNota(data);
    }

    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private List<Nota> getNotas() {
        NotaDAO dao = new NotaDAO();
        for (int i = 1; i <= 10; i++) {
            dao.insere(new Nota("Nota " + i, "Descricao " + i));
        }
        return dao.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycler_view);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);


    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener((nota, posicao) -> vaiParaFormularioNotaActivityAltera(nota, posicao));
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void    vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}