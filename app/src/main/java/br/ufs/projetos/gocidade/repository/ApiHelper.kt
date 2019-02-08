package br.ufs.projetos.gocidade.repository

import com.facebook.CallbackManager
import rx.Observable

/**
 * Created by samila on 14/11/17.
 */
interface ApiHelper {

    fun signInWithFacebook (callbackManager: CallbackManager, listener : DataManager.DataResult)
    fun signInWithEmailAndPassword (email : String, password:String, result : DataManager.DataResult)
    fun signUpWithEmailAndPassword (email : String, password:String, result : DataManager.DataResult)
    fun isSigned () : Boolean
}