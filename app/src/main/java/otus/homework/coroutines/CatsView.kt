package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {
    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    override fun showToast(e: String) {
        Toast.makeText(this.context, e, Toast.LENGTH_LONG)
            .show()
    }

    override fun setImageOnFact(url: String) {
        Picasso.get().load(url)
            .into(findViewById<ImageView>(R.id.fact_image))
    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun showToast(e: String)
    fun setImageOnFact(url: String)
}