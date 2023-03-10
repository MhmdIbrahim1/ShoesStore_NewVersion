package com.example.shoesstore.ui.ShoesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoesstore.R
import com.example.shoesstore.model.ShoeListData

class ShoeListAdapter: RecyclerView.Adapter<ShoeListAdapter.ShoeViewHolder>() {
    private var shoeList = emptyList<ShoeListData>()

    class ShoeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val shoeName: TextView = itemView.findViewById(R.id.shoeName)
        val shoeCompany: TextView = itemView.findViewById(R.id.shoeCompany)
        val shoeSize: TextView = itemView.findViewById(R.id.showSize)
        val shoeDescription: TextView = itemView.findViewById(R.id.shoeDescription)
        val shoePrice: TextView = itemView.findViewById(R.id.shoePrice)
        val shoeImage: ImageView = itemView.findViewById(R.id.imageView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        return ShoeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_view, parent, false))
    }

    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        val currentItem = shoeList[position]
        holder.shoeName.text = currentItem.shoeName
        holder.shoeCompany.text = currentItem.shoeCompany
        holder.shoeSize.text = currentItem.shoeSize
        holder.shoeDescription.text = currentItem.shoeDescription
        holder.shoePrice.text = currentItem.shoePrice
        holder.shoeImage.setImageResource(currentItem.images.random())
    }

    override fun getItemCount(): Int {
        return shoeList.size
    }

    fun setData(shoe: List<ShoeListData>) {
        this.shoeList = shoe
        notifyDataSetChanged()
    }

}
