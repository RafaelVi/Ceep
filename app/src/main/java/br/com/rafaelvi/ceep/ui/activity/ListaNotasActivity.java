package br.com.rafaelvi.ceep.ui.activity;

import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.rafaelvi.ceep.R;
import br.com.rafaelvi.ceep.dao.NotaDAO;
import br.com.rafaelvi.ceep.model.Nota;
import br.com.rafaelvi.ceep.ui.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {


    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        List<Nota> todasNotas = getNotas();
        configuraRecyclerView(todasNotas);
        configuraBotao();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehResultadoComNota(requestCode, resultCode, data)) {
            Nota nota = (Nota) data.getSerializableExtra("nota");
            adicionaNota(nota);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configuraBotao() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(view -> vaiParaAdicionaNotaAcitivity());
    }

    private void vaiParaAdicionaNotaAcitivity() {
        Intent abrirAdicionaNotas = new Intent(ListaNotasActivity.this, AdicionaNotaActivity.class);
        startActivityForResult(abrirAdicionaNotas, CODIGO_REQUISICAO_INSERE_NOTA);
    }


    private void adicionaNota(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA && resultCode == CODIGO_RESULTADO_NOTA_CRIADA && temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra("nota");
    }

    private List<Nota> getNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycler_view);
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }
}