package br.ufs.projetos.gocidade.repository

import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import rx.Observable

/**
 * Created by samila on 14/11/17.
 */
class AppApiHelper : ApiHelper {


    val mAuth = FirebaseAuth.getInstance()

    override fun signInWithFacebook(callbackManager: CallbackManager, listener : DataManager.DataResult) {


        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onError(error: FacebookException?) {

                Log.i ("GCityLog", "Error" )
                listener.onResult(false, error.toString())
            }

            override fun onCancel() {
                Log.i ("GCityLog", "Cancelled" )
            }

            override fun onSuccess(result: LoginResult?) {
                Log.i ("GCityLog", "Success ${result.toString()}" )
                listener.onResult(true, result.toString())
            }
        })



    }

    override fun signInWithEmailAndPassword(email: String, password: String, result : DataManager.DataResult) {
       mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
           task ->
           if (task.isSuccessful) result.onResult(true, task.toString())
           else result.onResult(false, task.toString())
       }
    }

    override fun signUpWithEmailAndPassword(email: String, password: String, result : DataManager.DataResult) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            if (task.isSuccessful) result.onResult(true, task.toString())
            else result.onResult(false, task.toString())
        }
    }


    override fun isSigned(): Boolean {
         return mAuth.currentUser != null
    }
}