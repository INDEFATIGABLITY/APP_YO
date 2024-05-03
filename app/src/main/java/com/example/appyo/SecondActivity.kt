package com.example.appyo

import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SecondActivity() : AppCompatActivity(), Parcelable {
    // Initial percentage values
    private var fedPercentage = 50
    private var happyPercentage = 50
    private var cleanPercentage = 50
    private var isPetDead = false

    constructor(parcel: Parcel) : this() {
        fedPercentage = parcel.readInt()
        happyPercentage = parcel.readInt()
        cleanPercentage = parcel.readInt()
        isPetDead = parcel.readByte() != 0.toByte()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)

        val petImageView: ImageView = findViewById(R.id.petImageView)
        val feedButton: Button = findViewById(R.id.feedButton)
        val cleanButton: Button = findViewById(R.id.cleanButton)
        val playButton: Button = findViewById(R.id.playButton)
        val deathImageView: ImageView = findViewById(R.id.deathImageView)

        // Set initial pet image on the second screen
        petImageView.setImageResource(R.drawable.your_pet_image)

        feedButton.setOnClickListener {
            if (!isPetDead) {
                // Update pet's image to match the feeding action icon
                petImageView.setImageResource(R.drawable.feeding_image)

                // Update the pet's status values
                if (fedPercentage < 100) fedPercentage += 5 // Increase fed percentage by 5
                updateStatus()
            }
        }

        cleanButton.setOnClickListener {
            if (!isPetDead) {
                // Update pet's image to match the cleaning action icon
                petImageView.setImageResource(R.drawable.pet_cleaning_image)

                // Update the pet's status values
                if (cleanPercentage < 100) cleanPercentage += 5 // Increase clean percentage by 5
                updateStatus()
            }
        }

        playButton.setOnClickListener {
            if (!isPetDead) {
                // Update pet's image to match the playing action icon
                petImageView.setImageResource(R.drawable.pet_playing_image)

                // Update the pet's status values
                if (happyPercentage < 100) happyPercentage += 5 // Increase happy percentage by 5
                updateStatus()
            }
        }
    }

    private fun updateStatus() {
        // Update progress bars
        findViewById<ProgressBar>(R.id.progressBarFed).progress = fedPercentage
        findViewById<ProgressBar>(R.id.progressBarHappy).progress = happyPercentage
        findViewById<ProgressBar>(R.id.progressBarClean).progress = cleanPercentage

        // Check if any percentage reaches 30%
        if (fedPercentage <= 30 && !isPetDead) {
            petDies()
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(fedPercentage)
        parcel.writeInt(happyPercentage)
        parcel.writeInt(cleanPercentage)
        parcel.writeByte(if (isPetDead) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SecondActivity> {
        override fun createFromParcel(parcel: Parcel): SecondActivity {
            return SecondActivity(parcel)
        }

        override fun newArray(size: Int): Array<SecondActivity?> {
            return arrayOfNulls(size)
        }
    }

    private fun petDies() {
        // Display the dying pet image
        findViewById<ImageView>(R.id.deathImageView).apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.pet_dying_image)
        }
        isPetDead = true

        // Restart game after 1 second
        Handler().postDelayed({
            // Reset all stats to 100 and hide the death image
            fedPercentage = 100
            happyPercentage = 100
            cleanPercentage = 100
            isPetDead = false

            findViewById<ImageView>(R.id.deathImageView).visibility = View.GONE
            findViewById<ProgressBar>(R.id.progressBarFed).progress = fedPercentage
            findViewById<ProgressBar>(R.id.progressBarHappy).progress = happyPercentage
            findViewById<ProgressBar>(R.id.progressBarClean).progress = cleanPercentage
        }, 1000)
    }}


