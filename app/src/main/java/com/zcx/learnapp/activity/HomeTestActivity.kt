package com.zcx.learnapp.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.zcx.learnapp.R
import com.zcx.learnapp.adapter.ViewHolder
import com.zcx.learnapp.base.BaseActivity
import com.zcx.learnapp.utils.dp
import com.zcx.lib_annotations.Subject

@Subject("首页测试")
class HomeTestActivity : BaseActivity() {
    private lateinit var itemRv: RecyclerView
    private var lastStick = false

    override fun getLayoutId() = R.layout.activity_home_test
    private var lastHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemRv = findViewById(R.id.itemRv)

        val layoutManager = VirtualLayoutManager(this)
        val delegateAdapter = DelegateAdapter(layoutManager)
        itemRv.layoutManager = layoutManager
        itemRv.adapter = delegateAdapter

        delegateAdapter.addAdapter(BlockAdapter(this, Color.parseColor("#999999"), 60, "搜索区域"))

        val testBlockAdapter = TestBlockAdapter(
            this,
            StickyLayoutHelper()
        )
        delegateAdapter.addAdapter(
            testBlockAdapter
        )

        val stickLayoutHelper = StickyLayoutHelper()
        delegateAdapter.addAdapter(
            BlockAdapter(
                this,
                Color.parseColor("#999999"),
                40,
                "二级 TAB",
                stickLayoutHelper
            )
        )

        val list = mutableListOf<String>()
        repeat(20) { i ->
            list.add("$i")
        }
        delegateAdapter.addAdapter(TextAdapter(this, list))

        itemRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val stickNow = testBlockAdapter.isStickNow()
                if (stickNow && dy > 0 && !lastStick) {
                    testBlockAdapter.onStickStateChanged(true)
                    lastStick = true
                } else if (!stickNow && dy < 0 && lastStick) {
                    testBlockAdapter.onStickStateChanged(false)
                    lastStick = false
                }

                if (stickNow) {
                    testBlockAdapter.onSticky(dy)
                }

                val itemView = testBlockAdapter.itemView
                val offset = if (itemView == null) {
                    0
                } else {
                    if (lastStick) {
                        itemView.measuredHeight - itemView.scrollY
                    } else {
                        itemView.measuredHeight
                    }
                }
                stickLayoutHelper.setOffset(offset)
            }
        })
    }

    class TestBlockAdapter(
        context: Context,
        layoutHelper: LayoutHelper = SingleLayoutHelper()
    ) : SingleItemAdapter(context, layoutHelper) {
        companion object {
            val TAG = TestBlockAdapter::class.simpleName
        }

        var itemView: View? = null

        fun isStickNow(): Boolean {
            return getStickyLayoutHelper()?.isStickyNow ?: false
        }

        private fun getStickyLayoutHelper(): StickyLayoutHelper? {
            return (mLayoutHelper as? StickyLayoutHelper)
        }

        override fun getLayoutId() = R.layout.item_block_test

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            itemView = holder.itemView
        }

        fun onStickStateChanged(stickNow: Boolean) {
            itemView?.let {
                val subTextTv = it.findViewById<TextView>(R.id.subTextTv)
                subTextTv.visibility = if (stickNow) View.GONE else View.VISIBLE

                it.requestLayout()
            }
        }

        fun onSticky(dy: Int) {
            itemView?.let {
                var offset = it.scrollY + dy
                if (dy > 0) {
                    // 上滑
                    offset = if (offset > it.measuredHeight) {
                        it.alpha = 0f
                        it.measuredHeight - it.scrollY
                    } else {
                        it.alpha = 1f
                        dy
                    }
                    it.scrollBy(0, offset)
                } else if (dy < 0) {
                    // 下滑
                    offset = if (offset < 0) -it.scrollY else dy
                    it.scrollBy(0, offset)
                    it.alpha = 1f
                }
            }
        }
    }

    open class BlockAdapter(
        context: Context,
        private val mColor: Int,
        private val mHeight: Int,
        private val mText: String,
        layoutHelper: LayoutHelper = SingleLayoutHelper()
    ) : SingleItemAdapter(context, layoutHelper) {
        override fun getLayoutId() = R.layout.item_block

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val textTv = holder.getView<TextView>(R.id.textTv) ?: return
            textTv.setBackgroundColor(mColor)
            textTv.text = mText

            val lp = textTv.layoutParams
            lp.height = mHeight.dp
            textTv.layoutParams = lp
        }
    }

    class TabBarAdapter(context: Context, layoutHelper: LayoutHelper) :
        SingleItemAdapter(context, layoutHelper) {
        override fun getLayoutId() = R.layout.item_tab_bar

        private var itemView: View? = null
        private var firstView: View? = null
        private var secondView: View? = null

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            itemView = holder.itemView
            firstView = holder.getView(R.id.firstTv)
            secondView = holder.getView(R.id.secondTv)
        }

        public fun onScrolled(dx: Int, dy: Int) {
            val layoutHelper = mLayoutHelper
            val itemView = this.itemView
            if (layoutHelper !is StickyLayoutHelper || itemView == null) {
                return
            }

            if (layoutHelper.isStickyNow) {
                if (dy > 0) {
                    firstView?.visibility = View.GONE
                    notifyItemChanged(0)
                } else if (dy < 0) {
                    firstView?.visibility = View.VISIBLE
                    notifyItemChanged(0)
                }
            } else {
                if (dy < 0) {
                    firstView?.visibility = View.VISIBLE
                    notifyItemChanged(0)
                }
            }
        }
    }

    class TextAdapter(context: Context, private val list: List<String>) :
        ListAdapter<String>(context, list) {
        override fun getLayoutId() = R.layout.item_text

        override fun convert(holder: ViewHolder, position: Int, item: String) {
            holder.setText(R.id.textTv, item)
        }
    }

    abstract class BaseAdapter(private val mContext: Context) :
        DelegateAdapter.Adapter<ViewHolder>() {
        abstract fun getLayoutId(): Int
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.createViewHolder(mContext, parent, getLayoutId())
        }
    }

    abstract class SingleItemAdapter(
        context: Context,
        protected val mLayoutHelper: LayoutHelper = SingleLayoutHelper()
    ) : BaseAdapter(context) {
        override fun getItemCount() = 1

        override fun onCreateLayoutHelper(): LayoutHelper {
            return mLayoutHelper
        }
    }

    abstract class ListAdapter<T>(protected val mContext: Context, private val mData: List<T>?) :
        BaseAdapter(mContext) {

        override fun getItemCount(): Int {
            val data = mData
            return data?.size ?: 0
        }

        abstract fun convert(holder: ViewHolder, position: Int, item: T)

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            if (item != null) {
                convert(holder, position, item)
            }
        }

        override fun onCreateLayoutHelper(): LayoutHelper {
            return LinearLayoutHelper()
        }

        fun getItem(position: Int): T? {
            val data = mData
            return if (data != null && 0 <= position && position < data.size) {
                data[position]
            } else null
        }
    }
}