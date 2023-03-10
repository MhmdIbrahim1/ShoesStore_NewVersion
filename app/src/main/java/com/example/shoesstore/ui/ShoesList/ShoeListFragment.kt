package com.example.shoesstore.ui.ShoesList

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.R
import com.example.shoesstore.databinding.FragmentShoeListBinding
import com.example.shoesstore.util.hideKeyboard
import com.example.shoesstore.viewmodels.DataViewModel


class ShoeListFragment : Fragment() {
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
        binding.lifecycleOwner = this

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
        binding.addBtn.setOnClickListener{
            view.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_shoeListFragment_to_shoeDetailsFragment) }
        }
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
}
//create the shoe list view

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
