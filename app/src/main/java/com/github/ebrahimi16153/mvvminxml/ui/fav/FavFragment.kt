package com.github.ebrahimi16153.mvvminxml.ui.fav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ebrahimi16153.mvvminxml.R
import com.github.ebrahimi16153.mvvminxml.data.adapter.FoodAdapter
import com.github.ebrahimi16153.mvvminxml.databinding.FragmentFavBinding
import com.github.ebrahimi16153.mvvminxml.util.setUpRecycler
import com.github.ebrahimi16153.mvvminxml.viewmodel.FavViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FavFragment : Fragment() {


    //binding
    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding

    //adapter
    @Inject
    lateinit var favAdapter: FoodAdapter

    //viewModel
    private val favViewModel: FavViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //call
        favViewModel.getFavList()


        binding?.apply {


            favViewModel.favoriteMeals.observe(viewLifecycleOwner) { itMeals ->


                favAdapter.setData(data = itMeals)

                favList.setUpRecycler(
                    layoutManager = LinearLayoutManager(requireContext()), adapter = favAdapter
                )

                favAdapter.seOnItemClickListener { itMeal ->
                    val direction = FavFragmentDirections.actionFavFragmentToDetailFragment(itMeal.idMeal)
                    findNavController().navigate(direction)
                }

            }

        }
    }


}