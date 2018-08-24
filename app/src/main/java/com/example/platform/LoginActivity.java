package com.example.platform;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText name;
    EditText password;
    Intent intent;
    User user=new User();
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String mysql_location="cd-cdb-nzezo9vq.sql.tencentcdb.com:63786";
    static final String mysql_location="202.182.102.228:3306";
    static final String database_name="platform";
    static final String DB_URL = "jdbc:mysql://"+mysql_location+"/"+database_name+"?serverTimezone=GMT%2B8";
    static final String USER = "root";
    static final String PASS = "mysql_1221";
    static Connection conn = null;
    static Statement stmt = null;
    static String sql=null;
    static Message message;
    Toast loginM;
    Toast registerM;
    Toast registered;

    Toast error;
    private Handler handler =new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case 2:
                    loginM.show();
                    break;
                case 3:
                    error.show();
                    break;
                case 4:
                    registerM.show();
                    break;
                case 5:
                    error.show();
                    break;
                case 6:
                    registered.show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        Button submit=(Button)findViewById(R.id.submit);
        Button register=(Button)findViewById(R.id.register);
        submit.setOnClickListener(this);
        register.setOnClickListener(this);
        if(!isConnectInternet()){
            Toast.makeText(this,"无网络连接",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick(View v){
        final String Name=name.getText().toString();
        final String Password=password.getText().toString();
        intent=new Intent(LoginActivity.this,MainActivity.class);
        if(Name.equals(null)||Password.equals(null)||Name.equals("")||Password.equals("")){
            Toast.makeText(this,"请输入用户名和密码",Toast.LENGTH_SHORT).show();
            return;
        }
        /*

        CREATE TABLE user(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    password VARCHAR(128) NOT NULL,
    PRIMARY KEY ( id )
    );
         */
        loginM=Toast.makeText(this,"用户名或密码错误",Toast.LENGTH_SHORT);
        error=Toast.makeText(this,"请检查网络连接",Toast.LENGTH_SHORT);
        registerM=Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT);
        registered=Toast.makeText(this,"该用户名已被注册",Toast.LENGTH_SHORT);
        user.setName(Name);
        user.setPassword(Password);
        switch(v.getId()) {
            case R.id.submit:
                new Thread() {
                    public void run() {

                        message=new Message();
                        message.what=1;
                        /*
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            conn = DriverManager.getConnection(DB_URL, USER, PASS);
                            stmt = conn.createStatement();
                            sql = "select * from user where name=\'" + name.getText() + "\'";
                            ResultSet rs = stmt.executeQuery(sql);
                            String password = null;
                            if (rs.next()) {
                                password = rs.getString("password");
                            }
                            if (password!=null&&!password.equals("")&&password.equals(Password)) {
                                message.what=1;
                            } else{
                                message.what=2;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            message.what=3;
                        }
                        */
                        handler.sendMessage(message);
                    }
                }.start();
                break;
            case R.id.register:
                new Thread() {
                    Message msg=new Message();
                    public void run() {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            conn = DriverManager.getConnection(DB_URL, USER, PASS);
                            stmt = conn.createStatement();
                            sql = "select * from user where name=\'" + name.getText() + "\'";
                            ResultSet rs= stmt.executeQuery(sql);
                            if(!rs.next()){
                                sql="insert into user values(null,\'"+user.getName()+"\',\'"+user.getPassword()+"\')";
                                stmt.execute(sql);
                                msg.what=4;
                            }
                            else{
                                msg.what=6;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            msg.what=5;
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
                break;
        }
    }
    public boolean isConnectInternet() {
        ConnectivityManager conManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null ){
            return networkInfo.isAvailable();
        }
        return false ;
    }
}
