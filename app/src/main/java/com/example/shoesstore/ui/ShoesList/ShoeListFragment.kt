package com.example.shoesstore.ui.ShoesList

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.R
import com.example.shoesstore.databinding.FragmentShoeListBinding
import com.example.shoesstore.util.hideKeyboard
import com.example.shoesstore.viewmodels.DataViewModel


class ShoeListFragment : Fragment() {
    private  var _binding: FragmentShoeListBinding? = null
    private val binding get() = _binding!!

 override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentShoeListBinding.inflate(inflater,container,false)
        val viewModel = ViewModelProvider(requireActivity())[DataViewModel::class.java]

        viewModel.dataShoeList.observe(viewLifecycleOwner) { item ->
            shoeListView(item)
        }

     binding.lifecycleOwner = viewLifecycleOwner
     binding.addBtn.setOnClickListener{
        view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_shoeListFragment_to_shoeDetailsFragment) }

    }

     // Hide soft keyboard
     hideKeyboard(requireActivity())

     // Initialize RecyclerView
        binding.shoeListRecyclerView.adapter = ShoeListAdapter()

     // Set the adapter to the RecyclerView
        val adapter = ShoeListAdapter()
        binding.shoeListRecyclerView.adapter = adapter

     // Set the layout manager to the RecyclerView grid
        binding.shoeListRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

     // Add a divider between items
        binding.shoeListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )


     return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.logout_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToLoginFragment())
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

//create the shoe list view
private fun shoeListView(item: List<ShoeListData>) {
    val adapter = binding.shoeListRecyclerView.adapter as ShoeListAdapter
    adapter.setData(item)
}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
