package com.huntor.mscrm.app.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiFansModify;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.Utils;

public class ShowListDialog {
	
	String TAG="ShowListDialog";
	public static String FILENAME = "fileName";
	 //   public static String FILEPATH = "filePath";

	private int selectedItem = -1;
	
	public Context context;


    public String mStrPreview=null;//选中的返回结果

    private ModifyFansParam modifyFansParam;
	/**
	 * 
	 * @param listFilePath  ArrayList(Hashtable<String,String>)
	 * @param context   Context
	 * @param mString   性别  gender  or  职业  IT ==
	 */
	public void showSudokuListDialog(final ArrayList<Hashtable<String, String>> listFilePath, final Context context,final String mString,final TextView mTxview,final int accountId,final int fans_id) {

		this.context=context;
        modifyFansParam=new ModifyFansParam();

        if (listFilePath == null || listFilePath.size() == 0) {
            Log.i("TAG","null:");
            return ;
        }
        selectedItem = -1;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layout = LayoutInflater.from(context);
        View sudokulistView = layout.inflate(R.layout.layout_sudokulist, null);
        builder.setView(sudokulistView);
        builder.setCancelable(false);

        final TextView tvSudokuPreview = (TextView) sudokulistView
                .findViewById(R.id.tvSudokuPreview);

        tvSudokuPreview.setText(mString);
        
        final ListView lvSudokuItems = (ListView) sudokulistView
                .findViewById(R.id.lvSudokuItems);
        final SimpleAdapter adapter = new SimpleAdapter(context,
                listFilePath, R.layout.layout_filelist, new String[] { FILENAME },
                new int[] { R.id.tvSudokuItem });
        // set list adapter
        lvSudokuItems.setAdapter(adapter);
        // TODO
        // set list click event
        lvSudokuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedItem = position;
                String strPreview = ((Hashtable<String, String>) adapter
                        .getItem(position)).get(FILENAME);

                tvSudokuPreview.setText(mString + ":" + strPreview);

            }
        });

        // set list touch event
        lvSudokuItems.setOnTouchListener(new AdapterView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });        


        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Field field = dialog.getClass().getSuperclass()
                            .getDeclaredField("mShowing");
                    field.setAccessible(true);
                    // set false?
                    field.setBoolean(dialog, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (selectedItem == -1) {
                    showToast("没选中任何一项");
                    return;
                } else {

                    try {
                        Field field = dialog.getClass().getSuperclass()
                                .getDeclaredField("mShowing");
                        field.setAccessible(true);
                        // set false?
                        field.setBoolean(dialog, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String key = ((Hashtable<String, String>) adapter
                            .getItem(selectedItem)).get(FILENAME);
                    if("其他".equals(key)){
                        goInput("job",context,mTxview,0,fans_id);
                    }else{

                        if("性别".equals(mString)){//选择性别
                            if("男".equals(key)){
                                modifyFansParam.gender=1;

                            }else if("女".equals(key)){
                                modifyFansParam.gender=2;
                            }else{
                                modifyFansParam.gender=3;
                            }
                        }else if("职业".equals(mString)){//选择职业
                            modifyFansParam.occupation=key;
                        }else if("年龄".equals(mString)){//选择年龄
                            //modifyFansParam.age=key;
                        }
                        modifyFansParam.accountId=accountId;
                        modifyFansParam.fanId=fans_id;
                        if(accountId!=0&&modifyFansParam!=null){
                            MyLogger.i(TAG,"accountId  =:"+accountId );
                            MyLogger.i(TAG,"modifyFansParam.gender =:"+modifyFansParam.gender);
                            MyLogger.i(TAG,"modifyFansParam.occupation =:"+modifyFansParam.occupation);

                            postCompile(modifyFansParam,context);
                        }else{
                            MyLogger.i(TAG,"modifyFansParam为null");
                        }

                        mTxview.setText(key);
                        dialog.dismiss();
                    }

                    //showToast(key);

                }
            }
        });

        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            Field field = dialog.getClass().getSuperclass()
                                    .getDeclaredField("mShowing");
                            field.setAccessible(true);
                            // set true
                            field.setBoolean(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
        builder.create().show();


    }


    public void showToast(String message) {
        this.mStrPreview=message;
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //向服务器提交更改的数据
    public void postCompile(ModifyFansParam mModifyFansParam,Context context){
        MyLogger.i(TAG, "postCompile");
        HttpRequestController.modifyFans(context, mModifyFansParam, new HttpResponseListener<ApiFansModify.ApiFansModifyResponse>() {
            @Override
            public void onResult(ApiFansModify.ApiFansModifyResponse response) {
                if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                    MyLogger.i(TAG, response.toString() + "ok");

                } else if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR) {
                    MyLogger.i(TAG, response.toString() + "error");
                }

            }
        });
    }



    public void goInput(String str,Context context,TextView textView,int accountId,int fans_id){
        accountInfoCompile(str, context, textView, accountId,fans_id);//传accountId
    }


    public void JobInfoCompile(final Context context,final TextView textView,final ModifyFansParam modifyFansParam,final int accountId,final int fans_id){
        this.context=context;

        LayoutInflater infalter = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = infalter.inflate(R.layout.set_group_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setView(view, 0, 0, 0, 0);
        if(dialog!=null){
            dialog.show();
        }

        TextView compile_name = (TextView) view.findViewById(R.id.tv_title);
        final EditText et_group_name = (EditText) view.findViewById(R.id.et_group_name);
        compile_name.setText("职业");
        et_group_name.setHint("请输入职业");

        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);



        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = et_group_name.getText().toString().trim();

                MyLogger.i("TAG", groupName);
                if (TextUtils.isEmpty(groupName)) {
                    Utils.toast(context, "不能为空!");
                } else {
                    textView.setText(groupName);
                    modifyFansParam.accountId = accountId;
                    modifyFansParam.fanId=fans_id;
                    if (accountId != 0 && modifyFansParam != null) {
                        postCompile(modifyFansParam,context);
                    }
                    dialog.dismiss();
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

    private void accountInfoCompile(final String title,final Context context,final TextView textView, final int accountId,final int fans_id) {
        this.context=context;
        modifyFansParam=new ModifyFansParam();
        LayoutInflater infalter = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = infalter.inflate(R.layout.set_group_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);//响应返回键
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
        //TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        final EditText et_group_name = (EditText) view.findViewById(R.id.et_group_name);

        //判断点击的是哪个编辑按钮
        final TextView compile_name= (TextView) view.findViewById(R.id.tv_title);
        if(title.equals("name")){
            compile_name.setText("姓名");
            et_group_name.setHint("请输入姓名");
        }
        if(title.equals("job")){
            compile_name.setText("职业");
            et_group_name.setHint("请输入职业");

        }
        if(title.equals("tel")){
            compile_name.setText("电话");
            et_group_name.setHint("请输入手机号");
        }

        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);



        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = et_group_name.getText().toString().trim();

                MyLogger.i("TAG",groupName);
                if (TextUtils.isEmpty(groupName)) {
                    Utils.toast(context, "不能为空!");
                } else {
                    if(title.equals("tel")){//编辑电话
                        boolean isMobile=isMobileNO(groupName);
                        MyLogger.i("TAG","isMobile:"+isMobile);
                        if(isMobile==true){
                            textView.setText(groupName);
                            modifyFansParam.phone1=groupName;
                            MyLogger.i(TAG, "modifyFansParam.phone1 =:" + modifyFansParam.phone1);
                        }else{
                            Utils.toast(context,"输入的手机号码有误！");
                            et_group_name.getText().clear();
                        }
                    }else if(title.equals("name")){//编辑姓名
                        textView.setText(groupName);
                        modifyFansParam.name=groupName;
                    } else{//编辑职业
                        textView.setText(groupName);
                        modifyFansParam.occupation=groupName;
                        MyLogger.i(TAG, "modifyFansParam.occupation 其他=:" + modifyFansParam.occupation);

                    }
                    modifyFansParam.accountId=accountId;
                    modifyFansParam.fanId=fans_id;
                    if(accountId!=0&&modifyFansParam!=null) {
                        postCompile(modifyFansParam,context);
                    }
                    dialog.dismiss();
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

    //判断输入的是否为手机号
    public boolean isMobileNO(String mobileNo){

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobileNo);
        return m.matches();
    }

    public ArrayList<Hashtable<String, String>> GetCity(String[] str) {
        ArrayList<Hashtable<String, String>> listData = new ArrayList<Hashtable<String, String>>();
        Hashtable<String, String> hmItem = new Hashtable<String, String>();
        
        for (int i = 0; i < str.length; i++) {
        	hmItem.put(FILENAME, str[i]);
            listData.add(hmItem);
            hmItem = new Hashtable<String, String>();
		}
        
        return listData;
    }
	
}
