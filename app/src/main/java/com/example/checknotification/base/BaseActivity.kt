package dong.duan.ecommerce.library.base

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.checknotification.R
import com.example.checknotification.base.ActivityData
import com.example.checknotification.base.CountdownService
import com.example.checknotification.base.HistoryManager
import com.example.checknotification.base.NavigationHistoryManager


abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {

    lateinit var serviceIntent: Intent

    companion object {
        private val TAG = BaseActivity::class.java.name
    }

    protected lateinit var binding: V
    private var onFullscreen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        decorView = window.decorView
        createView()

        serviceIntent = Intent(this, CountdownService::class.java)

        val intent = intent
        HistoryManager.instance?.addToHistory(intent)
        NavigationHistoryManager.getInstance().addToHistory(ActivityData(intent.component?.className ?: ""))

    }
    protected abstract fun getLayoutResourceId(): Int

    protected abstract fun getViewBinding(): V

    protected abstract fun createView()


    private var decorView: View? = null
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && onFullscreen) {
            decorView!!.systemUiVisibility = hideSystemBars()
        }
    }

    open fun hideSystemBars(): Int {
        return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    protected open fun setFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            val windowInsetsController = window.insetsController
            if (windowInsetsController != null) {
                windowInsetsController.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = hideSystemBars()
        }
    }

    open fun showKeyboard(view: View?) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    open fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    fun handleBackpress() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate();
        }

        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    open fun addFragment(
        fragment: Fragment,
        viewId: Int = android.R.id.content,
        addToBackStack: Boolean = true
    ) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(viewId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    open fun addFragment(
        fragment: Fragment,
        viewId: Int =android.R.id.content,
        addToBackStack: Boolean = true,
        hideBottomBar:Boolean
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        if(hideBottomBar){
            findViewById<FrameLayout>(viewId).visibility=View.GONE
        }
        transaction.add(viewId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun onPause() {
        super.onPause()
        startService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        val noti= NotificationManagerCompat.from(this)
        noti.cancel(123)
    }


    override fun onResume() {
        super.onResume()
        stopService(serviceIntent)
    }
    open fun replaceFragment(
        fragment: Fragment,
        viewId: Int = android.R.id.content,
        addToBackStack: Boolean = true
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(viewId, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commit()
    }

    protected fun showFullscreen(on: Boolean) {
        onFullscreen = on
        if (on)
            setFullscreen()
    }

}

