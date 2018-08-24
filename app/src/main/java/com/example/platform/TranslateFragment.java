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

/**
 * Created by 电脑配件 on 2018/8/21.
 */

public class TranslateFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{

    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private EditText ori;
    private EditText tra;
    private Button translate;
    private TranslateCallback tr;
    private RadioGroup lau;
    private String res="en";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.translate, container, false);
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
            case R.id.translate:
                try {
                    if(ori!=null&&!ori.equals(""))
                        new TranslateUtil().translate(getActivity(),"auto",res,String.valueOf(ori.getText()),tr);
                }catch(Exception e){

                }
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId){
        switch(checkedId){
            case R.id.chinese:
                res="zh-cn";
                break;
            case R.id.english:
                res="en";
                break;
            case R.id.japanese:
                res="ja";
                break;
            case R.id.germany:
                res="de";
                break;
        }
        new TranslateUtil().translate(getActivity(),"auto",res,String.valueOf(ori.getText()),tr);
    }

    private void initView() {
        ori=mView.findViewById(R.id.ori);
        tra=mView.findViewById(R.id.tra);
        translate=mView.findViewById(R.id.translate);
        translate.setOnClickListener(this);
        lau=mView.findViewById(R.id.lau);
        lau.setOnCheckedChangeListener(this);
        tr=new TranslateCallback() {
            @Override
            public void onTranslateDone(String result) {
                tra.setText(result);
            }
        };

    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
        mHasLoadedOnce = true;
    }
    public static TranslateFragment newInstance(String param1) {
        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
}
