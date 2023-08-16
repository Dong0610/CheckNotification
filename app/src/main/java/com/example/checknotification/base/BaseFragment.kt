package com.egame.backgrounderaser.aigenerator.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.checknotification.base.ActivityData
import com.example.checknotification.base.NavigationHistoryManager
import dong.duan.ecommerce.library.base.BaseActivity

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected lateinit var binding: T
    private lateinit var callback: OnBackPressedCallback

    open fun handlerBackPressed(){}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handlerBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onDestroy() {
        super.onDestroy()
        callback.remove()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getBinding(inflater, container)


        val fragmentTag = this::class.java.simpleName
        NavigationHistoryManager.getInstance().addToHistory(ActivityData(fragmentTag))


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }



    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?) :T
    abstract fun initView()

    fun addFragment(fragment: Fragment){
        (requireActivity() as BaseActivity<*>).addFragment(fragment)
    }

    fun replaceFullViewFragment(fragment: Fragment, addToBackStack: Boolean){
        (requireActivity()  as BaseActivity<*>).replaceFragment(fragment, android.R.id.content, addToBackStack)
    }
    fun replaceFragment(fragment: Fragment) {
        (requireActivity()  as BaseActivity<*>).replaceFragment(fragment)
    }
    open fun closeFragment(fragment: Fragment) {
        (requireActivity() as BaseActivity<*>).handleBackpress()
    }

    fun addAndRemoveCurrentFragment(currentFragment : Fragment, newFragment : Fragment, addToBackStack: Boolean = false) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.remove(currentFragment)
        transaction.add(android.R.id.content, newFragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    protected open fun hideKeyboard() {
        if (activity != null) {
            (activity as BaseActivity<*>?)?.hideKeyboard()
        }
    }

    protected open fun showKeyboard(view: View?) {
        (requireActivity() as BaseActivity<*>?)?.showKeyboard(view)
    }

    protected fun setColorStatusBar(idColor : Int){
        if(activity != null){
            (activity as BaseActivity<*>).window.statusBarColor = ContextCompat.getColor(requireContext(), idColor)
        }
    }

    protected fun getResultListener(requestKey : String, callback : (requestKey : String, bundle : Bundle) -> Unit){
        parentFragmentManager.setFragmentResultListener(requestKey, this
        ) { key, result ->
            callback(key, result)
        }
    }

    protected fun setFragmentResult(requestKey: String, resultBundle : Bundle){
        requireActivity().supportFragmentManager.setFragmentResult(requestKey, resultBundle)
    }

    companion object{
        var isGoToSetting = false
        var isAdsRewardShowing =  false
        fun <F : Fragment> newInstance(fragment: Class<F>, args: Bundle? = null): F {
            val f = fragment.newInstance()
            args?.let {
                f.arguments = it
            }
            return f
        }

    }
}
