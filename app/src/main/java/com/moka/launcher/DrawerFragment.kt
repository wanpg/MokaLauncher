package com.moka.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


/**
 * Created by wanpg on 2017/10/26.
 */
class DrawerFragment : Fragment() {

    private lateinit var appListView: RecyclerView
    private lateinit var appList: List<ResolveInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        appList = activity.packageManager.queryIntentActivities(mainIntent, 0)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_drawer, null)
        if (view != null) {
            init(view)
        }
        return view
    }

    private fun init(container: View) {
        appListView = container.findViewById(R.id.app_list)
        appListView.layoutManager = GridLayoutManager(context, 5)
        appListView.adapter = MyAdapter(context, appList)
    }

    private class MyAdapter(context: Context, appList: List<ResolveInfo>) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
        private var appList: List<ResolveInfo> = appList
        private var context: Context = context

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
            return MyHolder(LayoutInflater.from(context).inflate(R.layout.app_layout, parent, false))
        }

        override fun onBindViewHolder(holder: MyHolder?, position: Int) {
            val resolveInfo = appList[position]
            holder?.update(resolveInfo)
            holder?.itemView?.setOnClickListener({
                //该应用的包名
                val pkg = resolveInfo.activityInfo.packageName
                //应用的主activity类
                val cls = resolveInfo.activityInfo.name
                val componet = ComponentName(pkg, cls)

                val intent = Intent()
                intent.component = componet
                context.startActivity(intent)
            })
        }


        override fun getItemCount(): Int {
            return appList.size
        }

        class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var icon: ImageView? = itemView?.findViewById(R.id.icon)
            var name: TextView? = itemView?.findViewById(R.id.name)

            fun update(info: ResolveInfo) {
                Glide.with(itemView)
                        .load(info)
                        .apply(RequestOptions()
                                .centerInside()
                                .placeholder(R.drawable.ic_launcher_background))
                        .into(icon)

//                val loadIcon = info.loadIcon(itemView.context.packageManager)
//                icon?.setImageDrawable(loadIcon)
                name?.text = info.loadLabel(itemView.context.packageManager)
            }
        }
    }
}