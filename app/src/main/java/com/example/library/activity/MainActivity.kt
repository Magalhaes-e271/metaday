package com.example.library.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.library.LivroAdapter
import com.example.library.R
import com.example.library.model.Usuario
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    // Classe de dados representando um livro
    data class Item(
        val id: Int,
        val img: String,
        val titulo: String,
        val autor: String,
        val ano: Int,
        val genero: String,
        val descricao: String,
        val disponivel: Boolean
    ) {

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val json = """
[
  {"id":1,"img": "memorias_postumas_bras_cubas" ,"titulo":"Memórias Póstumas de Brás Cubas","autor":"Machado de Assis","ano":1881,"genero":"Romance Realista","descricao":"Narrado por um defunto-autor, ironiza a sociedade carioca do século XIX.","disponivel":true},
  {"id":2,"img":"dom_casmurro","titulo":"Dom Casmurro","autor":"Machado de Assis","ano":1899,"genero":"Romance Psicológico","descricao":"Bentinho narra sua história de amor com Capitu, marcada por ciúmes e dúvida.","disponivel":false},
  {"id":3,"img":"primo_basilio","titulo":"O Primo Basílio","autor":"Eça de Queirós","ano":1878,"genero":"Romance","descricao":"Crítica à sociedade burguesa e às paixões humanas.","disponivel":true},
  {"id":4,"img":"a_reliquia","titulo":"A Relíquia","autor":"Eça de Queirós","ano":1887,"genero":"Sátira","descricao":"Teodorico Raposo viaja à Terra Santa em busca de uma relíquia.","disponivel":false},
  {"id":5,"img":"irmaos_karamazov","titulo":"Os Irmãos Karamázov","autor":"Fiódor Dostoiévski","ano":1880,"genero":"Romance Filosófico","descricao":"Explora conflitos morais e espirituais de uma família russa.","disponivel":true},
  {"id":6,"img":"crime_e_castigo","titulo":"Crime e Castigo","autor":"Fiódor Dostoiévski","ano":1866,"genero":"Romance Psicológico","descricao":"Raskólnikov mata uma agiota e sofre consequências morais e psicológicas.","disponivel":true},
  {"id":7,"img":"dracula","titulo":"Drácula","autor":"Bram Stoker","ano":1897,"genero":"Terror","descricao":"O conde Drácula chega à Inglaterra para espalhar o vampirismo.","disponivel":true},
  {"id":8,"img":"frankenstein","titulo":"Frankenstein","autor":"Mary Shelley","ano":1818,"genero":"Ficção Científica","descricao":"Um cientista cria um monstro e enfrenta as consequências de sua ambição.","disponivel":false},
  {"id":9,"img":"jurassic_park","titulo":"Jurassic Park","autor":"Michael Crichton","ano":1990,"genero":"Ficção Científica","descricao":"Cientistas recriam dinossauros em um parque temático.","disponivel":true},
  {"id":10,"img":"o_hobbit","titulo":"O Hobbit","autor":"J.R.R. Tolkien","ano":1937,"genero":"Fantasia","descricao":"Bilbo Bolseiro embarca em uma aventura para recuperar um tesouro.","disponivel":true}
]
""".trimIndent()



        // 2️⃣ Converte para lista de Item (livros)
        val livros = Gson().fromJson(json, Array<Item>::class.java).toList()

        // 3️⃣ Encontra o RecyclerView no layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // 4️⃣ Configura o RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LivroAdapter(livros)

        val textoEmail = findViewById<TextView>(R.id.edit_text_email)

        // Recupera os dados enviados
        val emailRecebido = intent.getStringExtra("email_usuario")
        val senhaRecebida = intent.getStringExtra("senha_usuario")
        var usuario = Usuario(
            null.toString(),
            email = emailRecebido.toString(),
            senha = senhaRecebida.toString(),
        )

        textoEmail.text = "Bem-vindo, ${usuario.email}!"

    }

}



