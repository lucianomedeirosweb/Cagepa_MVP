package br.ufs.projetos.gocidade.util

/**
 * Created by samila on 19/11/17.
 */
enum class UserType(val id : Int){
    REGULAR(0), AUTHORITY(1);

    override fun toString(): String {
        return "$name"
    }
}