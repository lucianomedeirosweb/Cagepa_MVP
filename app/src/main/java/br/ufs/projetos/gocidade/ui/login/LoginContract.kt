package br.ufs.projetos.gocidade.ui.login

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.facebook.CallbackManager
import java.util.*

/**
 * Created by samila on 13/11/17.
 */
interface LoginContract {

    interface View {
       fun showProgressBar (active : Boolean)
        fun onSuccess ( message : String )
        fun onError (message : String)
        fun redirectTo(destinationClass : Class<*>)
    }

    interface Presenter {
        fun signInWithFacebook (callbackManager: CallbackManager)
        fun signInWithEmail(email : String, password : String)
        fun signUpWithEmail(email : String, password : String)
        fun isSigned ()

    }
}