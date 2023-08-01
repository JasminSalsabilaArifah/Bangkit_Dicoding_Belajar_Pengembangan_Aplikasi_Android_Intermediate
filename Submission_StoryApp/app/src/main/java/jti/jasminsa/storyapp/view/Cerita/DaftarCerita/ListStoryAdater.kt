package jti.jasminsa.storyapp.view.Cerita.DaftarCerita

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jti.jasminsa.storyapp.data.response.DaftarCeritaResponse
import jti.jasminsa.storyapp.data.response.ListStoryItem
import jti.jasminsa.storyapp.databinding.StoryItemBinding
import jti.jasminsa.storyapp.view.Cerita.DetailCerita.DetailCeritaActivity

class ListStoryAdater : PagingDataAdapter<ListStoryItem, ListStoryAdater.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        val story = listStory[position]
//        viewHolder.tvStory.text = story.name
//        Glide.with(viewHolder.itemView.context)
//            .load(story.photoUrl)
//            .into(viewHolder.ivStory)// URL Gambar
//        val detail = ListStoryItem(
//            listStory[viewHolder.adapterPosition].lon,
//            listStory[viewHolder.adapterPosition].lon,
//            listStory[viewHolder.adapterPosition].photoUrl,
//            listStory[viewHolder.adapterPosition].createdAt,
//            listStory[viewHolder.adapterPosition].name,
//            listStory[viewHolder.adapterPosition].description,
//            listStory[viewHolder.adapterPosition].id,
//            )
//        viewHolder.itemView.setOnClickListener{
//            val intent = Intent(viewHolder.itemView.context, DetailCeritaActivity::class.java)
//            intent.putExtra(DetailCeritaActivity.DETAIL, detail)
//            viewHolder.itemView.context.startActivity(intent)
//        }
//    }
//
//    override fun getItemCount() = listStory.size
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val ivStory: ImageView = view.findViewById(R.id.iv_picture)
//        val tvStory: TextView = view.findViewById(R.id.tv_name)
//    }
    class MyViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.tvName.text = data.name
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .into(binding.ivPicture)
        }
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            viewHolder.bind(story)
            val detail = ListStoryItem(
                story.lon,
                story.lon,
                story.photoUrl,
                story.createdAt,
                story.name,
                story.description,
                story.id,
            )
            viewHolder.itemView.setOnClickListener{
                val intent = Intent(viewHolder.itemView.context, DetailCeritaActivity::class.java)
                intent.putExtra(DetailCeritaActivity.DETAIL, detail)
                viewHolder.itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
