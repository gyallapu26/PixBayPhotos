package com.example.pixbayphotos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pixbayphotos.R
import com.example.pixbayphotos.databinding.ItemImageBinding
import com.example.pixbayphotos.models.Image
import com.squareup.picasso.Picasso

class ImagesAdapter(private val onClick: (Image) -> Unit ): PagingDataAdapter<Image, ImagesAdapter.ImageViewHolder >(diffCallback = differCallback) {

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            with(binding) {
                binding.root.setOnClickListener { onClick(image) }
                Picasso.get().load(image.previewURL).error(R.drawable.no_image).placeholder(R.drawable.no_image).into(photoIv)
                userId.text = image.user
                tagsId.text = image.tags
            }
        }

    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            with(holder) {
                bind(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


}