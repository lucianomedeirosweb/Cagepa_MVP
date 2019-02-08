package br.ufs.projetos.gocidade.repository

import br.ufs.projetos.gocidade.repository.model.DbHelper

/**
 * Created by samila on 14/11/17.
 */
interface DataManager : ApiHelper, DbHelper{
    interface DataResult {
        fun onResult (b : Boolean, s : String)
    }
}