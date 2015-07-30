package com.huntor.mscrm.app2.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.KbEntry;
import com.huntor.mscrm.app2.ui.ChatExtraDetailActivity;
import com.huntor.mscrm.app2.utils.Constant;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/13 0013
 * 19:09.
 */
public class ChatExtraChildAdapter extends BaseAdapter {

    private final String TAG = "ChatExtraChildAdapter";
    private Context context;

    private LayoutInflater inflater;
    private List<KbEntry> list;

    public ChatExtraChildAdapter(Context context, List<KbEntry> list) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }else {
            throw new IllegalStateException("Context must not be null");
        }
        setData(list);
    }

    public void setData(List<KbEntry> list) {
        if (this.list == null) {
            this.list = new LinkedList<KbEntry>();
        }
        if (list != null) {
            this.list = list;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public KbEntry getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = inflater.inflate(R.layout.item_chat_extra_child, parent, false);
        TextView textHolder = (TextView) ret.findViewById(R.id.text_item_chat_extra_name);
        KbEntry entry = getItem(position);

        final String title = entry.title;
        final String content = entry.content;

        textHolder.setText(title);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatExtraDetailActivity.class);
                intent.putExtra(Constant.CHAT_EXTRA_INTENT_TITLE, title);
                intent.putExtra(Constant.CHAT_EXTRA_INTENT_CONTENT, content);
                context.startActivity(intent);
            }
        });
        textHolder.measure(0, 0);
//        Log.e(TAG, "===========宋雷" + title);
//        Log.e(TAG, "height=" + holder.textHolder.getMeasuredHeight());
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        param.height = textHolder.getMeasuredHeight();
        ret.setLayoutParams(param);

        return ret;
    }

}
