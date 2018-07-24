package com.zq.scavenging.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zq.scavenging.R;
import com.zq.scavenging.acty.BaseActy;
import com.zq.scavenging.bean.Label;

import java.util.List;

/**
 * Created by AIERXUAN on 2018/6/22.
 */

public class LabelListAdapter extends BaseAdapter {

    private List<Label> list;
    private BaseActy context;
    private LayoutInflater mInflater;

    public LabelListAdapter(Context context, List<Label> list) {
        mInflater = LayoutInflater.from(context);
        this.context = (BaseActy) context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        ViewHolder holder = null;
        Label bean = list.get(position);
        // 判断convertView的状态，来达到复用效果
        if (null == convertView) {
            //如果convertView为空，则表示第一次显示该条目，需要创建一个view
            view = View.inflate(context, R.layout.item_label, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            // 将holder与view进行绑定
            view.setTag(holder);
        } else {
            //否则表示可以复用convertView
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_name.setText(bean.getName());
        holder.tv_time.setText(bean.getTime() + "");
        return view;
    }

    public class ViewHolder {
        TextView tv_name, tv_time;
    }
}
