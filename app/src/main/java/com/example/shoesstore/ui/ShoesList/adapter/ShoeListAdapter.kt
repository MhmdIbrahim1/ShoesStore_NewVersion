package com.example.shoesstore.ui.ShoesList.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoesstore.R
import com.example.shoesstore.databinding.ListViewBinding
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.ui.ShoesList.ShoeListFragmentDirections
import com.example.shoesstore.viewmodels.DataViewModel

class ShoeListAdapter : RecyclerView.Adapter<ShoeListAdapter.ShoeViewHolder>() {
    // The list of shoe items
    var shoeList = emptyList<ShoeListData>()
    // Listener for delete button click events
    private var onDeleteClickListener: OnDeleteClickListener? = null
    // View model for interacting with data
    lateinit var viewModel: DataViewModel

    // Interface for delete button click events
    interface OnDeleteClickListener {
        fun onDeleteClick(deletedItem: ShoeListData)
    }

    // Function to set the data and update the list with DiffUtil
    fun setData(shoeList: List<ShoeListData>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = this@ShoeListAdapter.shoeList.size

            override fun getNewListSize(): Int = shoeList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = this@ShoeListAdapter.shoeList[oldItemPosition]
                val newItem = shoeList[newItemPosition]
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = this@ShoeListAdapter.shoeList[oldItemPosition]
                val newItem = shoeList[newItemPosition]
                return oldItem == newItem
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.shoeList = shoeList
        diffResult.dispatchUpdatesTo(this)
    }

    // Function to set the onDeleteClickListener
    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    // Create a ViewHolder for the shoe item view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        val binding = ListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoeViewHolder(binding)
    }

    // Bind the data to the views within the ViewHolder
    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        val currentItem = shoeList[position]
        holder.bind(currentItem)

        // Update the UI based on the favorite state
        val favoriteIconRes = if (currentItem.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_bordered
        holder.binding.favButton.setImageResource(favoriteIconRes)
    }

    // Return the total number of shoe items in the list
    override fun getItemCount(): Int {
        return shoeList.size
    }

    // ViewHolder class for a single shoe item view
    inner class ShoeViewHolder(val binding: ListViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Click listener for the delete button
            binding.deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val deletedItem = shoeList[position]
                    // Show confirmation dialog
                    showConfirmationDialog(deletedItem)
                }
            }

            // Click listener for the favorite button
            binding.favButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val shoe = shoeList[position]
                    shoe.isFavorite = !shoe.isFavorite // Toggle the favorite state

                    // Update the UI based on the favorite state
                    val favoriteIconRes = if (shoe.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_bordered
                    binding.favButton.setImageResource(favoriteIconRes)

                    // Update the favorite state in the database
                    viewModel.updateShoe(shoe)
                }
            }

            // Click listener for the shoe item view
//            binding.root.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    // Remove the shoe argument from the action method
//                    val action = ShoeListFragmentDirections.actionShoeListFragmentToShoeInfoFragment()
//                    it.findNavController().navigate(action)
//                }
//            }
        }

        // Bind the shoe item data to the views
        fun bind(shoe: ShoeListData) {
            binding.shoeData = shoe
            Glide.with(binding.root.context)
                .load(shoe.shoeImageUri ?: R.drawable.shoe_1)
                .into(binding.shoeImage)
        }

        // Show a confirmation dialog for deleting a shoe item
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
