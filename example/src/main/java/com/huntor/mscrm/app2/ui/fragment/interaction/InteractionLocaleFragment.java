package com.huntor.mscrm.app2.ui.fragment.interaction;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.huntor.mscrm.app2.R;


import com.huntor.mscrm.app2.myZXing.CaptureActivity;
import com.huntor.mscrm.app2.push.PushMessageManager;
import com.huntor.mscrm.app2.ui.MainActivity2;

import com.huntor.mscrm.app2.ui.ShackActivity;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.fragment.SearchFragment;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.ui.fragment.member.MyGroupFragment;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.ui.InteractionLocaleActivity;
import com.huntor.mscrm.app2.utils.Utils;

/**
 * 现场互动的fragment
 */
public class InteractionLocaleFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;

    TextView shake_count;
    PushMessageManager pushMessageManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.dismissCustomDialog();
        }
        View ret = inflater.inflate(R.layout.fragment_interactive, container, false);
        LinearLayout layoutBuyEnter = (LinearLayout) ret.findViewById(R.id.layout_fragment_interaction_buy_enter);
        LinearLayout layoutEnterInvitation = (LinearLayout) ret.findViewById(R.id.layout_fragment_interaction_enter_invitation);
        LinearLayout layoutEnterShake = (LinearLayout) ret.findViewById(R.id.layout_fragment_interaction_enter_shake);
        //ImageView iv_search = (ImageView) ret.findViewById(R.id.iv_search);

        layoutBuyEnter.setOnClickListener(this);
        layoutEnterInvitation.setOnClickListener(this);
        layoutEnterShake.setOnClickListener(this);

        shake_count = (TextView)ret.findViewById(R.id.shake_count);

        if(Constant.shakecount > 0){
            shake_count.setText(String.valueOf(Constant.shakecount));
        }
        //iv_search.setOnClickListener(this);

//        ret.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MainActivity2 ma = ((MainActivity2) getActivity());
//                if (ma != null) {
//                    //ma.menu();
//                }
//            }
//        });

        fragmentManager = getFragmentManager();
        return ret;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pushMessageManager = PushMessageManager.getInstance(getActivity());
        pushMessageManager.registerOnReceivedPushMessageListener(shakeListener);

    }

    private PushMessageManager.OnPullMessageShakeListener shakeListener = new PushMessageManager.OnPullMessageShakeListener() {
        @Override
        public void OnPullMessageShake(Object pushMessage) {


            Log.e("infragment"  ,"收到摇一摇消息");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(Constant.shakecount > 0){
                        shake_count.setText(String.valueOf(Constant.shakecount));
                    }
                }
            });

        }
    };

    @Override
    public void onResume() {
        super.onResume();


        if(Constant.shakecount > 0){
            shake_count.setText(String.valueOf(Constant.shakecount));
        }else{
            shake_count.setText("");
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        pushMessageManager.unregisterOnReceivedPushMessageListener(shakeListener);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (id){
            case R.id.layout_fragment_interaction_buy_enter:
                intent = new Intent(getActivity(), CaptureActivity.class);		//CaptureActivity是扫描的Activity类
                //startActivityForResult(intent, 0);							//当前扫描完条码或二维码后,会回调当前类的onActivityResult方法,
                startActivity(intent);
                break;
            case R.id.layout_fragment_interaction_enter_invitation:
                intent = new Intent(getActivity(), InteractionLocaleActivity.class);
                intent.putExtra(Constant.INTERACTION_INTENT_DATA, Constant.ENTER_INVITATION);
                startActivity(intent);
                break;
            case R.id.layout_fragment_interaction_enter_shake:
                intent = new Intent(getActivity(), ShackActivity.class);
                intent.putExtra(Constant.INTERACTION_INTENT_DATA, Constant.ENTER_INVITATION);
                startActivity(intent);
                break;
//            case R.id.iv_search:
//                transaction.addToBackStack(Constant.SEARCH);
//                transaction.add(R.id.frame_main, new SearchFragment(), Constant.SEARCH);
//                break;
        }
        transaction.commitAllowingStateLoss();
    }



    Handler handler = new Handler();
}
