package br.ufs.projetos.gocidade.repository.model

import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * Created by samila on 19/11/17.
 */
data class Occurrence (val id : Long, val occurrenceDate : Date, val location : LatLng, val status : String)