package com.huntor.mscrm.app2.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 所有适配器的父类
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	// 上下文对象
	protected Context context;
	// 布局填充器
	protected LayoutInflater inflater;
	// 泛型集合
	protected ArrayList<T> list = new ArrayList<T>();

	/**
	 * 构造方法
	 * 
	 * @param c
	 */
	public MyBaseAdapter(Context c) {
		super();
		// 传递activity
		this.context = c;
		inflater = LayoutInflater.from(context);

	}

	/**
	 * 返回适配器中数据
	 */
	public ArrayList<T> getAdapterData() {
		return list;
	}

	/**
	 * 清空适配器数据
	 */
	public void clearAdapterData(){
		list.clear();
	}

	/**
	 * 封装添加一条记录方法 t 一条数据的对象 isClearOld 是否清除原数据
	 */
	public void appendData(T t, boolean isClearOld) {
		if (t == null) { // 非空验证
			return;
		}
		if (isClearOld) {// 如果true 清空原数据
			list.clear();
		}// 添加一条新数据到最后
		list.add(t);

	}

	/**
	 * 添加多条记录
	 * 
	 * @param alist
	 *            数据集合
	 * @param isClearOld
	 *            是否清空原数据
	 */
	public void addendData(ArrayList<T> alist, boolean isClearOld) {
		if (alist == null) {
			return;
		}
		if (isClearOld) {
			list.clear();
		}
		list.addAll(alist);

	}

	/**
	 * 添加一条记录 到第一条
	 * 
	 * @param t
	 * @param isClearOld
	 */
	public void appendDataTop(T t, boolean isClearOld) {
		if (t == null) { // 非空验证
			return;
		}
		if (isClearOld) {// 如果true 清空原数据
			list.clear();
		}// 添加一条新数据到第一条
		list.add(0, t);

	}

	/**
	 * 添加多条记录到顶部
	 * 
	 * @param alist
	 *            数据集合
	 * @param isClearOld
	 *            是否清空原数据
	 */
	public void addendDataTop(ArrayList<T> alist, boolean isClearOld) {
		if (alist == null) {
			return;
		}
		if (isClearOld) {
			list.clear();
		}

		list.addAll(0, alist);

	}

	/**
	 * 更新适配器数据
	 */
	public void updateAdapter() {
		this.notifyDataSetChanged();
	}

	/**
	 * 清空适配器数据
	 */
	public void clearAdapter() {
		list.clear();
	}

	/**
	 * 删除一条数据
	 * 
	 * @param id
	 *            下标
	 */
	public void delData(int id) {
		if (list.size() != 0) {
			list.remove(id);
		}

	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}

	}

	@Override
	public T getItem(int position) {
		if (list == null) {
			return null;
		}
		// 如果已经没有数据了返回空
		if (position > list.size() - 1) {
			return null;
		}
		return list.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return getMyView(position, convertView, parent);
	}

	/**
	 * 用户必须实现此方法 加载不同的布局
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getMyView(int position, View convertView,
			ViewGroup parent);

}
