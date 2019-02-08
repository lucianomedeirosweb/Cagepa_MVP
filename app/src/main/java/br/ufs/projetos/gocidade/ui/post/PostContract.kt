package br.ufs.projetos.gocidade.ui.post

import br.ufs.projetos.gocidade.repository.model.Post

/**
 * Created by samila on 21/11/17.
 */
interface PostContract {

    interface View {
        fun onSuccess ()
        fun onError ()
    }

    interface Presenter {
        fun newPost (post : Post)
    }
}