package com.huntor.mscrm.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.huntor.mscrm.app.R;

/**
 * Created by jh on 2015/7/17.
 */
public class EmptyAdapter extends BaseAdapter {
    private Context context;

    public EmptyAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return View.inflate(context, R.layout.layout_empty, null);
    }
}
