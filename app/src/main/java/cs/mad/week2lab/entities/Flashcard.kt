package cs.mad.week2lab.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity
data class Flashcard(
@PrimaryKey
val id: Long,   // Non-nullable
val setId: Long,
var term: String,
var definition: String
)

@Dao
interface FlashcardDao {
    @Query("select * from flashcard")
    suspend fun getAll(): List<Flashcard>

    @Insert
    suspend fun insertAll(flashcard: List<Flashcard>)

    @Update
    suspend fun update(flashcard: Flashcard)

    @Delete
    suspend fun delete(flashcard: Flashcard)

    @Query("delete from flashcard")
    suspend fun deleteAll()

    @Query("DELETE FROM flashcard WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM flashcard WHERE setId = :setId")
    suspend fun getFlashcardsForSet(setId: Long): List<Flashcard>

}

