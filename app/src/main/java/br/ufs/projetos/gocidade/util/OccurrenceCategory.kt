package br.ufs.projetos.gocidade.util

/**
 * Created by samila on 19/11/17.
 */
enum class OccurrenceCategory (val id : Int){
    HOLE(0), RUBBISH(1),
    LIGHTING(2), LEAKAGE(3),
    PAVEMENT(4), SIGNALING(5),
    OTHERS (6);

    override fun toString(): String {
        return "$name"
    }
}
