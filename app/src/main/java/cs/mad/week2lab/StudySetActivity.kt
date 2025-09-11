package cs.mad.week2lab

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cs.mad.week2lab.databinding.ActivityFlashcardSetDetailBinding
import cs.mad.week2lab.databinding.ActivityStudySetBinding
import cs.mad.week2lab.entities.Flashcard

class StudySetActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStudySetBinding

    private var position = 0
    private var flashcards = mutableListOf<Flashcard>()
    private var cardsMissed = 0
    private val missedCards = mutableListOf<Flashcard>()
    private var cardsCorrect = 0
    private var completed = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudySetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val setId = intent.getLongExtra("flashcardSetId", -1L)
        if (setId != -1L) {
            val db = AppDatabase.getDatabase(this)
            val flashcardDao = db.flashcardDao()

            runOnIO {
                val loaded = flashcardDao.getFlashcardsForSet(setId)
                runOnUiThread {
                    flashcards = loaded.toMutableList()
                    if (flashcards.isNotEmpty()) {
                        binding.flashCard.text = flashcards[position].term
                    } else {
                        binding.flashCard.text = "No cards"
                    }
                }
            }

        } else {
            binding.flashCard.text = "Error loading set"
        }

        binding.numsmissed.text = "cards missed: $cardsMissed"
        binding.numscorrect.text = "cards correct: $cardsCorrect"
        binding.numscompleted.text = "completed: $completed"

        binding.flashCard.setOnClickListener(){
            currentCard()
        }

        binding.skip.setOnClickListener(){
           skip()
        }

        binding.missed.setOnClickListener(){
            missedCard()
        }

        binding.correct.setOnClickListener(){
            correct()
        }

        fun quitStudying(view: View) {
            finish()
        }
    }

    fun currentCard(){
        val card = flashcards[position]
        if (binding.flashCard.text == card.definition){
            binding.flashCard.text = card.term
        }
        else binding.flashCard.text = card.definition
    }


    fun missedCard(){
        cardsMissed++
        val missed = flashcards[position]
        missedCards.add(missed)

        binding.numsmissed.text = "cards missed: $cardsMissed"
        skip()
    }

    fun correct(){
        if (position in flashcards.indices) {
            val currentCard = flashcards[position]
            if (currentCard !in missedCards) {
                cardsCorrect++
                binding.numscorrect.text = "cards correct: $cardsCorrect"
            }
            completed++
            binding.numscompleted.text = "completed: $completed"

            flashcards.removeAt(position)

            if (flashcards.isEmpty()) {
                binding.flashCard.text = "Done"
                finish()
                return
            }

            if (position >= flashcards.size) {
                position = flashcards.size - 1
            }

            binding.flashCard.text = flashcards[position].term
        }
    }

    fun skip(){
        if (flashcards.isEmpty()) {
            binding.flashCard.text = "Done"
            finish()
            return
        }
        position++

        if (position >= flashcards.size) {
            binding.flashCard.text = "Done"
            finish()
            return
        } else {
            binding.flashCard.text = flashcards[position].term
        }
    }

}
