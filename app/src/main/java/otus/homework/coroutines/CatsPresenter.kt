package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
     val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var _catsView: ICatsView? = null

    fun onInitComplete() {

        presenterScope.launch {
            try {
                val response = catsService.getCatFact()

                if (response.isSuccessful) {
                    response.body()?.let { fact ->
                        _catsView?.populate(fact)
                    }
                }

            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> _catsView?.showToast("Не удалось получить ответ от сервером")
                    else -> {
                        CrashMonitor.trackWarning(e)
                        e.message?.let { message ->
                            _catsView?.showToast(message)
                        }
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}