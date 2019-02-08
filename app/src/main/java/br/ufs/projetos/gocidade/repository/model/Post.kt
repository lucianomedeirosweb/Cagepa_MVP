package br.ufs.projetos.gocidade.repository.model

import br.ufs.projetos.gocidade.util.OccurrenceCategory
import com.google.android.gms.maps.model.LatLng

/**
 * Created by samila on 19/11/17.
 */
data class Post (val id : Long, val uriFoto : String, val category : OccurrenceCategory, val location : LatLng, val comment : String )