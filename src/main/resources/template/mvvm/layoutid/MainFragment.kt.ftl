package ${packageName}.view.fragment

import androidx.lifecycle.Observer
import com.ashlikun.core.mvvm.BaseMvvmFragment
import com.ashlikun.core.mvvm.IViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import ${RouterPathPackage}

import ${packageName}.R
import ${packageName}.viewmodel.${vmName}
import kotlinx.android.synthetic.main.${layoutName}.*
/**
 * @author　　: 李坤
 * 创建时间: ${mData}
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：${mvvmDesc}页面
 */
@Route(path = RouterPath.${RouterPath})
@IViewModel(${vmName}::class)
class ${fragmentClass} : BaseMvvmFragment<${vmName}>() {

    override fun getLayoutId(): Int {
        return R.layout.${layoutName}
    }

    override fun initView() {

    }
}
