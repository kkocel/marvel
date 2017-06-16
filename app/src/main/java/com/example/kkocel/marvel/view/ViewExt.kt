package com.example.kkocel.marvel.view

import android.app.Activity
import android.support.design.widget.Snackbar

fun Activity.displaySnackbar(message: String) = Snackbar.make(window.decorView, message, Snackbar.LENGTH_LONG).show()

