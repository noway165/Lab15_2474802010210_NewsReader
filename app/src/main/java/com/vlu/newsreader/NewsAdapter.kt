package com.vlu.newsreader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vlu.newsreader.databinding.ItemNewsBinding
import com.vlu.newsreader.model.NewsItem

class NewsAdapter : ListAdapter<NewsItem, NewsAdapter.VH>(Diff) {

    class VH(val b: ItemNewsBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.b.tvTitle.text = item.title
        holder.b.tvBody.text = item.body
    }

    private object Diff : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(a: NewsItem, b: NewsItem) = a.id == b.id
        override fun areContentsTheSame(a: NewsItem, b: NewsItem) = a == b
    }
}
