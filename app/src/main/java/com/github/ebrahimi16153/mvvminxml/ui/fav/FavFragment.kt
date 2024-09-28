package com.github.ebrahimi16153.mvvminxml.ui.fav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ebrahimi16153.mvvminxml.R
import com.github.ebrahimi16153.mvvminxml.databinding.FragmentFavBinding


class FavFragment : Fragment() {


    private lateinit var binding: FragmentFavBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }
}