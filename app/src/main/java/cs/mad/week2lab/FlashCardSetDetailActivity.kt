package cs.mad.week2lab

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import cs.mad.week2lab.databinding.ActivityFlashcardSetDetailBinding
import cs.mad.week2lab.entities.Flashcard
import cs.mad.week2lab.entities.FlashcardDao
import androidx.lifecycle.lifecycleScope
import cs.mad.week2lab.entities.FlashcardSet

class FlashCardSetDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFlashcardSetDetailBinding
    private lateinit var flashCardAdapter: FlashCardAdapter
    private lateinit var flashcardDao: FlashcardDao
    private lateinit var flashcardSet : FlashcardSet


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardSetDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val setId = intent.getLongExtra("flashcardSetId", -1L)

        val db = AppDatabase.getDatabase(this)

        val pageTitleTextView = findViewById<TextView>(R.id.pageTitle)

        // loading flashcards to database
        flashcardDao = db.flashcardDao()

        // recycler view
        val recyclerView2 = findViewById<RecyclerView>(R.id.recycler_view2)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        flashCardAdapter = FlashCardAdapter(this, mutableListOf(), flashcardDao, lifecycleScope)
        recyclerView2.adapter = flashCardAdapter

        //backgroud thread for db
        runOnIO {
            val setDao = db.flashcardSetDao()
            flashcardSet = setDao.getById(setId)
            val flashcardsForSet = flashcardDao.getFlashcardsForSet(setId)

            runOnUiThread {
                pageTitleTextView.text = flashcardSet.term ?: "Flashcard Set"
                flashCardAdapter.refreshSet(flashcardsForSet.toMutableList())
            }
        }


        binding.delete.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        binding.add.setOnClickListener {
            val setId = intent.getLongExtra("flashcardSetId", -1L)
            val newCard = Flashcard(System.currentTimeMillis(), setId, "New Term", "New Defn")
            flashCardAdapter.addFlashcard(newCard)  //updating UI
            //update db
            runOnIO {
                flashcardDao.insertAll(listOf(newCard))
            }
            val snack = Snackbar.make(binding.root, "Added", Snackbar.LENGTH_SHORT)
            snack.show()

        }

        binding.study.setOnClickListener {
            val intent = Intent(this, StudySetActivity::class.java)
            intent.putExtra("flashcardSetId", setId)
            startActivity(intent)
        }

    }

}