package com.github.ebrahimi16153.mvvminxml.ui.detail

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.github.ebrahimi16153.mvvminxml.data.model.FoodList
import com.github.ebrahimi16153.mvvminxml.databinding.FragmentDetailBinding
import com.github.ebrahimi16153.mvvminxml.util.Wrapper
import com.github.ebrahimi16153.mvvminxml.viewmodel.DetailViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {


    //args
    private val args: DetailFragmentArgs by navArgs()
    private var foodID: String? = null

    //viewmodel
    private val detailViewModel: DetailViewModel by viewModels()

    //binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        foodID = args.foodID
        //call api

        detailViewModel.detailFood(foodID!!)



        binding?.apply {

            // backBtm
            backBtn.setOnClickListener { findNavController().navigateUp() }


            //fill info
            detailViewModel.detailFood.observe(viewLifecycleOwner) { itFood ->

                when (itFood) {
                    is Wrapper.Error -> {}
                    Wrapper.Idle -> {}
                    Wrapper.Loading -> {}
                    is Wrapper.Success -> {
                        fillContent(itFood.data)
                    }
                }


            }


        }


    }

    private fun fillContent(meal: FoodList.Meal) {

        binding?.apply {

            /////////////coverImage////////////////
            coverImg.load(meal.strMealThumb)

            ///////////Top Icon///////////////////
            categoryDetail.text = meal.strCategory

            areaDetail.text = meal.strArea

            youtube.isVisible = meal.strYoutube != null

            if (meal.strSource != null) {
                sourceDetail.isVisible = true
                sourceDetail.setOnClickListener {
                    val intent = Intent(ACTION_VIEW)
                    intent.setData(Uri.parse(meal.strSource))
                    requireContext().startActivity(intent)
                }
            } else {
                sourceDetail.isVisible = false
            }

            //TODO FAV FUN


            //////////////FoodINFO////////////////////////

            foodTitleTxt.text = meal.strMeal
            foodDescTxt.text = meal.strInstructions

            //////////////ingredients @ measure ////////////////////////////////////////

            ingredientsTxt.text = meal.toIngredient()
            measureTxt.text = meal.toMeasure()

            ///////////////youtube player///////////////////////////////////////
            youtubePlayer.apply {
                viewLifecycleOwner.lifecycle.addObserver(this)


                val youtubeKey = meal.youtubeKey()

                addYouTubePlayerListener(object :AbstractYouTubePlayerListener(){
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youtubeKey?.let { youTubePlayer.cueVideo(it, 0f) }

                    }
                })
            }

        }
    }


    override fun onStop() {
        _binding = null
        super.onStop()
    }

}

