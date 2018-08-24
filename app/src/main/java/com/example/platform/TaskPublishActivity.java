package com.example.platform;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;

public class TaskPublishActivity extends AppCompatActivity implements View.OnClickListener{

    private Button submit;
    private Socket socket= ListFragment.socket;
    private Activity activity;
    private Button btn2;
    private User user;
    BasicTask BT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_publish);
        btn2=(Button) findViewById(R.id.release_task);
        submit = findViewById(R.id.submit_task);
        submit.setOnClickListener(this);
        activity=this;
        user=(User)getIntent().getSerializableExtra("user");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BT=new BasicTask();
                BT.setPublisher(user);
                EditText editText1 =(EditText)findViewById(R.id.language);
                String str1=editText1.getText().toString();
                BT.setLanguage(str1);
                EditText editText2 =(EditText)findViewById(R.id.style);
                String str2=editText2.getText().toString();
                BT.setStyle(str2);
                EditText editText3 =(EditText)findViewById(R.id.form);
                String str3=editText3.getText().toString();
                BT.setClassify(str3);
                EditText editText4 =(EditText)findViewById(R.id.wordNumbers);
                int str4=Integer.parseInt(editText4.getText().toString());
                BT.setWordNumbers(str4);
                EditText editText5 =(EditText)findViewById(R.id.reward);
                String str5=editText5.getText().toString();
                BT.setReward(Double.parseDouble(str5));
                EditText editText6 =(EditText)findViewById(R.id.level);
                String str6=editText6.getText().toString();
                BT.setLevel(Integer.parseInt(str6));
                EditText editText7 =(EditText)findViewById(R.id.ddl);
                String str7=editText7.getText().toString();
                BT.setDeadline(str7);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                BT.setDate(year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
                Log.e("12",BT.toString());
                Toast.makeText(activity,"请选择上传文件",Toast.LENGTH_LONG).show();
                submit.performClick();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_task:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(intent.createChooser(intent, "选择文件"), 1);
                } catch (Exception e) {
                    Toast.makeText(this, "没有文件管理器", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.release_task:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                final Uri uri = data.getData();
                Toast.makeText(this, "文件路径："+uri.getPath().toString(), Toast.LENGTH_SHORT).show();
                String path=getFileAbsolutePath(activity,uri);
                try {
                   // final

                    new Thread() {
                        public void run() {
                            try {
                  //              socket = new Socket("192.168.1.103", 9050);
                        //        ObjectOutputStream Oout = new ObjectOutputStream(socket.getOutputStream());
//                                final DataInputStream in = new DataInputStream(socket.getInputStream());
//                                final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                                final ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//                                final ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                                ObjectOutputStream out=ListFragment.out;
                                ObjectInputStream in=ListFragment.in;
                                String path=getFileAbsolutePath(activity,uri);
                       //         Log.e("path",path);
                                File file=new File(path);
                                FileInputStream fis=new FileInputStream(file);
                                byte[] bytes=new byte[1024];
                                int length=0;
                                long fileLength=0;
                                while((length=fis.read(bytes,0,bytes.length))!=-1){
                                    ++fileLength;
                                }
                                Log.e("fileLength",String.valueOf(fileLength));
                                fis=new FileInputStream(file);
                                out.writeUTF(path);

                                out.writeLong(fileLength);
                                long progress=0;
                                while(progress<fileLength&&(length=fis.read(bytes,0,bytes.length))!=-1){
                                    out.write(bytes,0,length);
                                    out.flush();
                                    progress++;
                                }
                                out.flush();
                                //out.close();
                                Log.e("传递文件","结束");
                                out.writeObject(BT);
                                out.flush();

                             //   Oout.close();
                                Log.e("传递对象","结束");
                            } catch (Exception e2) {
                                Log.e("传递","发生异常");
                                e2.printStackTrace();
                            }
                        }
                    }.start();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static String getFileAbsolutePath(Activity context, Uri fileUri) {
        if (context == null || fileUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, fileUri)) {
            if (isExternalStorageDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(fileUri)) {
                String id = DocumentsContract.getDocumentId(fileUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(fileUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(fileUri))
                return fileUri.getLastPathSegment();
            return getDataColumn(context, fileUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(fileUri.getScheme())) {
            return fileUri.getPath();
        }
        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
