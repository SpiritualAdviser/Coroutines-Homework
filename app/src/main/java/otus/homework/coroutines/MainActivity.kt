package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.CatsViewModel

class MainActivity : AppCompatActivity() {

    private val catsViewModel: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.presenter = catsViewModel
        onFactStateSuccess(view)
        onFactStateError(view)
        onImageUrlFactSuccess(view)
        catsViewModel.onInitComplete()
    }

    private fun onFactStateSuccess(view: CatsView) {
        lifecycleScope.launch {
            catsViewModel.factState.collect { result ->
                view.populate(result.fact)
            }
        }
    }

    private fun onImageUrlFactSuccess(view: CatsView) {
        lifecycleScope.launch {
            catsViewModel.imageCatUrl.collect { url ->
                if (url.isNotEmpty()) {
                    view.setImageOnFact(url)
                }
            }
        }
    }

    private fun onFactStateError(view: CatsView) {
        lifecycleScope.launch {
            catsViewModel.factErrorState.collect { error ->
                view.showToast(error.message)
            }
        }
    }

    override fun onStop() {
        lifecycleScope.cancel()
        super.onStop()
    }
}