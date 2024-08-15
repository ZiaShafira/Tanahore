package com.example.tanahore.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanahore.data.response.ArticlesItem
import com.example.tanahore.data.response.DataItem
import com.example.tanahore.databinding.ItemLayoutBinding
import com.example.tanahore.ui.DetailActivity

class ArticleAdapter: ListAdapter<Any, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    inner class DataItemViewHolder(private val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dataItem: DataItem){
            binding.tvName.text = dataItem.name

            Glide
                .with(itemView.context)
                .load(dataItem.image)
                .into(binding.ivPhoto)

            binding.root.setOnClickListener{
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.DETAIL, dataItem.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    inner class ArticlesItemViewHolder(private val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(articlesItem: ArticlesItem){
            binding.tvName.text = articlesItem.name

            Glide
                .with(itemView.context)
                .load(articlesItem.image)
                .into(binding.ivPhoto)

            binding.root.setOnClickListener{
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.DETAIL, articlesItem.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem -> VIEW_TYPE_DATA_ITEM
            is ArticlesItem -> VIEW_TYPE_ARTICLES_ITEM
            else -> throw IllegalArgumentException("Unsupported item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            VIEW_TYPE_DATA_ITEM -> DataItemViewHolder(view)
            VIEW_TYPE_ARTICLES_ITEM -> ArticlesItemViewHolder(view)
            else -> throw IllegalArgumentException("Unsupported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is DataItem -> (holder as DataItemViewHolder).bind(item)
            is ArticlesItem -> (holder as ArticlesItemViewHolder).bind(item)
        }
    }

    companion object{
        private const val VIEW_TYPE_DATA_ITEM = 0
        private const val VIEW_TYPE_ARTICLES_ITEM = 1

        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Any>(){
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return when {
                    oldItem is DataItem && newItem is DataItem -> oldItem.id == newItem.id
                    oldItem is ArticlesItem && newItem is ArticlesItem -> oldItem.id == newItem.id
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return when {
                    oldItem is DataItem && newItem is DataItem -> oldItem.name == newItem.name && oldItem.image == newItem.image
                    oldItem is ArticlesItem && newItem is ArticlesItem -> oldItem.name == newItem.name && oldItem.image == newItem.image
                    else -> false
                }
            }
        }
    }
}