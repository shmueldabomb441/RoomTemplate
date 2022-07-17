package com.anushka.roomapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var bookDao: BookDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.add_time)
        val list = findViewById<ScrollView>(R.id.scrollView)

        val db = Room.databaseBuilder(
            applicationContext,
            BookDatabase::class.java, "book_database"
        ).build()

        bookDao = db.bookDao()
        val from = LayoutInflater.from(this)
        fab.setOnClickListener {
            val id = UUID.randomUUID()
            val book = Book(id.toString(), "", "", "")
            from.inflate(R.layout.list_item, list, true).apply {
                val date = findViewById<TextView>(R.id.date)
                val startTime = findViewById<TextView>(R.id.start)
                val sndTime = findViewById<TextView>(R.id.end)
                val starttimePickerDialog = TimePickerDialog(
                    this@MainActivity,
                    { view, hourOfDay, minute ->
                        val s = "$hourOfDay:$minute"
                        book.start = s
                        sndTime.text = s
                        updateBook(book)
                    },
                    0,
                    0,
                    false
                )
                val endtimePickerDialog = TimePickerDialog(
                    this@MainActivity,
                    { view, hourOfDay, minute ->
                        val s = "$hourOfDay:$minute"
                        startTime.text = s
                        book.end = s
                        updateBook(book)

                    },
                    0,
                    0,
                    false
                )
                val datePickerDialog = DatePickerDialog(this@MainActivity, { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
                    val s = LocalDate.of(i, i1, i2).toString()
                    date.text = s
                    book.date = s
                    updateBook(book)
                }, 0, 0, 0)
                findViewById<Button>(R.id.set_start_time).setOnClickListener {
                    starttimePickerDialog.show()
                }
                findViewById<Button>(R.id.set_end_time).setOnClickListener {
                    endtimePickerDialog.show()
                }
                findViewById<Button>(R.id.set_date).setOnClickListener {
                    datePickerDialog.show()
                }
            }
            addItem(book)
        }
    }

    fun addItem(book: Book) = lifecycleScope.launch(Dispatchers.IO) {
        bookDao.insertBook(book)
    }

    fun updateBook(book: Book) = lifecycleScope.launch(Dispatchers.IO) {
        bookDao.updateBook(book)
    }
}