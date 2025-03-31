package com.example.hotel_03.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.hotel_03.R
import com.example.hotel_03.databinding.FragmentHomeBinding
import com.example.hotel_03.retrofit.UserAPI.Companion.BASE_URL

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        popolateLayout()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scrollViewHome.smoothScrollTo(0,0)
    }
    //Riempie la home con le immagini scaricate dal server
    private fun popolateLayout() {

        Glide.with(binding.img1.context)
            .load("$BASE_URL/static/img/img_1.jpg")
            .centerCrop()
            .into(binding.img1)

        Glide.with(binding.img2.context)
            .load("$BASE_URL/static/img/img_2.jpg")
            .centerCrop()
            .into(binding.img2)

        Glide.with(binding.img3.context)
            .load("$BASE_URL/static/img/img_3.jpg")
            .centerCrop()
            .into(binding.img3)

        Glide.with(binding.img4.context)
            .load("$BASE_URL/static/img/img_4.jpg")
            .centerCrop()
            .into(binding.img4)
    }

}