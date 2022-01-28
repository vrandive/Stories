package com.example.stories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import jp.shts.android.storiesprogressview.StoriesProgressView
import jp.shts.android.storiesprogressview.StoriesProgressView.StoriesListener


class MainActivity : AppCompatActivity(), StoriesListener {
   
    // on below line we are creating variable for
    // our press time and time limit to display a story.
    var pressTime = 0L
    var limit = 500L

    // on below line we are creating variables for
    // our progress bar view and image view .
    private var storiesProgressView: StoriesProgressView? = null
    private var image: VideoView? = null

    // on below line we are creating a counter
    // for keeping count of our stories.
    private var counter = 0

    // on below line we are creating a new method for adding touch listener
    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { v, event -> // inside on touch method we are
        // getting action on below line.
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                // on action down when we press our screen
                // the story will pause for specific time.
                pressTime = System.currentTimeMillis()

                // on below line we are pausing our indicator.
                storiesProgressView!!.pause()

                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {

                // in action up case when user do not touches
                // screen this method will skip to next image.
                val now = System.currentTimeMillis()

                // on below line we are resuming our progress bar for status.

                storiesProgressView!!.resume()


                // on below line we are returning if the limit < now - presstime
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inside in create method below line is use to make a full screen.
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        // on below line we are initializing our variables.
        storiesProgressView = findViewById<View>(R.id.stories) as StoriesProgressView

        // on below line we are setting the total count for our stories.
        storiesProgressView!!.setStoriesCount(5)

        // on below line we are setting story duration for each story.
        storiesProgressView!!.setStoryDuration(20000L)

        // on below line we are calling a method for set
        // on story listener and passing context to it.
        storiesProgressView!!.setStoriesListener(this)

        // below line is use to start stories progress bar.
        storiesProgressView!!.startStories(counter)

        // initializing our image view.
        image = findViewById<View>(R.id.image) as VideoView

        // on below line we are setting image to our image view.
        image!!.setVideoPath("android.resource://" + packageName + "/" + R.raw.dzrv)
        image!!.setZOrderOnTop(true)
        image!!.start()

        // below is the view for going to the previous story.
        // initializing our previous view.
        val reverse = findViewById<View>(R.id.reverse)

        // adding on click listener for our reverse view.
        reverse.setOnClickListener {
            // inside on click we are
            // reversing our progress view.
            storiesProgressView!!.reverse()
        }

        // on below line we are calling a set on touch
        // listener method to move towards previous image.
        reverse.setOnTouchListener(onTouchListener)

        // on below line we are initializing
        // view to skip a specific story.
        val skip = findViewById<View>(R.id.skip)
        skip.setOnClickListener {
            // inside on click we are
            // skipping the story progress view.
            storiesProgressView!!.skip()
        }
        // on below line we are calling a set on touch
        // listener method to move to next story.
        skip.setOnTouchListener(onTouchListener)
    }

    override fun onNext() {
        // this method is called when we move
        // to next progress view of story.
        when (counter) {
            0 -> {
                image!!.seekTo(20000)
                counter++
            }
            1 -> {
                image!!.seekTo(30000)
                counter++
            }
            2 -> {
                image!!.seekTo(45000)
                counter++
            }
            3 -> {
                image!!.seekTo(60000)
                counter++
            }
            4->{
                image!!.seekTo(93000)
            }

            else -> {
                null
            }
        }

    }

    override fun onPrev() {

        // this method id called when we move to previous story.
        // on below line we are decreasing our counter
        when (counter) {
            0 -> {
                image!!.seekTo(0)
            }
            1 -> {

                image!!.seekTo(0)
                --counter
            }
            2 -> {
                image!!.seekTo(20000)
                --counter
            }
            3 -> {
                image!!.seekTo(30000)
                --counter
            }
            4->{
                image!!.seekTo(60000)
                --counter
            }

            else -> {
                null
            }
        }
    }

    override fun onComplete() {
        // when the stories are completed this method is called.
        // in this method we are moving back to initial main activity.

        image!!.seekTo(93500)
        image!!.postDelayed(Runnable {
            kotlin.run {
                image!!.pause()
            }
        }, 2000)

    }

    override fun onDestroy() {
        // in on destroy method we are destroying
        // our stories progress view.
        storiesProgressView!!.destroy()
        super.onDestroy()
    }
}