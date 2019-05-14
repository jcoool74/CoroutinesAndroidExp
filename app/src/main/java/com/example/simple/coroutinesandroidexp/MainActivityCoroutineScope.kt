package com.example.simple.coroutinesandroidexp

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main_coroutine_scope.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//https://proandroiddev.com/coroutines-for-android-6f9b9f966056
class MainActivityCoroutineScope : AppCompatActivity(), CoroutineScope {
    private val TAG = MainActivityCoroutineScope::class.java.simpleName
    private lateinit var tv: TextView

    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        //Log.e("Exception", ":" + throwable)
        print(tv, throwable.message + "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_coroutine_scope)
        setSupportActionBar(toolbar)

        tv = findViewById<TextView>(R.id.textView)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Clicked", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

//            test_01()

//            test_02()
            test_03()
        }

        mJob = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }

    private fun test_01() {

        launch(handler) {
            print(tv, "here")
            val async = async(Dispatchers.Default) {
                Log.d(TAG, "inside the async")
                delay(1000 * 3)
                //"there yo"
                throw Exception("Exception here")
            }

            if (false) {
                async.await()
                print(tv, "there")
            } else {
                print(tv, async.await())
            }
        }
    }

    private fun print(tv: TextView, any: Any) {
        tv.post {
            tv.text = any.toString()
        }
    }

    private fun test_02() {
        launch(handler) {
            val job01 = async(Dispatchers.Default, CoroutineStart.LAZY) {
                delay(1000)
                "job01"
            }
            val job02 = async(Dispatchers.Default, CoroutineStart.LAZY) {
                delay(2000)
                return@async "job02"
            }

            print(tv, job01.await().toString() + "_" + job02.await())
        }
    }

    private fun test_03() {
        launch(handler) {
            val callback = getFromCallback()
            print(tv, callback)
        }
    }

    private suspend fun getFromCallback() = suspendCoroutine<Int> {

        Handler().postDelayed(object : Runnable {
            override fun run() {
                //Return result directly
                it.resume(15)

                //or, return result which is wrapped by Result success
//                it.resumeWith(Result.success(15))
//
//                //or, return result which is wrapped by Result failure
//                it.resumeWith(Result.failure(AssertionError()))
            }
        }, 1000)
    }
}
