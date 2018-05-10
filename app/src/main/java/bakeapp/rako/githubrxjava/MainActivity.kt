package bakeapp.rako.githubrxjava

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar

import org.reactivestreams.Subscription

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by rako on 09/05/2018.
 */

class MainActivity : AppCompatActivity() {
    private val adapter = GitHubRepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list_view_repos.adapter = adapter


        val editTextUsername = findViewById<View>(R.id.edit_text_username) as EditText
        val buttonSearch = findViewById<View>(R.id.button_search) as Button
        buttonSearch.setOnClickListener {
            val username = editTextUsername.text.toString()
            if (!TextUtils.isEmpty(username)) {
                progress!!.visibility = View.VISIBLE
                list_view_repos.visibility = View.GONE
                getStarredRepos(username)
            }
        }
    }

    private fun getStarredRepos(username: String) {
        GitHubClient.gitHubService
                .getStarredRepositories(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<List<GitHubRepo>> {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Log.d(TAG, "In onError()")
                    }

                    override fun onComplete() {
                        Log.e(TAG, "In onComplete()")
                    }

                    override fun onSubscribe(d: Disposable) {
                        Log.e(TAG, "onSubscribe")
                    }

                    override fun onNext(gitHubRepos: List<GitHubRepo>) {
                        progress!!.visibility = View.GONE
                        list_view_repos.visibility = View.VISIBLE
                        adapter.setGitHubRepos(gitHubRepos)
                        Log.e(TAG, "onNext")
                    }
                })
    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName
    }
}