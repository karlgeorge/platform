package com.example.platform;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Created by 电脑配件 on 2018/8/21.
 */

public class ListFragment extends BaseFragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    //private TextView textView;
    private EditText mes;
    private String[] task=new String[100];
    private ListView taskList;
    private ArrayAdapter<String>adapter;
    private int taskNumber=0;
    private Button refresh;
    public static Socket socket;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    private Intent intent;
    private Button build;
    private User user;
    String msg;
    private Handler handler =new Handler(){
        public void handleMessage(Message m){
            switch(m.what){
                case 1:
                    //textView.setText(textView.getText()+msg+"\n");
                    task[taskNumber++]=msg;
                    adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,task);
                    taskList.setAdapter(adapter);

                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.list, container, false);
            initView();


//        实现懒加载
            lazyLoad();
            isPrepared = true;
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
            case R.id.refresh:
                try {
                    new Thread() {
                        public void run() {
                            try {
                                final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                try {
                                    //   System.out.println("Message from server: " + in.readUTF());
                                    out.writeUTF(String.valueOf(mes.getText()));
                                    Log.e("sendMes",String.valueOf(mes.getText()));
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } catch(Exception e2){
                                e2.printStackTrace();
                            }
                        }
                    }.start();
                } catch(Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.build:
                intent=new Intent(getActivity(),TaskPublishActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent,View v,int position,long id){

        String test=task[position];
        Log.e("click list",test);
        Toast.makeText(getActivity(),test,Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        //textView=mView.findViewById(R.id.text);
        refresh= mView.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        mes=mView.findViewById(R.id.mes);
        taskList=mView.findViewById(R.id.task_list);
        //task[0]="aer";
        for(int i=0;i<100;++i){
            task[i]="";
        }
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,task);
        taskList.setAdapter(adapter);
        taskList.setOnItemClickListener(this);
        build=mView.findViewById(R.id.build);
        build.setOnClickListener(this);
        user=((MainActivity)getActivity()).user;
        //Toast.makeText(getActivity(),user.getName(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void lazyLoad() {
        if (mHasLoadedOnce) {
            Log.e("lazyload","return");
            return;
        }
        //填充各控件的数据
        try {
            new Thread() {
                public void run() {
                    try {
                        socket = new Socket("192.168.1.102", 9050);
                        out=new ObjectOutputStream(socket.getOutputStream());
                        in=new ObjectInputStream(socket.getInputStream());
                        out.writeObject(user);
                        out.flush();
                      //  sleep(1000);
                        //out.close();

                       // Oout= new ObjectOutputStream(socket.getOutputStream());
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
                        while (true) {
                            try {
                                //   System.out.println("Message from server: " + in.readUTF());
                                msg = in.readUTF();
                                Log.e("msg", msg);
                                Message m = new Message();
                                m.what = 1;
                                handler.sendMessage(m);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } catch(Exception e2){
                        e2.printStackTrace();
                    }
                }
            }.start();
        } catch(Exception e){
            e.printStackTrace();
        }
        mHasLoadedOnce = true;
    }
    public static ListFragment newInstance(String param1) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

}
