package com.example.pixbayphotos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pixbayphotos.models.Image
import com.example.pixbayphotos.domain.data.ImagesFinderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageFinderViewModel @Inject constructor(
    private val imagesFinderUseCase: ImagesFinderUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {


    private val _uiStatePagedData: MutableStateFlow<PagingData<Image>?> = MutableStateFlow(null)
    val uiStatePagedData = _uiStatePagedData.asStateFlow()

    init {
        getResults(DEFAULT_QUERY)
    }

    companion object {
        const val DEFAULT_QUERY = "fruits"
    }

     fun getResults(query: String) {
         viewModelScope.launch(ioDispatcher) {
             imagesFinderUseCase(query).cachedIn(viewModelScope).collectLatest {
                  _uiStatePagedData.emit(
                      it
                  )
             }
         }
     }

}