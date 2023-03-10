package com.example.shoesstore.ui.ShoesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoesstore.R
import com.example.shoesstore.databinding.ListViewBinding
import com.example.shoesstore.model.ShoeListData

class ShoeListAdapter: RecyclerView.Adapter<ShoeListAdapter.ShoeViewHolder>() {
    private var shoeList = emptyList<ShoeListData>()

    class ShoeViewHolder(private val binding:ListViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(shoe: ShoeListData) {
            binding.shoeData =shoe
            binding.executePendingBindings()
    }
        companion object{
            fun from(parent: ViewGroup): ShoeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListViewBinding.inflate(layoutInflater, parent, false)
                return ShoeViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        return ShoeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        val currentItem = shoeList[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return shoeList.size
    }

    fun setData(shoe: List<ShoeListData>) {
        this.shoeList = shoe
        notifyDataSetChanged()
    }

}
