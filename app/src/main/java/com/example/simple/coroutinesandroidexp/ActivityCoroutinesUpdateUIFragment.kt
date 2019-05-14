package com.example.simple.coroutinesandroidexp

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.*

/**
 * A placeholder fragment containing a simple view.
https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md
https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
 */
class ActivityCoroutinesUpdateUIFragment : Fragment() {

    val uiDispatcher: CoroutineDispatcher = Dispatchers.Main
    val bgDispatcher: CoroutineDispatcher = Dispatchers.IO

    val uiScope = CoroutineScope(uiDispatcher)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_activity_coroutines_update_ui, container, false)
        val view = inflate.findViewById<TextView>(R.id.textView2_)

        if (true) {
            uiScope.launch(uiDispatcher) {
                repeat(5) {
                    view.text = "updating text: $it"
                    delay(1000)
                }
                val job = async(bgDispatcher) {
                    delay(1000 * 4)
                    "all IO done"
                }
//                val result = job.await()
//                view.text = result
                view.text = "can't wait"
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                repeat(5) {
                    view.text = "updating text: $it"
                    delay(200)
                }
            }
        }

        return inflate
    }
}
