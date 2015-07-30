package com.huntor.mscrm.app2.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.Categorie;
import com.huntor.mscrm.app2.model.Category;
import com.huntor.mscrm.app2.model.ChatExtraGroup;
import com.huntor.mscrm.app2.model.KbEntry;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiKbCategorieContent;
import com.huntor.mscrm.app2.provider.api.ApiKbCategoriesDb;
import com.huntor.mscrm.app2.provider.api.ApiKbEntryDb;
import com.huntor.mscrm.app2.ui.ChatExtraActivity;
import com.huntor.mscrm.app2.ui.ChatExtraDetailActivity;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.utils.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/13 0013
 * 18:47.
 */
public class ChatExtraGroupAdapter extends BaseAdapter{

    private final String TAG = "ChatExtraGroupAdapter";
    private Context context;
    private LayoutInflater inflater;
    private List<Categorie> list;
    private ViewHolder viewHolder;

    public ChatExtraGroupAdapter(Context context, List<Categorie> list) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }else{
            throw new IllegalStateException("Context must nut be null");
        }
        if (list != null) {
            this.list = list;
        }else {
            this.list = new LinkedList<Categorie>();
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Categorie getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        if (convertView != null) {
            ret = convertView;
        }else {
            ret = inflater.inflate(R.layout.item_chat_extra_group, parent, false);
        }
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.textHolder = (TextView) ret.findViewById(R.id.text_item_chat_extra_group_title);
            holder.listHolder = (LinearLayout) ret.findViewById(R.id.list_chat_extra_child);
            holder.layoutHolder = (LinearLayout) ret.findViewById(R.id.layout_chat_extra_group);
            holder.arrow = (IconTextView) ret.findViewById(R.id.img_item_right_corner);
            ret.setTag(holder);
        }
        final Categorie categorie = getItem(position);
        holder.textHolder.setText(categorie.name);

        //查询当前类别下的数据信息
//        holder.layoutHolder.setOnTouchListener(this);
        holder.layoutHolder.setTag(R.id.tag_first, holder.listHolder);
        holder.layoutHolder.setTag(R.id.tag_second,categorie.id);
        if(categorie.isExpand){
            holder.arrow.setText(R.string.icon_text_cheveron_up);
        }else{
            holder.arrow.setText(R.string.icon_text_cheveron_down);
        }
        holder.layoutHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = (LinearLayout) v.getTag(R.id.tag_first);
                int id = (Integer)v.getTag(R.id.tag_second);
                if(categorie.isExpand){
                    categorie.isExpand = false;
                    ll.setVisibility(View.GONE);
                }else{
                    categorie.isExpand = true;
                    ll.setVisibility(View.VISIBLE);
                    long saveTime = PreferenceUtils.getLong(context, Constant.PREFERENCES_SAVE_TIME,0);
                    List<KbEntry> kbEntries = ApiKbEntryDb.getKbEntryByCategorieId(context, id);
                    if(!DateFormatUtils.isUpdate(context, saveTime) && kbEntries != null && kbEntries.size()>=1) {
                        if (Constant.DEBUG) {
                            MyLogger.i(TAG, "kbEntries = " + kbEntries.toString());
                        }
                        refresh(kbEntries,ll);
                    }else {
                        getChatExtraChildList(id, ll);
                    }
                }
                notifyDataSetChanged();

            }
        });

        viewHolder = holder;
        return ret;
    }

    private void getChatExtraChildList(int categorieId,final LinearLayout ll) {
        ((BaseActivity)context).showCustomDialog(R.string.loading);
        HttpRequestController.kbContents(context, categorieId,
                new HttpResponseListener<ApiKbCategorieContent.ApiKbCategorieContentResponse>() {
                    @Override
                    public void onResult(ApiKbCategorieContent.ApiKbCategorieContentResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            //Log.i("ChatExtraGroupAdapter", "response.kbEntries = " + response.kbEntries);
                            refresh(response.kbEntries,ll);
                        } else {
                            Utils.toast(context, response.getRetInfo() + "");
                        }
                        ((BaseActivity)context).dismissCustomDialog();
                    }
                });
    }

    private void refresh(List<KbEntry> list, LinearLayout ll){
        ll.removeAllViews();
        for(KbEntry kb :list){
            View ret = inflater.inflate(R.layout.item_chat_extra_child, null, false);
            TextView tv = (TextView)ret.findViewById(R.id.text_item_chat_extra_name);
            tv.setText(kb.title);
            tv.setTag(R.id.tag_first, kb.title);
            tv.setTag(R.id.tag_second,kb.content);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatExtraDetailActivity.class);
                    intent.putExtra(Constant.CHAT_EXTRA_INTENT_TITLE, v.getTag(R.id.tag_first).toString());
                    intent.putExtra(Constant.CHAT_EXTRA_INTENT_CONTENT, v.getTag(R.id.tag_second).toString());
                    context.startActivity(intent);
                }
            });
            ll.addView(ret);
        }

        ChatExtraChildAdapter ceca = new ChatExtraChildAdapter(context, list);
//                            listView.setAdapter(ceca);
//                            setPullLvHeight(listView);
    }

    private void setPullLvHeight(ListView pull){
        int totalHeight = 0;
        ListAdapter adapter= pull.getAdapter();
        for (int i = 0, len = adapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, pull);
            listItem.measure(0, 0); //计算子项View 的宽高
            Log.e(TAG,"listItem高度======"+listItem.getMeasuredHeight());
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = pull.getLayoutParams();
        params.height = totalHeight + (pull.getDividerHeight() * pull.getCount());
        pull.setLayoutParams(params);
    }

    private class ViewHolder{
        public TextView textHolder;
        public LinearLayout listHolder;
        public LinearLayout layoutHolder;
        public IconTextView arrow;
    }

}
