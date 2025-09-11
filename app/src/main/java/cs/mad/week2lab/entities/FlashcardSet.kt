package cs.mad.week2lab.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity
data class FlashcardSet(
    @PrimaryKey
    val id: Long,
    var term: String? //nullable
)


@Dao
interface FlashcardSetDao {
    @Query("select * from flashcardset")
    suspend fun getAll(): List<FlashcardSet>

    @Insert
    suspend fun insertAll(flashcardset: List<FlashcardSet>)

    @Update
    suspend fun update(flashcardset: FlashcardSet)

    @Delete
    suspend fun delete(flashcardset: FlashcardSet)

    @Query("delete from flashcardset")
    suspend fun deleteAll()

    @Query("SELECT * FROM flashcardset WHERE id = :setId LIMIT 1")
    suspend fun getById(setId: Long): FlashcardSet

}

