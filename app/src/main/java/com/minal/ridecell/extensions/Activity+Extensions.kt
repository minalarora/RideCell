package com.minal.ridecell.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.preference.PreferenceManager


fun Context.createToast(msg: String)
{
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}

fun Context.putData(key: String, value: String?)
{
    PreferenceManager.getDefaultSharedPreferences(this)
        ?.let {
        with(it.edit()){

            putString(key,value)
            apply()
        }
    }
}

fun Context.getData(key: String?): String?
{
    var value: String?= null
    PreferenceManager.getDefaultSharedPreferences(this)?.let {

        value = it.getString(key,null)
    }
    return value
}


fun Activity.hideKeyBoard()
{
    val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
}