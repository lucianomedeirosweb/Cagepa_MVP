package br.ufs.projetos.gocidade.repository

import br.ufs.projetos.gocidade.repository.model.AppDbHelper
import br.ufs.projetos.gocidade.repository.model.Post
import com.facebook.CallbackManager
import rx.Observable

/**
 * Created by samila on 14/11/17.
 */
class AppDataManager : DataManager {

    val mApiHelper = AppApiHelper ()
    val mDbHelper = AppDbHelper ()

    override fun signInWithFacebook(callbackManager: CallbackManager, listener : DataManager.DataResult)  {
         mApiHelper.signInWithFacebook(callbackManager, listener)
    }

    override fun signInWithEmailAndPassword(email: String, password: String, result : DataManager.DataResult)  {
        mApiHelper.signInWithEmailAndPassword(email, password, result)
    }

    override fun signUpWithEmailAndPassword(email: String, password: String, result : DataManager.DataResult)  {
         mApiHelper.signUpWithEmailAndPassword(email, password, result)
    }

    override fun isSigned(): Boolean {
        return mApiHelper.isSigned()
    }

    override fun newPost(post : Post) {
        mDbHelper.newPost(post)
    }
}