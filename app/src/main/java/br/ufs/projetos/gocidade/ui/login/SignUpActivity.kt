package br.ufs.projetos.gocidade.ui.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import br.ufs.projetos.gocidade.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), LoginContract.View {



    lateinit var mLoginPresenter : LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mLoginPresenter = LoginPresenter(this)

        btn_signup.setOnClickListener {

            if ((edit_email_signup != null) && (edit_password_signup != null) &&
                    (edit_password_signup.text.toString() == "") && edit_email_signup.text.toString() == "") {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Preencha todos os campos!")
                builder.setCancelable(true).create().show()
            } else {
                mLoginPresenter.signUpWithEmail(edit_email_signup.text.toString(),
                        edit_password_signup.text.toString())
            }
        }
    }

    override fun showProgressBar(active: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess(message : String) {
        Toast.makeText(this, "Usuários Cadastrado com Sucesso!!!", Toast.LENGTH_SHORT).show ()
    }

    override fun onError(message : String) {
        Toast.makeText(this, "Ocorreu um erro. Tente Novamente $message", Toast.LENGTH_SHORT).show ()
    }

    override fun redirectTo(destinationClass: Class<*>) {
        startActivity(Intent(this, destinationClass))
    }
}
