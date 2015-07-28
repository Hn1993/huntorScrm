package com.huntor.scrm.ui.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huntor.scrm.R;
import com.huntor.scrm.myZXing.CaptureActivity;
import com.huntor.scrm.ui.Fragment.onlineInteraction.InvitationFragment;
import com.huntor.scrm.ui.Fragment.onlineInteraction.buyInitiationFragment;


/**
 * Created by Admin on 2015/7/16.
 */
public class InteractionFragment extends BaseFragment implements View.OnClickListener{
    protected FragmentManager fragmentManager;
    private ImageView mImageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_interactive, container, false);
        fragmentManager = getFragmentManager();
        findViews(ret);
        return ret;
    }

    private void findViews(View view) {
        view.findViewById(R.id.membership_invation).setOnClickListener(this);
        view.findViewById(R.id.buy_membership).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        switch (view.getId()){
            case R.id.buy_membership:
                //transaction.replace(R.id.fl_content,new buyInitiationFragment());
                Intent intent=new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.membership_invation:
                transaction.replace(R.id.fl_content,new InvitationFragment());
                break;
        }
        transaction.commit();
    }
}
