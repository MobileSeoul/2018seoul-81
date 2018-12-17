@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.jhmk.stampme.Module.DataBase

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.User

object PreferencesManager {
    val TAG = this.javaClass.simpleName

    fun saveUserInfo(context: Context, user: User): Boolean {
        Log.d(TAG, "##### saveUserInfo ##### id : ${user.userId}")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        return try {
            editor.putString(ConstVariables.PREF_KEY_USER_ID, user.userId)
            editor.putString(ConstVariables.PREF_KEY_USER_PW, user.userPw)
            editor.apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun loadUserInfo(context: Context): User {
        Log.d(TAG, "##### loadUserInfo #####")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id = sharedPreferences.getString(ConstVariables.PREF_KEY_USER_ID, "")
        val pw = sharedPreferences.getString(ConstVariables.PREF_KEY_USER_PW, "")
        val user = User(id, pw)
        return user
    }

    fun deleteUserInfo(context: Context, user: User): Boolean {
        Log.d(TAG, "##### requestDeleteUserInfo ##### id : ${user.userId}")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        return try {
            editor.remove(ConstVariables.PREF_KEY_USER_ID)
            editor.remove(ConstVariables.PREF_KEY_USER_PW)
            editor.apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}