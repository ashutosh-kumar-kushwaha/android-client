package com.mifos.mifosxdroid.online.search


import androidx.lifecycle.ViewModel
import com.mifos.objects.SearchedEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Aditya Gupta on 06/08/23.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    private val _searchUiState = MutableStateFlow(SearchUiState())

    val searchUiState: StateFlow<SearchUiState>
        get() = _searchUiState.asStateFlow()

    fun searchResources(query: String?, resources: String?, exactMatch: Boolean?) {
        _searchUiState.value = _searchUiState.value.copy(isLoading = true)
        repository.searchResources(query, resources, exactMatch)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<List<SearchedEntity>>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    _searchUiState.value = _searchUiState.value.copy(isLoading = false, error = e.message)
                }

                override fun onNext(searchedEntities: List<SearchedEntity>) {
                    if (searchedEntities.isEmpty()) {
                        _searchUiState.value = SearchUiState(error = "No Search Result found", searchedEntities = emptyList())
                    } else {
                        _searchUiState.value = SearchUiState(searchedEntities = searchedEntities)
                    }
                }
            })
    }

    fun showError(error: String){
        _searchUiState.value = _searchUiState.value.copy(error = error)
    }

    fun dismissDialog() {
        _searchUiState.value = _searchUiState.value.copy(isLoading = false)
    }

    fun resetErrorMessage(){
        _searchUiState.value = _searchUiState.value.copy(error = null)
    }
}
