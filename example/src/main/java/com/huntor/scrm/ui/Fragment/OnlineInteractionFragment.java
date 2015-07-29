package com.huntor.scrm.ui.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.huntor.scrm.R;
import com.huntor.scrm.adapter.ContactListAdapter;
import com.huntor.scrm.model.BatchQueryFansResult;
import com.huntor.scrm.model.Contact;
import com.huntor.scrm.model.FansRecordModel;
import com.huntor.scrm.model.MessageRecordModel;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequestController;
import com.huntor.scrm.net.HttpResponseListener;
import com.huntor.scrm.net.api.ApiFans;
import com.huntor.scrm.net.api.ApiFansList;
import com.huntor.scrm.provider.api.ApiFansRecordDb;
import com.huntor.scrm.provider.api.ApiMessageRecordDb;
import com.huntor.scrm.push.PushMessageManager;
import com.huntor.scrm.ui.BaseActivity;
import com.huntor.scrm.ui.ChatActivity;
import com.huntor.scrm.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 2015/7/16.
 */
public class OnlineInteractionFragment extends BaseFragment   implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{


    private static final String TAG = "InteractionOnline";
    public static final String LAST_TIME = "lastTime";
    public static final String ADAPTER_COUNT = "AdapterCount";

    private Context context;

    private List<Contact> data;
    private int[] fanIds;
    private PushMessageManager messageManager;
    private ContactListAdapter adapter;
    private ListView listContactList;
    private View popView;
    private PopupWindow popupWindow;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.dismissCustomDialog();
        }
        context = getActivity();

        messageManager = PushMessageManager.getInstance(getActivity());
        messageManager.registerOnReceivedPushMessageListener(opl);

        initPopWindow();

        super.onCreate(savedInstanceState);
    }

    private void initPopWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        popView = inflater.inflate(R.layout.layout_mypop, null);
        int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popView, wrapContent, wrapContent);
    }

    public static final long ONE_MIN = 1 * 60 * 1000;

    public void onResume() {
        MyLogger.e(TAG, "onResume");

        long currentTime = System.currentTimeMillis();

        long lastTime = Long.parseLong(PreferenceUtils.getString(context, LAST_TIME, "0"));

        MyLogger.w(TAG, "lastTime = " + lastTime + ",currentTime = " + currentTime);
        if (currentTime - lastTime > ONE_MIN) {
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
            PreferenceUtils.putString(context, LAST_TIME, Long.toString(currentTime));
        }
        setContactList();
        super.onResume();
    }

    private void updateUserGrade() {
        setFanIds();
        MyLogger.w(TAG, "fanIds.length = " + fanIds.length);
        MyLogger.w(TAG, "fanIds = " + Arrays.toString(fanIds));

        if (fanIds == null) {
            return;
        }
        HttpRequestController.fansList(context, fanIds,
                new HttpResponseListener<ApiFansList.ApiFansListResponse>() {
                    @Override
                    public void onResult(ApiFansList.ApiFansListResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            MyLogger.i(TAG, "response = " + response.fansList.toString());
                            int empID = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0);
                            int size = response.fansList.size();
                            if (size == fanIds.length) {
                                for (int position = 0; position < size; position++) {//������ѯ��������
                                    BatchQueryFansResult.Fans fans = response.fansList.get(position);
                                    int fanId = fans.id;
                                    for (int i = 0; i < data.size(); i++) {
                                        Contact contact = data.get(i);
                                        if (contact.fan_id == fanId) {
                                            if (TextUtils.isEmpty(fans.errMsg) && empID == fans.empId && fans.followStatus) {
                                                ApiFansRecordDb.updateFansBelongToGroup(context, fanId, fans.group);
                                            } else if (!fans.followStatus || !TextUtils.isEmpty(fans.errMsg) || empID != fans.empId) {
                                                MyLogger.e(TAG, "delete fans: " + fans.toString());
                                                new ApiFansRecordDb(context).delete(fans.id);
                                                new ApiMessageRecordDb(context).deleteByFanId(fans.id);
                                                NotificationUtils.getInstance(context).cancleById(fans.id);
                                            }
                                        }
                                    }
                                }
                                setContactList();
                                MyLogger.w(TAG, "adapter����2");
                            } else {
                                MyLogger.w(TAG, "���ݴ���,�����б��������ϴ����ݳ��Ȳ�һ��");
                            }
                        } else {
                            //Utils.toast(context, response.getRetInfo() + "");
                        }
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        View ret = inflater.inflate(R.layout.fragment_interaction_online, container, false);
        listContactList = (ListView) ret.findViewById(R.id.list_fragment_interaction_online_contact_list);
        listContactList.setOnItemClickListener(this);
        listContactList.setOnItemLongClickListener(this);
        data = new ArrayList<Contact>();
        adapter = new ContactListAdapter(getActivity(), data);
        MyLogger.i(TAG, "data===" + data);
        listContactList.setAdapter(adapter);
        return ret;
    }


    /**
     * �����ݿ������»�ȡ�б�����
     */
    private void setContactList() {
        List<FansRecordModel> fansList = ApiFansRecordDb.getFansList(context);
        int size = fansList.size();
        MyLogger.e(TAG, "fansList.size = " + size);
        MyLogger.e(TAG, "fansList = " + fansList.toString());
        if (fansList.size() > 0) {
            data.clear();
            for (FansRecordModel model : fansList) {
                Contact contact = new Contact();
                contact.imgHead = model.avatar;
                int fanId = model.accountId;
                MyLogger.e(TAG, "fanId = " + fanId);
                MessageRecordModel lastMessage = ApiMessageRecordDb.getLastMessage(context, fanId);
                String type = "1";
                String content = "";
                long time = System.currentTimeMillis();
                if (lastMessage != null) {
                    MyLogger.e(TAG, "lastMessage = " + lastMessage.toString());
                    type = lastMessage.type;
                    content = lastMessage.content;
                    time = lastMessage.timestamp;
                } else {
                    MyLogger.e(TAG, "lastMessage is noll");
                }
                contact.type = type;
                contact.lastWord = content;
                contact.fan_id = model.accountId;
                contact.lastTime = time;
                contact.name = model.realName;
                contact.numberUnread = ApiMessageRecordDb.getUnReadByFansId(context, fanId).size();
                contact.grade = ApiFansRecordDb.getFansInfoById(context, fanId).group;
                MyLogger.i(TAG, "contact: " + contact.toString());
                data.add(contact);
            }
            setFanIds();
        }

        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }


    @Override
    public void onDestroy() {

        messageManager.unregisterOnReceivedPushMessageListener(opl);

        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact contact = data.get(position);
        String name = contact.name;
        String imgHead = contact.imgHead;
        int fan_id = contact.fan_id;
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Constant.CHAT_CONTACT_FAN_ID, fan_id);
        intent.putExtra(Constant.CHAT_CONTACT_HEAD, imgHead);
        intent.putExtra(Constant.CHAT_CONTACT_NAME, name);
        startActivity(intent);
    }

    /**
     * ��̨������Ϣ
     */
    private PushMessageManager.OnReceivedPushMessageListener opl = new PushMessageManager.OnReceivedPushMessageListener() {

        @Override
        public void onReceivedFansMessage(Object message) {
            MyLogger.e(TAG, "���յ�����Ϣ");
            MessageRecordModel pushMessage = (MessageRecordModel) message;
            manageReceiveMessage(pushMessage);
        }
    };

    public void manageReceiveMessage(MessageRecordModel pushMessage) {
        int fan_id = pushMessage.fid;
        FansRecordModel fansRecordModel = ApiFansRecordDb.getFansInfoById(context, fan_id);
        if (fansRecordModel == null) {
            MyLogger.e(TAG, "��˿�б���û�и÷�˿��Ϣ");
            getFansDetail(fan_id);//������ȡ��˿��Ϣ������ӵ����ݿ���
        } else {
            setContactList();
        }
    }

    /**
     * ��ȡ��˿��¼
     * ����˿�� accountId Ϊ -1 ʱ����ʾ������ȡʧ��
     *
     * @param fan_id
     * @return
     */
    private void getFansDetail(final int fan_id) {
        HttpRequestController.getFansInfo(context, fan_id,
                new HttpResponseListener<ApiFans.ApiFansResponse>() {
                    @Override
                    public void onResult(ApiFans.ApiFansResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            MyLogger.e(TAG, "response.fanInfo = " + response.fanInfo);
                            if (response.fanInfo != null) {
                                FansRecordModel fanModel = new FansRecordModel();
                                fanModel.accountId = fan_id;
                                fanModel.eid = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, -1);
                                fanModel.avatar = response.fanInfo.avatar;
                                fanModel.realName = response.fanInfo.realName;
                                fanModel.nickName = response.fanInfo.nickName;
                                fanModel.province = response.fanInfo.province;
                                fanModel.city = response.fanInfo.city;
                                fanModel.followStatus = response.fanInfo.followStatus;
                                fanModel.gender = response.fanInfo.gender;
                                fanModel.interactionTimes = response.fanInfo.interactionTimes;
                                fanModel.lastInteractionTime = response.fanInfo.lastInteractionTime;
                                fanModel.subscribeTime = response.fanInfo.subscribeTime;
                                Uri fansRecordModelUrl = new ApiFansRecordDb(context).insert(fanModel);
                                if (fansRecordModelUrl == null) {
                                    //TODO �÷�˿���벻�ɹ�
                                    MyLogger.e(TAG, "��˿id" + fan_id + "δ��ӵ����ݿ���");
                                }
                                setContactList();
                            }
                        } else {
                            Utils.toast(context, response.getRetInfo() + "");
                        }
                    }
                });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyLogger.e(TAG, "adapter.notify");
                    adapter.notifyDataSetChanged();
                    int count = adapter.getCount();
                    int adapterCount = PreferenceUtils.getInt(context, ADAPTER_COUNT, -1);
                    if (count != adapterCount) {
                        PreferenceUtils.putInt(context, ADAPTER_COUNT, count);
                        updateUserGrade();
                    }
                    listContactList.smoothScrollToPosition(0);
                    setFanIds();
                    if (getActivity() != null) {
                        // ((MainActivity) getActivity()).setMessageNumber();
                    }
                    break;
                case 2:
                    adapter.notifyDataSetChanged();
                    MyLogger.w(TAG, "adapter����1");
                    listContactList.smoothScrollToPosition(0);
                    updateUserGrade();
                    break;
            }
        }
    };

    private void setFanIds() {
        fanIds = null;
        fanIds = new int[data.size()];
        for (int position = 0; position < data.size(); position++) {
            fanIds[position] = data.get(position).fan_id;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        int[] location = new int[2];
        View textLastWord = view.findViewById(R.id.text_item_chat_contact_list_last_word);
        textLastWord.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, x, y);

        TextView delete = (TextView) popView.findViewById(R.id.pop_delete);
        TextView cancel = (TextView) popView.findViewById(R.id.pop_cancel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiFansRecordDb fansDb = new ApiFansRecordDb(context);
                Contact contact = data.get(position);
                int delResult = fansDb.delete(contact.fan_id);
                if (delResult > 0) {
                    Log.w(TAG, "ɾ�� " + contact.fan_id + "�ɹ�");
                    data.remove(position);
                    setFanIds();
                } else {
                    Log.w(TAG, "ɾ�� " + contact.fan_id + " ʧ��");
                }
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                adapter.notifyDataSetChanged();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        return true;
    }

}
