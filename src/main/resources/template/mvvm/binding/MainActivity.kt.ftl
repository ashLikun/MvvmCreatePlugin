package ${packageName}.view.activity

import androidx.lifecycle.Observer
import com.ashlikun.core.mvvm.BaseMvvmActivity
import com.ashlikun.core.mvvm.IViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import ${RouterPathPackage}

import ${packageName}.R
import ${packageName}.viewmodel.${vmName}
import ${packageName}.databinding.${viewBinding}
/**
 * @author　　: 李坤
 * 创建时间: ${mData}
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：${mvvmDesc}页面
 */
@Route(path = RouterPath.${RouterPath})
@IViewModel(${vmName}::class)
class ${activityClass} : BaseMvvmActivity<${vmName}>() {
    override val binding by lazy {
        ${viewBinding}.inflate(layoutInflater)
    }
    override fun initView() {
        toolbar?.setBack(this)
    }
}
