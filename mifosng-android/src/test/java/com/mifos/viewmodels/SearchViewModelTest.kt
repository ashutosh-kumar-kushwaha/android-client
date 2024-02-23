package com.mifos.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mifos.mifosxdroid.util.RxSchedulersOverrideRule
import com.mifos.objects.SearchedEntity
import com.mifos.mifosxdroid.online.search.SearchRepository
import com.mifos.mifosxdroid.online.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import rx.Observable

/**
 * Created by Aditya Gupta on 02/09/23.
 */
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var searchRepository: SearchRepository

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var searchedEntities: List<SearchedEntity>


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(searchRepository)
        Dispatchers.setMain(testDispatcher)
    }


    @Test
    fun testSearchAll_SuccessfulSearchAllReceivedFromRepository_ReturnsSuccess() = runTest {

        Mockito.`when`(
            searchRepository.searchResources(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyBoolean()
            )
        ).thenReturn(
            Observable.just(searchedEntities)
        )
        searchViewModel.searchResources("query", "resources", false)

        searchViewModel.searchUiState.collect {
            assertEquals(it.isLoading, false)
            assertEquals(it.error, null)
            assertEquals(it.searchedEntities, searchedEntities)

        }
    }

    @Test
    fun testSearchAll_UnsuccessfulSearchAllReceivedFromRepository_ReturnsError() = runTest {
        Mockito.`when`(
            searchRepository.searchResources(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyBoolean()
            )
        ).thenReturn(
            Observable.error(RuntimeException("some error message"))
        )
        searchViewModel.searchResources("query", "resources", false)

        searchViewModel.searchUiState.collect {
            assertEquals(it.isLoading, false)
            assertEquals(it.error, "some error message")
            assertEquals(it.searchedEntities, emptyList<SearchedEntity>())
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }
}