package com.example.library.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.library.R
import com.example.library.model.Book
import com.example.library.network.BookServiceKtor
import kotlinx.coroutines.launch

class AtualizarLivroActivity : AppCompatActivity() {

    private lateinit var editTitulo: EditText
    private lateinit var editAutor: EditText
    private lateinit var editDescricao: EditText
    private lateinit var editTipo: EditText
    private lateinit var editQuantia: EditText
    private lateinit var editImg: EditText
    private lateinit var btnAtualizar: Button
    private lateinit var progresso: ProgressBar
    private lateinit var btnVoltar: ImageButton
    private lateinit var imgCapa: ImageView
    private lateinit var btnGaleria: Button
    private lateinit var btnCamera: Button

    private var imagemUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imagemUri = it
            Glide.with(this).load(it).into(imgCapa)
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            imgCapa.setImageBitmap(it)
            // aqui você pode salvar temporariamente se quiser enviar ao servidor
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atualizar_livro)

        editTitulo = findViewById(R.id.editTitulo)
        editAutor = findViewById(R.id.editAutor)
        editDescricao = findViewById(R.id.editDescricao)
        editTipo = findViewById(R.id.editTipo)
        editQuantia = findViewById(R.id.editQuantia)
        editImg = findViewById(R.id.editImg)
        btnAtualizar = findViewById(R.id.btnAtualizarLivro)
        progresso = findViewById(R.id.progressoAtualizar)
        btnVoltar = findViewById(R.id.ImgButtonVoltarLivroId)
        imgCapa = findViewById(R.id.imgCapaPreview)
        btnGaleria = findViewById(R.id.btnGaleria)
        btnCamera = findViewById(R.id.btnCamera)

        val livro = intent.getSerializableExtra("livro") as? Book
        if (livro == null) {
            Toast.makeText(this, "Erro: livro não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        preencherCampos(livro)

        btnVoltar.setOnClickListener { finish() }

        btnGaleria.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnCamera.setOnClickListener {
            takePicture.launch(null)
        }

        btnAtualizar.setOnClickListener {
            atualizarLivro(livro)
        }
    }

    private fun preencherCampos(livro: Book) {
        editTitulo.setText(livro.titulo)
        editAutor.setText(livro.autor)
        editDescricao.setText(livro.descricao)
        editTipo.setText(livro.type)
        editQuantia.setText(livro.quantia?.toString() ?: "")
        editImg.setText(livro.img)

        Glide.with(this)
            .load(livro.img)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgCapa)
    }

    private fun atualizarLivro(livro: Book) {
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
        btnAtualizar.isEnabled = false

        val livroAtualizado = Book(
            id = livro.id,
            titulo = titulo,
            autor = autor,
            descricao = descricao,
            img = if (img.isEmpty()) "https://placehold.co/200x300" else img,
            type = tipo,
            quantia = quantia
        )

        lifecycleScope.launch {
            val resultado = livro.id?.let { BookServiceKtor.atualizarLivro(it, livroAtualizado) }

            progresso.visibility = View.GONE
            btnAtualizar.isEnabled = true

            if (resultado != null) {
                Toast.makeText(this@AtualizarLivroActivity, "Livro atualizado com sucesso!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this@AtualizarLivroActivity, "Erro ao atualizar livro!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
