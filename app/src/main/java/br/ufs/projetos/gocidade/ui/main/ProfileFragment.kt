package br.ufs.projetos.gocidade.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import br.ufs.projetos.gocidade.R

/**
 * Created by samila on 16/11/17.
 */
class ProfileFragment : Fragment {

    constructor() : super()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_profile, container, false)

        return layout
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile_menu, menu)
    }
}