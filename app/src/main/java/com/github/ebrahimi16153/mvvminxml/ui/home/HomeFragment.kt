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
import com.github.ebrahimi16153.mvvminxml.data.adapter.FoodAdapter
import com.github.ebrahimi16153.mvvminxml.R
import com.github.ebrahimi16153.mvvminxml.data.adapter.CategoryAdapter
import com.github.ebrahimi16153.mvvminxml.databinding.FragmentHomeBinding
import com.github.ebrahimi16153.mvvminxml.util.Wrapper
import com.github.ebrahimi16153.mvvminxml.util.setUpRecycler
import com.github.ebrahimi16153.mvvminxml.util.setUpSpinner
import com.github.ebrahimi16153.mvvminxml.viewmodel.HomeViewModel
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

    private var errorMessage: String? = null


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
        letters.add(0,'_')


        binding?.apply {

            //randomFood
            homeViewModel.randomFood.observe(viewLifecycleOwner) { itFood ->

                when (itFood) {
                    is Wrapper.Error -> {
                        errorMessage = itFood.message
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
               if (!itText.isNullOrEmpty()){
                   homeViewModel.searchFoods(itText.toString())
               }
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
                        errorMessage = itCategories.message
                    }

                    Wrapper.Idle -> {}

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
                        errorMessage = itFoods.message
                    }

                    Wrapper.Idle -> {}
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

            //Error
            homeViewModel.errorStatus.observe(viewLifecycleOwner) {
                if (homeViewModel.foods.value is Wrapper.Error || homeViewModel.randomFood.value is Wrapper.Error || homeViewModel.categories.value is Wrapper.Error) {


                    homeContent.isVisible = false
                    disconnected.disconnectedIcon.setImageResource(R.drawable.error)
                    disconnected.disconnectedText.text = errorMessage
                    disconnected.refreshBtn.isVisible = true
                    disconnected.refreshBtn.setOnClickListener {
                        homeViewModel.getFoodByFirstLetter("A")
                    }
                    disconnectedLay.isVisible = true

                } else {

                    disconnectedLay.isVisible = false
                    homeContent.isVisible = true
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}