package br.ufs.projetos.gocidade.ui.login

import br.ufs.projetos.gocidade.repository.AppDataManager
import br.ufs.projetos.gocidade.repository.DataManager
import br.ufs.projetos.gocidade.ui.main.MainActivity
import br.ufs.projetos.gocidade.ui.main.MapActivity
import com.facebook.CallbackManager

/**
 * Created by samila on 13/11/17.
 */
class LoginPresenter : LoginContract.Presenter {

    var mView : LoginContract.View
    val mDataManager : DataManager = AppDataManager ()
    lateinit var listener : DataManager.DataResult



    val TAG =  "GCity"

    constructor(mView: LoginContract.View) {
        this.mView = mView
    }

    override fun signUpWithEmail(email: String, password: String) {

        listener = object : DataManager.DataResult {
            override fun onResult(b: Boolean, s: String) {
                if (b){
                    mView.onSuccess(s)
                    mView.redirectTo(LoginActivity :: class.java)
                }else{
                    mView.onError(s)
                }
            }
        }
        mDataManager.signUpWithEmailAndPassword(email, password, listener)
    }

    override fun signInWithFacebook(callbackManager: CallbackManager) {
        listener = object : DataManager.DataResult {
            override fun onResult(b: Boolean, s: String) {
                if (b){
                    mView.onSuccess(s)
                    mView.redirectTo(MainActivity :: class.java)
                }else{
                    mView.onError(s)
                }
            }
        }
        mDataManager.signInWithFacebook(callbackManager, listener)

    }

    override fun signInWithEmail(email: String, password: String) {
        listener = object : DataManager.DataResult {
            override fun onResult(b: Boolean, s: String) {
                if (b){
                    mView.onSuccess(s)
                    mView.redirectTo(MainActivity :: class.java)
                }else{
                    mView.onError(s)
                }
            }
        }
       mDataManager.signInWithEmailAndPassword(email, password, listener)
    }

    override fun isSigned() {
        if (mDataManager.isSigned()){
            mView.redirectTo(MainActivity :: class.java)
        }
    }
}