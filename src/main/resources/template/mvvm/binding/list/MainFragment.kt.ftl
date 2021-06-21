package ${packageName}.view.fragment

import androidx.lifecycle.Observer
import com.ashlikun.core.mvvm.BaseMvvmFragment
import com.ashlikun.core.mvvm.IViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.CommonAdapter
import ${BaseSuperListFragment}
import ${RouterPathPackage}
import com.ashlikun.loadswitch.ContextData
import ${showEmpty}
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
class ${fragmentClass} : BaseSuperListFragment<${vmName}>() {
    val binding by lazy {
        ${viewBinding}.inflate(layoutInflater)
    }
    override val adapter by lazy {
        object : CommonAdapter<String>(requireContext(), null) {
            override fun convert(holder: ViewHolder, t: String) {
            }
        }
    }

    override fun initView() {
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

    override fun getSuperRecyclerView() = binding.superRecyclerView
}
