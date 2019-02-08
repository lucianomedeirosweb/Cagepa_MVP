package br.ufs.projetos.gocidade.ui.post

import br.ufs.projetos.gocidade.repository.AppDataManager
import br.ufs.projetos.gocidade.repository.model.Post

/**
 * Created by samila on 21/11/17.
 */
class PostPresenter : PostContract.Presenter {

    val mDataManager = AppDataManager ()
    var mView : PostContract.View

    constructor(mView: PostContract.View) {
        this.mView = mView
    }


    override fun newPost(post : Post) {
        mDataManager.newPost(post)
    }
}