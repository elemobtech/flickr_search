package com.wluo.flickrsearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wluo.flickrsearch.R
import com.wluo.flickrsearch.model.GalleryItem

class PhotoAdapter
    : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {
    private var galleryItems: ArrayList<GalleryItem> = ArrayList()

    fun setItemList(itemList: ArrayList<GalleryItem>) {
        val oldCount = galleryItems.size
        galleryItems.addAll(itemList)
        notifyItemRangeChanged(oldCount, itemList.size)
    }

    fun cleanList() {
        galleryItems.clear()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_gallery, parent, false)
        return PhotoHolder(view)
    }

    override fun getItemCount() = galleryItems.size // returns the size of the list in the recyclerView

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val galleryItem = galleryItems[position]

        holder.bindGalleryItem(galleryItem)
    }

    inner class PhotoHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        private lateinit var galleryItem: GalleryItem
        private val imageView: ImageView = itemView.findViewById(R.id.image)
        private val textView: TextView = itemView.findViewById(R.id.title)

        fun bindGalleryItem(item: GalleryItem) {
            galleryItem = item
            textView.text = galleryItem.title
            val urlString = "https://live.staticflickr.com/${galleryItem.server}/${galleryItem.id}_${galleryItem.secret}_m.jpg"
            Glide.with(itemView)
                .load(urlString)
                .placeholder(R.drawable.placeholder)
                .into(imageView)
        }
    }
}