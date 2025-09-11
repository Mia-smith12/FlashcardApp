package cs.mad.week2lab

import cs.mad.week2lab.entities.Flashcard
import cs.mad.week2lab.entities.FlashcardSet
import cs.mad.week2lab.entities.getFlashcardSets
import cs.mad.week2lab.entities.getFlashcards
import org.junit.Test
import kotlin.reflect.KProperty

class Week2LabTests {

    @Test
    fun testFlashcard() {
        assert(sizeAndType<Flashcard>(getFlashcards()))

        assert(Flashcard::term is KProperty<String>)
        assert(Flashcard::definition is KProperty<String>)
    }

    @Test
    fun testFlashcardSet() {
        assert(sizeAndType<FlashcardSet>(getFlashcardSets()))
        assert(FlashcardSet::title is KProperty<String>)
    }

    private fun <T> sizeAndType(array: Array<T>) = sizeAndType(array.asList())
    private fun <T> sizeAndType(collection: Collection<T>) = collection.size == 10
    private fun <T> sizeAndType(other: Any) = false
}