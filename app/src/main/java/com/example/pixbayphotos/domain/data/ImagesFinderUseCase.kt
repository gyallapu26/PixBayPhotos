package com.example.pixbayphotos.domain.data

import com.example.pixbayphotos.repository.ImagesFinderRepositoryImpl
import javax.inject.Inject

/**
 * UseCase encapsulate complex business logic and can be reused by mutiple viewmodel
 * Although @see ImagesFinderUseCase might not add much benefit in simple case but
 * when app grows complex it's useful!
 * */

class ImagesFinderUseCase @Inject constructor(private val imagesFinderRepository: ImagesFinderRepositoryImpl) {

    suspend operator fun invoke(query: String) = imagesFinderRepository.getSearchImages(query)
}