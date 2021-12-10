package com.zcx.learnapp.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder

abstract class BaseFragment : Fragment() {

    private lateinit var activity: BaseActivity
    lateinit var contentView: View
        private set
    private lateinit var unbinder: Unbinder
    private var originStatusBarColor: Int = DEFAULT_STATUS_BAR_COLOR
    private var originFullscreen: Boolean = false
    private var originOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    private var viewDestroyed = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as BaseActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        originOrientation = getRequestedOrientation()
    }

    protected fun runOnUiThread(runnable: Runnable) {
        if (isActivityInitialized()) {
            activity.runOnUiThread(runnable)
        }
    }

    private fun isActivityInitialized(): Boolean = ::activity.isInitialized

    protected fun onOrientationChanged(orientation: Int) {}

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window: Window? = activity.window
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                originStatusBarColor = window.statusBarColor
                window.statusBarColor = getStatusBarColor()
            }
            originFullscreen =
                window.attributes?.flags ?: 0 and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
        }

        if (fullscreen()) {
            setFullscreen(true)
        }

        val view = inflater.inflate(getLayoutId(), container, false)
        unbinder = ButterKnife.bind(this, view)
        this.contentView = view
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
    }

    protected fun setRequestedOrientation(orientation: Int) {
        if (isActivityInitialized()) {
            activity.requestedOrientation = orientation

            if (!viewDestroyed) {
                onOrientationChanged(orientation)
            }
        }
    }

    protected fun requestOrientationPortrait() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    protected fun requestOrientationLandscape() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    protected fun isOrientationPortrait(): Boolean {
        return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    protected fun <T : View> findViewById(@IdRes id: Int): T? {
        return if (::contentView.isInitialized) contentView.findViewById(id) else null
    }

    protected abstract fun initViews(view: View)

    override fun onDestroyView() {
        super.onDestroyView()
        if (::unbinder.isInitialized) {
            unbinder.unbind()
        }

        viewDestroyed = true

        setFullscreen(originFullscreen)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window?.statusBarColor = originStatusBarColor
        }
    }

    open fun handleBackEvent() = false

    protected open fun fullscreen() = false

    protected fun setFullscreen(fullscreen: Boolean) {
        if (!isActivityInitialized()) {
            return
        }

        if (fullscreen) {
            activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        if (!viewDestroyed) {
            onFullscreenStateChange(fullscreen)
        }
    }

    protected open fun onFullscreenStateChange(fullscreen: Boolean) {

    }

    protected open fun getStatusBarColor(): Int {
        return DEFAULT_STATUS_BAR_COLOR
    }

    protected open fun getRequestedOrientation(): Int {
        return if (isActivityInitialized()) activity.requestedOrientation else ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    companion object {
        const val DEFAULT_STATUS_BAR_COLOR = Color.WHITE
    }
}