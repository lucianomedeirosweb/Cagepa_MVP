package br.ufs.projetos.gocidade.ui.post

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter

import br.ufs.projetos.gocidade.R
import br.ufs.projetos.gocidade.repository.model.Post
import br.ufs.projetos.gocidade.util.OccurrenceCategory
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_posts.*
import java.util.*

class PostActivity : AppCompatActivity(), PostContract.View{

    lateinit var mSelectedCategory : String
    lateinit var mPostPresenter : PostContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        mPostPresenter = PostPresenter(this)
      val mSpinner = spinner_categories
        mSpinner.adapter = ArrayAdapter<String> (this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                resources.getStringArray(R.array.categories))
      //  mSpinner.onItemSelectedListener = this
        mSelectedCategory = mSpinner.selectedItem.toString()
        Log.i("GCity", "Categoria = $mSelectedCategory")
        btn_post_publish.setOnClickListener {
            val post = Post (Date().time, "",
                    OccurrenceCategory.HOLE,
                    LatLng(-3324343.toDouble(), -4332323.toDouble()), post_comment_box.text.toString())
            mPostPresenter.newPost(post)
            finish()

        }

    }

    override fun onSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
