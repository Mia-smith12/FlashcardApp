package cs.mad.week2lab

import androidx.room.Room
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cs.mad.week2lab.databinding.ActivityMainBinding
import cs.mad.week2lab.entities.FlashcardSet
import android.content.SharedPreferences
import android.util.Log
import cs.mad.week2lab.entities.Flashcard
import cs.mad.week2lab.entities.FlashcardSetDao

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var flashCardSetAdapter: FlashcardSetAdapter
    private lateinit var flashcardSetDao: FlashcardSetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        flashCardSetAdapter = FlashcardSetAdapter(this, mutableListOf())
        recyclerView.adapter = flashCardSetAdapter

        // loading flashcards to database
        flashcardSetDao = db.flashcardSetDao()
        //backgroud thread for db
        runOnIO {
            val setDao = db.flashcardSetDao()
            val cardDao = db.flashcardDao()


            val existingSets = setDao.getAll()
            if (existingSets.isEmpty()) {
                val sets = mutableListOf<FlashcardSet>()
                for (i in 1..10) {
                    sets.add(FlashcardSet(id = i.toLong(), term = "Set $i"))
                }
                setDao.insertAll(sets)

                val flashcards = mutableListOf<Flashcard>()
                var flashcardId = 1L
                for (setId in 1..10) {
                    for (cardNum in 1..10) {
                        flashcards.add(
                            Flashcard(
                                id = flashcardId,
                                setId = setId.toLong(),
                                term = "Term $cardNum",
                                definition = "Defn $cardNum"
                            )
                        )
                        flashcardId++
                    }
                }
                cardDao.insertAll(flashcards)
            }

            val allSets = setDao.getAll()
            runOnUiThread {
                flashCardSetAdapter.refreshSet(allSets.toMutableList())
            }
        }

        binding.add.setOnClickListener{
            val newCard = FlashcardSet(System.currentTimeMillis(),"New Term")
            flashCardSetAdapter.addFlashcard(newCard)  //updating UI
            //update db
            runOnIO {
                flashcardSetDao.insertAll(listOf(newCard))
            }
            val snack = Snackbar.make(binding.root, "Added", Snackbar.LENGTH_SHORT)
            snack.show()
        }

    }

}