package io.github.kaii_lb.yamfsquared.manager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.kaii_lb.yamfsquared.R
import io.github.kaii_lb.yamfsquared.databinding.SidebarItemviewBinding
import io.github.kaii_lb.yamfsquared.xposed.ui.window.model.AppInfo

class SideBarAdapter (
    private val onClick: (AppInfo) -> Unit,
    private val sideBarApp: ArrayList<AppInfo>
) : RecyclerView.Adapter<SideBarAdapter.ViewHolder>() {

    fun setData(items: List<AppInfo>?) {
        sideBarApp.apply {
            clear()
            items?.let { addAll(it) }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SideBarAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sidebar_itemview, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(sideBarApp[position])

    override fun getItemCount(): Int = sideBarApp.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = SidebarItemviewBinding.bind(itemView)
        fun bind(appInfo: AppInfo){
            binding.apply {
                ivAppIcon.setImageDrawable(appInfo.icon)
                ivAppIcon.setOnClickListener {
                    onClick(appInfo)
                }
                if (appInfo.id != 0) {
                    ivWorkIcon.visibility = View.VISIBLE
                }
            }
        }
    }

}