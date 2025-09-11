package cs.mad.week2lab

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cs.mad.week2lab.entities.Flashcard
import cs.mad.week2lab.entities.FlashcardDao
import cs.mad.week2lab.entities.FlashcardSet
import cs.mad.week2lab.entities.FlashcardSetDao


    @Database(entities = [Flashcard::class, FlashcardSet::class], version = 1)
    abstract class AppDatabase: RoomDatabase() {
        abstract fun flashcardDao(): FlashcardDao
        abstract fun flashcardSetDao(): FlashcardSetDao

        companion object {
            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "flashcard-db"
                    ).build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
