package com.example.simple.coroutinesandroidexp

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log

import kotlinx.android.synthetic.main.activity_coroutine_exception_handler.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//https://proandroiddev.com/coroutines-for-android-6f9b9f966056
class ActivityCoroutineExceptionHandler : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("Exception", ":" + throwable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_exception_handler)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        job = Job()

        test_01()
        test_02()
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun test_01() {
        launch(handler) {
            //Working on UI thread
            print(Thread.currentThread().name)
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                //Working on background thread
                10 + 10
            }
            //Working on UI thread
            print(deferred.await())
        }
    }

    private fun test_02() {
        launch(handler) {
            val job1 = async(Dispatchers.Default, CoroutineStart.LAZY) {
                //Working on background thread
                10
            }

            val job2 = async(Dispatchers.Default, CoroutineStart.LAZY) {
                //Working on background thread
                20
            }

            //Working on UI thread
            Log.v("DATA", "Output" + job1.await() + job2.await())
        }
    }

    private fun test_03() {
        launch(handler) {
            val result = getFromCallback()
            Log.v("Res", "" + result)
        }
    }

    private suspend fun getFromCallback() = suspendCoroutine<Int> {

        Handler().postDelayed(object : Runnable {
            override fun run() {
                //Return result directly
                it.resume(15)

                //or, return result which is wrapped by Result success
                it.resumeWith(Result.success(15))

                //or, return result which is wrapped by Result failure
                it.resumeWith(Result.failure(AssertionError()))
            }
        }, 2000)
    }
}
