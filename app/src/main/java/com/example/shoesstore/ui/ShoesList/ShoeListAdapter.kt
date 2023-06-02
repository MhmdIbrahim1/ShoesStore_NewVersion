package com.example.shoesstore.ui.ShoesList

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoesstore.R
import com.example.shoesstore.databinding.ListViewBinding
import com.example.shoesstore.model.ShoeListData

class ShoeListAdapter : RecyclerView.Adapter<ShoeListAdapter.ShoeViewHolder>() {
    var shoeList = emptyList<ShoeListData>()
    private var onDeleteClickListener: OnDeleteClickListener? = null

    interface OnDeleteClickListener {
        fun onDeleteClick(deletedItem: ShoeListData)
    }

    fun setData(shoeList: List<ShoeListData>) {
        this.shoeList = shoeList
        notifyDataSetChanged()
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        val binding = ListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        val currentItem = shoeList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return shoeList.size
    }

    inner class ShoeViewHolder(private val binding: ListViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val deletedItem = shoeList[position]
                    // Show confirmation dialog
                    showConfirmationDialog(deletedItem)

                }
            }


            binding.favButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val shoe = shoeList[position]
                    shoe.isFavorite = !shoe.isFavorite // Toggle the favorite state

                    // Update the UI based on the favorite state
                    val favoriteIconRes = if (shoe.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_bordered
                    binding.favButton.setImageResource(favoriteIconRes)
                }
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Remove the shoe argument from the action method
                    val action = ShoeListFragmentDirections.actionShoeListFragmentToShoeInfoFragment()
                    it.findNavController().navigate(action)
                }
            }

        }

        fun bind(shoe: ShoeListData) {
            binding.shoeData = shoe
            Glide.with(binding.root.context)
                .load(shoe.shoeImageUri ?: R.drawable.shoe_1)
                .into(binding.shoeImage)
        }

        private fun showConfirmationDialog(deletedItem: ShoeListData) {
            val alertDialog = AlertDialog.Builder(binding.root.context)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to remove this item?")
                .setPositiveButton("Yes") { dialog, _ ->
                    // User confirmed, delete the item
                    onDeleteClickListener?.onDeleteClick(deletedItem)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }
    }


}
