package com.imaginato.homeworkmvvm.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.imaginato.homeworkmvvm.data.remote.login.LoginRepository
import com.imaginato.homeworkmvvm.data.remote.login.request.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.KoinApiExtension
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class, KoinApiExtension::class)
class LoginActivityViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: LoginRepository

    @Mock
    private lateinit var mockProgressObserver: Observer<Boolean>

    @Mock
    private lateinit var mockResultLiveDataObserver: Observer<String>

    private lateinit var viewModel: LoginActivityViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val testScope = TestScope()
    private val expectedFlowResult = flowOf("your password is incorrect.")
    private val expectedFlowResultForSuccess = flowOf("Sukses.")

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginActivityViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_check_username_is_empty() {
        viewModel.loginValidation.observeForever {
            assert(it == LoginValidation.IS_USERNAME_BLANK)
        }
        viewModel.checkLoginValidation("", "")
    }

    @Test
    fun test_check_password_is_empty() {
        viewModel.loginValidation.observeForever {
            assert(it == LoginValidation.IS_PASSWORD_BLANK)
        }
        viewModel.checkLoginValidation("admin", "")
    }

    @Test
    fun test_check_login_input_valid() {
        viewModel.loginValidation.observeForever {
            assert(it == LoginValidation.VALID_INPUT)
        }
        viewModel.checkLoginValidation("admin", "123456")
    }

    @Test
    fun test_login_success() = testScope.runTest {
        Mockito.`when`(repository.callLogin(LoginRequest("admin", "1111111")))
            .thenReturn(expectedFlowResultForSuccess)

        viewModel.isInProgress.observeForever(mockProgressObserver)
        viewModel.loginResultData.observeForever(mockResultLiveDataObserver)

        viewModel.callLogin("admin", "1111111")

        Mockito.verify(mockProgressObserver)
            .onChanged(true)
        Mockito.verify(mockResultLiveDataObserver)
            .onChanged("Sukses.")
        Mockito.verify(mockProgressObserver)
            .onChanged(false)
    }


    @Test
    fun test_login_failed() = testScope.runTest {
        Mockito.`when`(repository.callLogin(LoginRequest("admin", "123456")))
            .thenReturn(expectedFlowResult)
        viewModel.isInProgress.observeForever(mockProgressObserver)
        viewModel.loginResultData.observeForever(mockResultLiveDataObserver)

        viewModel.callLogin("admin", "123456")

        Mockito.verify(mockProgressObserver)
            .onChanged(true)
        Mockito.verify(mockResultLiveDataObserver)
            .onChanged("your password is incorrect.")
        Mockito.verify(mockProgressObserver)
            .onChanged(false)
    }
}