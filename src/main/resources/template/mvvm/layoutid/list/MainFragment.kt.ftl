package ${packageName}.view.fragment

import androidx.lifecycle.Observer
import com.ashlikun.core.mvvm.BaseMvvmFragment
import com.ashlikun.core.mvvm.IViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import ${BaseSuperListFragment}
import ${RouterPathPackage}
import com.ashlikun.loadswitch.ContextData
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
class ${fragmentClass} : BaseSuperListFragment<${vmName}>() {
    override val adapter by lazy {
        object : CommonAdapter<String>(requireContext(), null) {
            override fun convert(holder: ViewHolder, t: String) {
            }
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.${layoutName}
    }

    override fun initView() {
        toolbar.setBack(this)
    }
    override fun initData() {
        super.initData()
        viewModel.mainData.observe(this) {
            adapter.datas = it
            adapter.notifyDataSetChanged()
            if (adapter.isEmpty) {
                showEmpty()
            }
        }
    }
    override fun onRefresh() {
        viewModel.getData(true)
    }

    override fun onLoadding() {
        viewModel.getData(false)
    }

    override fun onRetryClick(data: ContextData?) {
        super.onRetryClick(data)
        viewModel.getData(true)
    }

    override fun getSuperRecyclerView() = superRecyclerView
}