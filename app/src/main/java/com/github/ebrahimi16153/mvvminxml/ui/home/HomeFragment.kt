package com.github.ebrahimi16153.mvvminxml.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.github.ebrahimi16153.foodapp.ui.home.adapter.FoodAdapter
import com.github.ebrahimi16153.mvvminxml.data.adapter.CategoryAdapter
import com.github.ebrahimi16153.mvvminxml.databinding.FragmentHomeBinding
import com.github.ebrahimi16153.mvvminxml.util.Wrapper
import com.github.ebrahimi16153.mvvminxml.util.setUpRecycler
import com.github.ebrahimi16153.mvvminxml.util.setUpSpinner
import com.github.ebrahimi16153.mvvminxml.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding


    //viewModel
    private val homeViewModel: HomeViewModel by viewModels()

    //adapter
    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var foodAdapter: FoodAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val letters = listOf('A'..'Z').flatten().toMutableList()


//        check connection


        ////////////////////////////call API//////////////////
        homeViewModel.getRandomFood()
        homeViewModel.getCategory()

        binding?.apply {

             //randomFood
            homeViewModel.randomFood.observe(viewLifecycleOwner) { itFood ->

                when (itFood) {
                    is Wrapper.Error -> {
                        foodsLoading.isVisible = false
                        Snackbar.make(root, itFood.message, Snackbar.LENGTH_LONG).show()
                    }

                    Wrapper.Idle -> {}
                    Wrapper.Loading -> {
                        foodsLoading.isVisible = true
                        homeContent.isVisible = false

                    }

                    is Wrapper.Success -> {
                        headerImg.load(data = itFood.data?.strMealThumb)
                        foodsLoading.isVisible = false
                        homeContent.isVisible = true
                    }
                }

            }


            //////////////////////////////////search//////////////////////////////////////
            searchEdt.addTextChangedListener { itText ->
                homeViewModel.searchFoods(itText.toString())
            }


///////////////////////////Spinner///////////////////////////////////////////////////////

            filterSpinner.setUpSpinner(list = letters, callback = { itString ->

                //call api -> find food by letters
                homeViewModel.getFoodByFirstLetter(itString)
            })


            ///////////////////////category////////////////////////////////////////////////////////


            homeViewModel.categories.observe(viewLifecycleOwner) { itCategories ->

                when (itCategories) {
                    is Wrapper.Error -> {
                        foodsLoading.isVisible = false
                        Snackbar.make(root, itCategories.message, Snackbar.LENGTH_LONG)
                            .show()
                    }

                    Wrapper.Idle -> {

                    }

                    Wrapper.Loading -> {
                        foodsLoading.isVisible = true
                        homeContent.isVisible = false
                    }

                    is Wrapper.Success -> {
                        foodsLoading.isVisible = false

                        categoryAdapter.setData(itCategories.data)

                        categoryRecycler.setUpRecycler(
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            ),
                            adapter = categoryAdapter
                        )

                        categoryAdapter.seOnItemClickListener { itCategory ->
                            homeViewModel.getFoodsByCategory(cat = itCategory.strCategory)

                        }
                    }
                }
            }


            //////////////////////////////////foods/////////////////////////////////////////////////////
            homeViewModel.foods.observe(viewLifecycleOwner) { itFoods ->

                when (itFoods) {
                    is Wrapper.Error -> {
                        foodsLoading.isVisible = false
                        Snackbar.make(root, itFoods.message, Snackbar.LENGTH_LONG).show()
                    }

                    Wrapper.Idle -> {

                    }

                    Wrapper.Loading -> {
                        foodsLoading.isVisible = true
                        homeContent.isVisible = false
                    }

                    is Wrapper.Success -> {
                        foodsLoading.isVisible = false
                        homeContent.isVisible = true


                        foodAdapter.setData(itFoods.data)

                        foodsRecycler.setUpRecycler(
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            ), adapter = foodAdapter
                        )


                        foodAdapter.seOnItemClickListener { itFood ->

                            val direction =
                                HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                                    itFood.idMeal
                                )
                            findNavController().navigate(direction)

                        }

                    }
                }

            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}