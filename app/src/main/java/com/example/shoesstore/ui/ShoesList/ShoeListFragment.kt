package com.example.shoesstore.ui.ShoesList

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.R
import com.example.shoesstore.databinding.FragmentShoeListBinding
import com.example.shoesstore.ui.ShoesList.adapter.ShoeListAdapter
import com.example.shoesstore.util.SwipeToDelete
import com.example.shoesstore.util.hideKeyboard
import com.example.shoesstore.util.observeOnce
import com.example.shoesstore.viewmodels.DataViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShoeListFragment : Fragment(), SearchView.OnQueryTextListener , ShoeListAdapter.OnDeleteClickListener {

    // Binding object instance corresponding to the fragment_shoe_list.xml layout
    private var _binding: FragmentShoeListBinding? = null
    private val binding get() = _binding!!
    override fun onDeleteClick(deletedItem: ShoeListData) {
        mDataViewModel.deleteShoe(deletedItem)
    }


    private val mDataViewModel: DataViewModel by viewModels()
    // Set the adapter on the view model

    private val adapter: ShoeListAdapter by lazy {
        ShoeListAdapter().apply {
            setOnDeleteClickListener(this@ShoeListFragment)
            viewModel = mDataViewModel
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentShoeListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        // Hide the back arrow in the ActionBar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.detailFragment -> {
                    findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailsFragment())
                    true
                }

                R.id.listFragment -> {
                    // Already in the desired fragment, no navigation needed
                    true
                }

                R.id.favFragment -> {
                    findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeInfoFragment())
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

    override fun onResume() {
        super.onResume()
        // Update the selected navBottom icon when the fragment is resumed
        binding.bottomNavigationView.selectedItemId = R.id.listFragment
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set adapter for the ListView
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
