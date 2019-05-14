package com.example.simple.coroutinesandroidexp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
//import okhttp3.OkHttpClient
//import okhttp3.Request
import java.io.IOException

/*
https://sourcediving.com/kotlin-coroutines-in-android-e2d5bb02c275
https://proandroiddev.com/coroutines-for-android-6f9b9f966056
https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
 */
class MainActivity : AppCompatActivity() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
//    var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            //doSomeOperation()
            startActivity(Intent(this, MainActivityCoroutineScope::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun doSomeOperation() {
        var str = Thread.currentThread().name + "_01"

        uiScope.launch {
            //Working on UI thread
            print(str)
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                //Working on background thread
                10 + 10
                str = Thread.currentThread().name + "_02"
            }
            //Working on UI thread
            deferred.await()
            print(str)
        }

//        GlobalScope.launch {
//            repeat(1000) { i ->
//                println("I'm sleeping $i ...")
//                delay(500L)
//            }
//        }
//        delay(1300L) // just quit after delay
    }

    private fun print(str: String) {
        val tv = findViewById<TextView>(R.id.tv_main)
        tv.text = str
    }

//    @Throws(IOException::class)
//    fun run(url: String): String? {
//        val request = Request.Builder()
//                .url(url)
//                .build()
//        client.newCall(request).execute().use { response -> return response.body()?.string() }
//    }
}
