package com.example.library.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library.R
import com.example.library.model.Book
import com.example.library.network.BookServiceKtor
import kotlinx.coroutines.launch

class CadastrarLivroActivity : AppCompatActivity() {

    private lateinit var editTitulo: EditText
    private lateinit var editAutor: EditText
    private lateinit var editDescricao: EditText
    private lateinit var editTipo: EditText
    private lateinit var editQuantia: EditText
    private lateinit var editImg: EditText
    private lateinit var btnCadastrar: Button
    private lateinit var progresso: ProgressBar
    private lateinit var btnVoltar: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_livro)

        // Inicializa os componentes
        btnVoltar = findViewById(R.id.btnVoltarCadastro)
        editTitulo = findViewById(R.id.editTitulo)
        editAutor = findViewById(R.id.editAutor)
        editDescricao = findViewById(R.id.editDescricao)
        editTipo = findViewById(R.id.editTipo)
        editQuantia = findViewById(R.id.editQuantia)
        editImg = findViewById(R.id.editImg)
        btnCadastrar = findViewById(R.id.btnCadastrarLivro)
        progresso = findViewById(R.id.progressoCadastro)
        progresso = findViewById(R.id.progressoCadastro)

        btnCadastrar.setOnClickListener {
            cadastrarLivro()
        }
        btnVoltar.setOnClickListener{
            finish()
        }
    }

    private fun cadastrarLivro() {
        val titulo = editTitulo.text.toString().trim()
        val autor = editAutor.text.toString().trim()
        val descricao = editDescricao.text.toString().trim()
        val tipo = editTipo.text.toString().trim()
        val quantiaText = editQuantia.text.toString().trim()
        val img = editImg.text.toString().trim()

        if (titulo.isEmpty() || autor.isEmpty() || descricao.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
            return
        }

        val quantia = quantiaText.toIntOrNull()

        progresso.visibility = View.VISIBLE
        btnCadastrar.isEnabled = false

        val novoLivro = Book(
            id = null,
            titulo = titulo,
            autor = autor,
            descricao = descricao,
            img = img.ifEmpty { "https://placehold.co/200x300" }, // imagem padrão
            type = tipo,
            quantia = quantia
        )

        lifecycleScope.launch {
            val resultado = BookServiceKtor.cadastrarLivro(novoLivro)

            progresso.visibility = View.GONE
            btnCadastrar.isEnabled = true

            if (resultado != null) {
                Toast.makeText(this@CadastrarLivroActivity, "Livro cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                limparCampos()
            } else {
                Toast.makeText(this@CadastrarLivroActivity, "Erro ao cadastrar livro!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun limparCampos() {
        editTitulo.text.clear()
        editAutor.text.clear()
        editDescricao.text.clear()
        editTipo.text.clear()
        editQuantia.text.clear()
        editImg.text.clear()
    }
}
