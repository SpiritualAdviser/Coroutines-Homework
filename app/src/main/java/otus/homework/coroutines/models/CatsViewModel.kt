package otus.homework.coroutines.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.server.CatsService
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.server.DiContainer
import otus.homework.coroutines.data.Fact
import otus.homework.coroutines.data.Result
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {
    private val catsService: CatsService = DiContainer().service
    val factErrorState = MutableStateFlow(Result.Error(""))
    val factState = MutableStateFlow(Result.Success(Fact("", 0)))
    val imageCatUrl = MutableStateFlow("")

    private val exceptionHandler = getExceptionHandler()

    private fun getExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->

            when (exception) {
                is SocketTimeoutException -> factErrorState.value =
                    Result.Error("Не удалось получить ответ от сервером")

                else -> {
                    CrashMonitor.trackWarning(exception)
                    factErrorState.value = Result.Error(exception.message.toString())
                }
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            getRandomFact()
            getRandomCatPicture()
        }
    }

    private suspend fun getRandomFact() {
        val response = catsService.getCatFact()
        if (response.isSuccessful) {
            response.body()?.let { isFact ->
                factState.value = Result.Success(isFact)
            }
        }
    }

    private suspend fun getRandomCatPicture() {
        val responseP = catsService.getCatPicture()
        if (responseP.isSuccessful) {
            responseP.body()?.let { listCatImage ->
                listCatImage.firstOrNull()?.let {
                    imageCatUrl.value = it.url
                }
            }
        }
    }

}
