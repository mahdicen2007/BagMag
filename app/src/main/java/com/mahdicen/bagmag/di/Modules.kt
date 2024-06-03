package com.mahdicen.bagmag.di


import android.content.Context
import androidx.room.Room
import com.mahdicen.bagmag.model.db.AppDatabase
import com.mahdicen.bagmag.model.net.createApiService
import com.mahdicen.bagmag.model.repository.cart.CartRepository
import com.mahdicen.bagmag.model.repository.cart.CartRepositoryImpl
import com.mahdicen.bagmag.model.repository.comment.CommentRepository
import com.mahdicen.bagmag.model.repository.comment.CommentRepositoryImpl
import com.mahdicen.bagmag.model.repository.product.ProductRepository
import com.mahdicen.bagmag.model.repository.product.ProductRepositoryImpl
import com.mahdicen.bagmag.model.repository.user.UserRepository
import com.mahdicen.bagmag.model.repository.user.UserRepositoryImpl
import com.mahdicen.bagmag.ui.features.cart.CartViewModel
import com.mahdicen.bagmag.ui.features.category.CategoryViewModel
import com.mahdicen.bagmag.ui.features.main.MainViewModel
import com.mahdicen.bagmag.ui.features.product.ProductViewModel
import com.mahdicen.bagmag.ui.features.profile.ProfileViewModel
import com.mahdicen.bagmag.ui.features.signIn.SignInViewModel
import com.mahdicen.bagmag.ui.features.signUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }
    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java , "AppDatabase.db"
        )
            .build()
    }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ProductRepository> {
        ProductRepositoryImpl(
            get(),
            get<AppDatabase>().productDao()
        )
    }
    single<CommentRepository>{ CommentRepositoryImpl(get()) }
    single<CartRepository>{ CartRepositoryImpl(get() , get()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { (isNetConnected:Boolean) -> MainViewModel(get(), get() , isNetConnected) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductViewModel(get() , get() , get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { CartViewModel(get() , get()) }

}