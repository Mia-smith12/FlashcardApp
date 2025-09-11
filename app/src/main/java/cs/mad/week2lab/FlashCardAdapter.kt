package cs.mad.week2lab

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cs.mad.week2lab.entities.Flashcard
import cs.mad.week2lab.entities.FlashcardDao
import cs.mad.week2lab.entities.FlashcardSet
import kotlinx.coroutines.*

// context:this activity
class FlashCardAdapter (val context: Context, val items:MutableList<Flashcard>, private val flashcardDao: FlashcardDao,
                        private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<FlashCardAdapter.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val termTextView: TextView = itemView.findViewById(R.id.flashCardTerm)
        val defnTextView: TextView = itemView.findViewById(R.id.flashCardDefn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.flashcard_item, parent, false)
        return ViewHolder(view)
    }
    // on start up
    fun refreshSet(newFlashcards: MutableList<Flashcard>){
        items.clear()
        items.addAll(newFlashcards)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val flashcard = items[pos]
        holder.termTextView.text = flashcard.term
        holder.defnTextView.text = flashcard.definition


// flashcard clicked
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            AlertDialog.Builder(context)
                .setTitle(flashcard.term)
                .setMessage(flashcard.definition)
                .setPositiveButton("Edit") { _, _ ->
                    showEditDialog(context, flashcard, pos)
                }
                .setNegativeButton("Close", null)
                .show()
        }

    }
// edit flashcard
    fun showEditDialog(context: Context, flashcard: Flashcard, position: Int ){
        val layout = LinearLayout(context).apply{
            orientation = LinearLayout.VERTICAL
            setPadding(50,50,50,50)
        }

        val termEdit = EditText(context).apply{
            hint = "term"
            setText(flashcard.term)
        }

        val defnEdit = EditText(context).apply{
            hint = "definition"
            setText(flashcard.definition)
        }

    layout.addView(termEdit)
    layout.addView(defnEdit)

    val dialog = AlertDialog.Builder(context)
        .setTitle("Edit flashcard")
        .setView(layout)
        .setPositiveButton("Done",null)
        .setNeutralButton("Delete",null)
        .create()

    dialog.setOnShowListener{
        val doneB = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val deleteB = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)

        doneB.setOnClickListener{
            val newTerm = termEdit.text.toString()
            val newDef = defnEdit.text.toString()

            flashcard.term = newTerm
            flashcard.definition = newDef

            coroutineScope.launch(Dispatchers.IO) {
                flashcardDao.update(flashcard)
                withContext(Dispatchers.Main) {
                    notifyItemChanged(position)
                    dialog.dismiss()
                }
            }
        }

        deleteB.setOnClickListener{
            coroutineScope.launch {
                flashcardDao.delete(flashcard)
                withContext(Dispatchers.Main) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    dialog.dismiss()
                }
            }
        }
    }
    dialog.show()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addFlashcard(addFlash: Flashcard){
        items.add(addFlash)
        notifyItemInserted(items.size-1)
    }

    fun removeAt(index: Int) {
        items.removeAt(index)
        notifyDataSetChanged()
    }

}