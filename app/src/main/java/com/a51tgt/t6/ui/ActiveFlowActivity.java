package com.a51tgt.t6.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t6.MZApplication;
import com.a51tgt.t6.R;
import com.a51tgt.t6.abstract_face.OnNoticeUI;
import com.a51tgt.t6.comm.TcpConfig;

public class ActiveFlowActivity extends BaseActivity implements View.OnClickListener, OnNoticeUI{
//    private Button setDeaultPwdBtn;
    private TextView reductionBtn;
   private  ImageView   iv_back;

    private DeviceSetDialog setDialog = new DeviceSetDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activeflow_layout);


//        setDeaultPwdBtn = (Button)findViewById(R.id.setDefaultPwd);
        reductionBtn = findViewById(R.id.reduction);
        iv_back = (ImageView)findViewById(R.id.iv_back);
//        setDeaultPwdBtn.setOnClickListener(this);
        reductionBtn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
//        MZApplication.getInstance().addActivity(this);
        MZApplication.getInstance().setOnNoticeUiListener( this, OnNoticeUI.KEY_TYPE_DEVICE_REDUCTION);

//        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){

    case R.id.iv_back:
    finish();
    break;
    case R.id.reduction:
        showDialog(R.string.title_active_flowdes,TcpConfig.BT_REDUCTION);

        break;
        default:
            break;
//    case R.id.setDefaultPwd:
//        showDialog(R.string.tile_restore_pwtip,TcpConfig.SET_WIFIANDPASSWORD);
//        break;

}

    }


    private  void showDialog(int message, final String command){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton(getResources().getText(R.string.button_cancel),null);
        builder.setPositiveButton(getResources().getText(R.string.button_commit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDialog.setDefaultPwd(command);
                Toast.makeText(ActiveFlowActivity.this,R.string.tip_operation_success,Toast.LENGTH_LONG).show();



            }
        });

          builder.show();


    }


    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {
        switch (NOTICE_TYPE){
            //恢复默认密码
            case 38:
                Log.i("xiugaimima",object.toString());
                Toast.makeText(this,object.toString().equals("true")? R.string.tip_operation_success:R.string.tip_operation_failed,Toast.LENGTH_LONG).show();
            break;
            case 42://还原出厂设置

                Toast.makeText(this,object.toString().equals("true")? R.string.tip_operation_success:R.string.tip_operation_failed,Toast.LENGTH_LONG).show();
             break;
        }
    }
}
