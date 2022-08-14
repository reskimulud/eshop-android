package com.mankart.eshop.favorite.di

import android.content.Context
import com.mankart.eshop.core.di.dfm.FavoriteModuleDependencies
import com.mankart.eshop.favorite.ui.fragment.FavoriteProductFragment
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [FavoriteModuleDependencies::class])
interface FavoriteComponent {
    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun componentDependencies(favoriteModuleDependencies: FavoriteModuleDependencies): Builder
        fun build(): FavoriteComponent
    }

    fun inject(fragment: FavoriteProductFragment)
}