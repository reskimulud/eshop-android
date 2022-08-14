package com.mankart.eshop.favorite.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mankart.eshop.favorite.databinding.FragmentFavoriteProductBinding
import com.mankart.eshop.favorite.di.DaggerFavoriteComponent
import com.mankart.eshop.core.di.dfm.FavoriteModuleDependencies
import com.mankart.eshop.favorite.ui.FavoriteViewModel
import com.mankart.eshop.favorite.ui.ViewModelFactory
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavoriteProductFragment: Fragment() {
    private var _binding: FragmentFavoriteProductBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val favoriteViewModel: FavoriteViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        DaggerFavoriteComponent.builder()
            .context(context)
            .componentDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        getFavoriteProducts()
    }

    private fun getFavoriteProducts() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}