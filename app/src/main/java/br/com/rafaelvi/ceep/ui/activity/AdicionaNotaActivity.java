package br.com.rafaelvi.ceep.ui.activity;

import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.rafaelvi.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.rafaelvi.ceep.R;
import br.com.rafaelvi.ceep.model.Nota;

public class AdicionaNotaActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adiciona_nota);




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
        setResult(CODIGO_RESULTADO_NOTA_CRIADA, intent);
    }

    private @NonNull Nota getNota() {
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);
        Nota notaCriada = new Nota(titulo.getText().toString(),
                descricao.getText().toString());
        return notaCriada;
    }
}