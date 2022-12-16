package com.example.pixbayphotos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.pixbayphotos.R
import com.example.pixbayphotos.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val args: DetailsFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            likesTv.text = args.image.likes.toString()
            commentsTv.text = args.image.comments.toString()
            userNameTv.text = args.image.user
            downloadsTv.text = args.image.downloads.toString()
            Picasso.get().load(args.image.largeImageURL).error(R.drawable.no_image).placeholder(R.drawable.no_image).into(photoIv)
            tagsTv.text = args.image.tags
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}