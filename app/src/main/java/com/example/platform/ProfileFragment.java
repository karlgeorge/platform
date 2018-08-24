package com.example.platform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/*
    made by met 2018.8.24
 */

public class ProfileFragment extends BaseFragment implements View.OnClickListener{
    private boolean isPrepared;
    private TextView userID;
    private EditText userName;
    private TextView Level;
    private boolean mHasLoadedOnce;
    private TextView ReceiveTask;
    private TextView PublishTask;
    public User user;

    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.profile, container, false);
            initView();
            isPrepared = true;
//        实现懒加载
            lazyLoad();
        }
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }
    /**
     * 初始化控件
     */
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.all:
                try {

                }catch(Exception e){

                }
        }
    }

    private void initView() {
        userID=mView.findViewById(R.id.userID);
        user=((MainActivity)getActivity()).user;
        userID.setText("账户ID："+user.getName());
        Level=mView.findViewById(R.id.level);
        Level.setText("积分："+String.valueOf(user.getAccount()));

    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
        mHasLoadedOnce = true;
    }
    public static ProfileFragment newInstance(String param1) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

}