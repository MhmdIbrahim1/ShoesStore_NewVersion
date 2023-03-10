package com.example.shoesstore.ui.ShoesList

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.R
import com.example.shoesstore.databinding.FragmentShoeListBinding
import com.example.shoesstore.util.SwipeToDelete
import com.example.shoesstore.util.hideKeyboard
import com.example.shoesstore.util.observeOnce
import com.example.shoesstore.viewmodels.DataViewModel
import com.google.android.material.snackbar.Snackbar


class ShoeListFragment : Fragment(), SearchView.OnQueryTextListener {
    private  var _binding: FragmentShoeListBinding? = null
    private val binding get() = _binding!!
    private  val mDataViewModel: DataViewModel by viewModels()
    private val adapter: ShoeListAdapter  by lazy { ShoeListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
       _binding = FragmentShoeListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.detailFragment -> {
                    // Handle add menu item click here
                    findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailsFragment())
                    true
                }
                // Handle other menu items here if necessary
                R.id.listFragment -> {
                    // Handle add menu item click here
                    findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentSelf2())
                    true
                }
                else -> false
            }
        }
        // Observe LiveData
        mDataViewModel.getAllData.observe(viewLifecycleOwner) { data ->
            mDataViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
            binding.shoeListRecyclerView.scheduleLayoutAnimation()
        }
     // Hide soft keyboard
     hideKeyboard(requireActivity())
        setUpRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.logout_menu, menu)

                val searchItem = menu.findItem(R.id.search)
                val searchView = searchItem.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ShoeListFragment)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.logout -> {
                        findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToLoginFragment())
                        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
                    }

                    R.id.delete_all -> {
                        deleteAllShoes()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun setUpRecyclerView() {
       val recyclerView = binding.shoeListRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.shoeListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        // Swipe to Delete
        swipeToDelete(recyclerView)
    }


    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.shoeList[viewHolder.adapterPosition]
                // Delete Item
                mDataViewModel.deleteShoe(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun restoreDeletedData(view: View, deletedItem: ShoeListData) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.shoeName}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            mDataViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    private fun deleteAllShoes() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mDataViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully removed everything!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchThroughDatabase(newText)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    //search
    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mDataViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner) { list ->
            list?.let {
                Log.d("ListFragment", "searchThroughDatabase")
                adapter.setData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
