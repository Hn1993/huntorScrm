package com.huntor.mscrm.app2.ui.fragment.member;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.MyGroupAdapter;
import com.huntor.mscrm.app2.model.Target;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiCreateTargetlist;
import com.huntor.mscrm.app2.net.api.ApiDeleteTargetList;
import com.huntor.mscrm.app2.net.api.ApiFansTargetListUpdate;
import com.huntor.mscrm.app2.net.api.ApiFansTargetLists;
import com.huntor.mscrm.app2.provider.api.ApiTargetListDb;
import com.huntor.mscrm.app2.ui.MainActivity2;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.XListView;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;
import com.huntor.mscrm.app2.view.EditText.ColorUnderLineEditText;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 我的分组
 */
public class MyGroupFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "MyGroupFragment";
    private XListView mListView;
    private TextView mNoContentHint;
    private PopupWindow pw;
    private MyGroupAdapter adapter;
    private View ret;
    private BaseActivity activity;
    private int mPreviousVisibleItem;
    private Toolbar toolbar;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ret = inflater.inflate(R.layout.fragment_my_group, container, false);
        initView();
        setListener();
        showFAB();
        getLocalTargetList();
        getTargetlist();
        return ret;
    }

    private void showFAB() {
        final FloatingActionButton fab = (FloatingActionButton) ret.findViewById(R.id.fab);
        fab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show(true);
                fab.setShowAnimation(AnimationUtils.loadAnimation(activity, R.anim.show_from_bottom));
                fab.setHideAnimation(AnimationUtils.loadAnimation(activity, R.anim.hide_to_bottom));
            }
        }, 300);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTargetName();
                //startActivity(new Intent(MainActivity.this, FloatingMenusActivity.class));
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
                    fab.hide(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    fab.show(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });
    }


    private void initView() {
        activity = (BaseActivity) getActivity();
        toolbar= MainActivity2.toolbar;
        mListView = (XListView) ret.findViewById(R.id.list_my_group);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(false);
        mNoContentHint = (TextView) ret.findViewById(R.id.no_content_hint);
        adapter = new MyGroupAdapter(getActivity());
        mListView.setAdapter(adapter);

    }

    public void setListener() {
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
    }

    /**
     * 给一个控件设置一个动画
     *
     * @param view
     */
    private void setAnimation(View view) {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(
                0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(400);
        scale.setFillAfter(true);
        set.addAnimation(scale);

        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(400);
        alpha.setFillAfter(true);
        set.addAnimation(alpha);
        view.startAnimation(set);
    }

    @Override
    public void onResume() {
        //toolbar.setTitle("我的分组");
        super.onResume();
    }

    /**
     * ListView条目点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Target target = adapter.getItem(position - 1);
        Bundle bundle = new Bundle();
        bundle.putInt("targetListId", target.id);
        bundle.putString("name", target.name);
        bundle.putInt("size", target.count);
        GroupMemberFragment2 fragment = new GroupMemberFragment2();
        fragment.setArguments(bundle);
        transaction.add(R.id.fl_content, fragment, Constant.GROUP_MEMBER);
        fragment.setRefreshCallback(new GroupMemberFragment2.RefreshCallback() {
            @Override
            public void onResult(int targetListId, int groupSize) {
                if (groupSize != -1) {
                    List<Target> data = adapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        Target target = data.get(i);
                        if (target.id == targetListId) {
                            target.count = groupSize;
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                } else {
                    toolbar.setTitle("我的分组");
                }

            }
        });
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * ListView条目长按事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        LayoutInflater infalter = (LayoutInflater) getActivity().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View contentView = infalter.inflate(R.layout.item_my_group_update, null);
        int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
        pw = new PopupWindow(contentView, WRAP_CONTENT, WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new BitmapDrawable());//点滴弹窗以外的地方消失
        int[] location = new int[2];
        //ImageView iv_my_group_head = (ImageView) view.findViewById(R.id.iv_my_group_head);
        TextView text_item_my_group_name = (TextView) view.findViewById(R.id.text_item_my_group_name);
        text_item_my_group_name.getLocationInWindow(location);
        int x = location[0];
        int y = location[1] - 30;
        pw.showAtLocation(view, Gravity.TOP | Gravity.LEFT, x, y);

        setAnimation(contentView);
        TextView tv_delete = (TextView) contentView.findViewById(R.id.tv_delete);
        TextView tv_update = (TextView) contentView.findViewById(R.id.tv_update);

        MyOnClickListener listener = new MyOnClickListener(position);
        tv_delete.setOnClickListener(listener);
        tv_update.setOnClickListener(listener);
        return true;
    }

    /**
     * 设置弹出窗口点击监听（删除和编辑）
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Target target = adapter.getItem(position - 1);
            //Log.i(TAG, "target:" + target.toString());
            switch (v.getId()) {
                case R.id.tv_delete:
                    if (target != null) {
                        deleteTargetName(target);
                    }
                    break;
                case R.id.tv_update:
                    if (target != null) {
                        updateTargetName(target);
                    }
                    break;
                default:
                    break;
            }
            pw.dismiss();
        }
    }

    /**
     * 删除分组弹出确认对话框
     *
     * @param target
     */
    private void deleteTargetName(final Target target) {
        LayoutInflater infalter = (LayoutInflater) getActivity().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = infalter.inflate(R.layout.set_group_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("确定删除?");
        EditText et_group_name = (EditText) view.findViewById(R.id.et_group_name);
        et_group_name.setVisibility(View.GONE);

        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteTargetListItem(target);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 更新分组对话框
     *
     * @param target
     */
    private void updateTargetName(final Target target) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.set_group_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

        final EditText et_group_name = (EditText) view.findViewById(R.id.et_group_name);
        et_group_name.setText(target.name);
        et_group_name.setSelection(target.name.length());

        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = et_group_name.getText().toString().trim();
                if (TextUtils.isEmpty(groupName)) {
                    Utils.toast(getActivity(), "分组名称不能为空!");
                } else if (groupName.equals(target.name)) {
                    Utils.toast(getActivity(), "分组名称没有更新!");
                } else {
                    dialog.dismiss();
                    updateTargetListItem(groupName, target);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 更新自定义分组名称
     *
     * @param groupName
     * @param target
     */
    public void updateTargetListItem(final String groupName, final Target target) {
        HttpRequestController.getFansTargetListUpdate(getActivity(), target.id, groupName, groupName,
                new HttpResponseListener<ApiFansTargetListUpdate.ApiFansTargetListUpdateResponse>() {
                    @Override
                    public void onResult(ApiFansTargetListUpdate.ApiFansTargetListUpdateResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            target.name = groupName;
                            adapter.notifyDataSetChanged();
                            Utils.toast(getActivity(), "更新成功！");
                        }
                    }
                });
    }

    /**
     * 删除自定义分组
     *
     * @param target
     */
    private void deleteTargetListItem(final Target target) {
        HttpRequestController.deleteFansTargetList(getActivity(), target.id,
                new HttpResponseListener<ApiDeleteTargetList.ApiDeleteTargetListResponse>() {
                    @Override
                    public void onResult(ApiDeleteTargetList.ApiDeleteTargetListResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            adapter.getData().remove(target);
                            adapter.notifyDataSetChanged();
                            Utils.toast(getActivity(), "删除成功！");
                        }
                    }
                });
    }

    /**
     * 获取本地分组数据
     */
    private void getLocalTargetList() {
        updateAdapter(ApiTargetListDb.getTargetList(activity));
    }

    /**
     * 获取分组列表数据
     */
    private void getTargetlist() {
        if (activity != null) {
            //activity.showCustomDialog(R.string.loading);
        }
        int empId = PreferenceUtils.getInt(getActivity(), Constant.PREFERENCE_EMP_ID, 0);
        HttpRequestController.getTargetLists(getActivity(), empId,
                new HttpResponseListener<ApiFansTargetLists.ApiFansTargetListsResponse>() {
                    @Override
                    public void onResult(ApiFansTargetLists.ApiFansTargetListsResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            updateAdapter(response.targetLists);
                        } else if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR) {
                            //updateAdapter(response.targetLists);
                        } else {
                            MyLogger.i(TAG, "response.getRetInfo(): " + response.getRetInfo());
                        }
                        if (activity != null) {
                            activity.dismissCustomDialog();
                        }

                    }
                });
    }

    /**
     * 刷新Adapter
     *
     * @param targets
     */
    private void updateAdapter(List<Target> targets) {
        if (targets == null || targets.size() == 0) {//提示没有内容信息
            mNoContentHint.setText("没有分组信息");
            mNoContentHint.setVisibility(View.VISIBLE);
        } else {
            mNoContentHint.setVisibility(View.GONE);
            adapter.setData(targets);
            adapter.notifyDataSetChanged();
            mListView.setSelection(mCurrentIndex);//定位到当前
        }
    }

    /**
     * 返回和添加分组按钮点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int id = v.getId();
        switch (id) {
            case R.id.img_left_corner:
                manager.popBackStack();
                transaction.remove(this);
                break;
        /*	case R.id.add_group_symbol_imag://创建分组
				addTargetName();
				break;*/
        }
        transaction.commit();
    }


    /**
     * 添加分组对话框
     */
    private void addTargetName() {
        LayoutInflater infalter = (LayoutInflater) getActivity().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = infalter.inflate(R.layout.set_group_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

        //TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        //final EditText et_group_name = (EditText) view.findViewById(R.id.et_group_name);
        final ColorUnderLineEditText et_group_name = (ColorUnderLineEditText) view.findViewById(R.id.et_group_name);

        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = et_group_name.getText().toString().trim();
                if (TextUtils.isEmpty(groupName)) {
                    Utils.toast(getActivity(), "分组名称不能为空!");
                } else {
                    dialog.dismiss();
                    addTargetListItem(groupName, groupName);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 网络请求新增一个分组
     *
     * @param groupName
     * @param desc
     */
    private void addTargetListItem(String groupName, String desc) {
        int empID = PreferenceUtils.getInt(getActivity(), Constant.PREFERENCE_EMP_ID, 0);
        HttpRequestController.createFansTargetList(getActivity(), empID, groupName, desc,
                new HttpResponseListener<ApiCreateTargetlist.ApiCreateTargetlistResponse>() {
                    @Override
                    public void onResult(ApiCreateTargetlist.ApiCreateTargetlistResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            //mCurrentIndex = adapter.getCount() + 1;
                            getTargetlist();
                            Utils.toast(getActivity(), "添加成功！");
                        } else {
                            Utils.toast(getActivity(), "添加失败！");
                        }
                    }
                });

    }


}
