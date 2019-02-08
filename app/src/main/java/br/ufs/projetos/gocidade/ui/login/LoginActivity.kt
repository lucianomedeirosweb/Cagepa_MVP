package br.ufs.projetos.gocidade.ui.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import br.ufs.projetos.gocidade.R
import br.ufs.projetos.gocidade.ui.main.MainActivity
import br.ufs.projetos.gocidade.ui.main.MapActivity
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity(), LoginContract.View{

    var callbackManager: CallbackManager
    lateinit var mLoginPresenter : LoginContract.Presenter

    init {
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_login)

        mLoginPresenter = LoginPresenter (this)

        mLoginPresenter.signInWithFacebook(callbackManager)

        btn_login.setOnClickListener {

            if ((edit_email != null) && (edit_password != null) &&
                    (edit_password.text.toString() == "") && edit_email.text.toString() == "") {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Preencha todos os campos!")
                builder.setCancelable(true).create().show()
            } else {
                mLoginPresenter.signInWithEmail(edit_email.text.toString(),
                        edit_password.text.toString())
            }
            }

        txt_signup.setOnClickListener { startActivity(Intent (this, SignUpActivity ::class.java)) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

    override fun showProgressBar(active: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess(message : String) {
        Toast.makeText(this, "Login Realizado com Sucesso!!!", Toast.LENGTH_SHORT).show ()
    }

    override fun onError(message : String) {
        Toast.makeText(this, "Ocorreu um erro. Tente Novamente! $message", Toast.LENGTH_SHORT).show ()
    }

    override fun redirectTo(destinationClass: Class<*>) {
        startActivity(Intent(this, destinationClass))
    }
}
