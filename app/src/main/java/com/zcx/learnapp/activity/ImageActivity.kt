package com.zcx.learnapp.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.zcx.learnapp.R
import com.zcx.learnapp.adapter.ImageSourceAdapter
import com.zcx.learnapp.adapter.SourceBo
import com.zcx.learnapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_image.*

/**
 * @Description:
 * @Author: zcx

 * @CreateDate: 2021/2/5
 */
class ImageActivity : BaseActivity() {
    private val sources = mutableListOf<SourceBo>()
    private val imageSourceAdapter = ImageSourceAdapter(sources)

    override fun getLayoutId() = R.layout.activity_image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return sources.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = layoutInflater.inflate(R.layout.item_image_source, null)
                val photoView = view.findViewById<PhotoView>(R.id.photoView)

                val item = sources[position]

                Glide.with(this@ImageActivity).load(item.path).into(photoView)
                view.findViewById<TextView>(R.id.nameTv).text = item.name

                container.addView(view)

                return view
            }

            override fun isViewFromObject(view: View, `object`: Any) = `object` == view

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }

        val intent = this.intent
        if (intent != null) {
            val uri = intent.getSerializableExtra("source") as String
            val luban = intent.getSerializableExtra("luban") as String
            val so = intent.getSerializableExtra("so") as String

            sources.add(SourceBo("原图", uri))
            sources.add(SourceBo("Luban 引擎", luban))
            sources.add(SourceBo("So 引擎", so))
            viewPager.adapter?.notifyDataSetChanged()
        }
    }
}