package cs.mad.week2lab

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cs.mad.week2lab.entities.Flashcard
import cs.mad.week2lab.entities.FlashcardSet

// context:this activity


class FlashcardSetAdapter(val context: Context, val items:MutableList<FlashcardSet>) : RecyclerView.Adapter<FlashcardSetAdapter.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView = itemView.findViewById<TextView>(R.id.flashCardSet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.flashcard_set_item, parent, false)
        return ViewHolder(view)
    }

    // on start up
    fun refreshSet(newFlashcards: MutableList<FlashcardSet>){
        items.clear()
        items.addAll(newFlashcards)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val flashcard = items[pos]
        holder.textView.text = flashcard.term

        holder.itemView.setOnClickListener {
                val intent = Intent(context, FlashCardSetDetailActivity::class.java)
                intent.putExtra("flashcardSetId", flashcard.id)
                context.startActivity(intent)

        }
    }

    fun addFlashcard(addFlash: FlashcardSet){
        items.add(addFlash)
        notifyItemInserted(items.size-1)
    }

    fun removeAt(index: Int) {
        items.removeAt(index)
        notifyDataSetChanged()

    }

}