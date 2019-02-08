package br.ufs.projetos.gocidade.repository.model

import br.ufs.projetos.gocidade.util.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by samila on 21/11/17.
 */
class  AppDbHelper : DbHelper  {

    val database = FirebaseDatabase.getInstance()
    val dbReference : DatabaseReference? = database.reference

    override fun newPost(post : Post) {
        dbReference?.child(Constants.DataBase.DATABASE_POST)
                ?.child(post.id.toString())?.setValue(post)
    }
}