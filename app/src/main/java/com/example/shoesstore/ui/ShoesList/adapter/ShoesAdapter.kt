package com.example.shoesstore.ui.ShoesList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoesstore.databinding.ListViewBinding
import com.example.shoesstore.databinding.ProductRvItemBinding
import com.example.shoesstore.model.Products

class ShoesAdapter: RecyclerView.Adapter<ShoesAdapter.ShoeViewHolder>() {
    class ShoeViewHolder(private val binding: ProductRvItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Products) {
            binding.apply {
                shoeImage.load(product.images.getOrNull(0)) {
                    crossfade(600)
                    error(com.example.shoesstore.R.drawable.error_placeholder)
                }
                shoeName.text = product.shoeName
                shoeCompany.text = product.shoeCompany
                showSize.text = product.shoeSize
                shoeDescription.text = product.shoeDescription
                shoePrice.text = "E£ ${product.price}"
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        return ShoeViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

}