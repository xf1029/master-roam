package com.a51tgt.t6.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.a51tgt.t6.MZApplication;
import com.a51tgt.t6.R;
import com.a51tgt.t6.abstract_face.OnNoticeUI;
import com.a51tgt.t6.comm.APIConstants;

public class SIMCardActivity extends BaseActivity implements View.OnClickListener,OnNoticeUI{
    private ToggleButton autoBtn;
    private LinearLayout ll_manually_set_card;
    private ImageView iv_set_dev_real_sim, iv_set_virtual;
    private DeviceSetDialog setDialog = new DeviceSetDialog();
    String mode, TAG = "SIMCardActivity";
    boolean auto;
    boolean isSelect;

    private void setButtonStateAndVariables(){
        int mode_num = APIConstants.deviceInfo.getRealModem();

        if(mode_num == 2){
            //自动
            iv_set_virtual.setImageResource(R.mipmap.gray);
            iv_set_dev_real_sim.setImageResource(R.mipmap.gray);
            mode = "auto";
            auto = true;
            ll_manually_set_card.setVisibility(View.GONE);
            autoBtn.setChecked(true);
        } else if(mode_num == 1){
            //SIM卡
            autoBtn.setChecked(false);
            ll_manually_set_card.setVisibility(View.VISIBLE);
            auto = false;
            mode = "real";
            iv_set_virtual.setImageResource(R.mipmap.gray);
            iv_set_dev_real_sim.setImageResource(R.mipmap.red);
        } else{
            //虚卡
            autoBtn.setChecked(false);
            ll_manually_set_card.setVisibility(View.VISIBLE);
            auto = false;
            mode = "virtual";
            iv_set_virtual.setImageResource(R.mipmap.red);
            iv_set_dev_real_sim.setImageResource(R.mipmap.gray);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simcard_layout);
        findViewById(R.id.iv_back2).setOnClickListener(this);
        ll_manually_set_card = findViewById(R.id.ll_manually_set_card);
        iv_set_virtual = findViewById(R.id.iv_set_virtual);
        iv_set_dev_real_sim = findViewById(R.id.iv_set_dev_real_sim);
        autoBtn = findViewById(R.id.tb_sim_card_auto);

        setButtonStateAndVariables();

        MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_SET_SIMCARD );

        autoBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ll_manually_set_card.setVisibility(View.GONE);
                    mode = "auto";
                    isSelect = false;
                    auto = true;
                    saveSetting();
                }
                else {
                    ll_manually_set_card.setVisibility(View.VISIBLE);
                    iv_set_virtual.setImageResource(R.mipmap.gray);
                    iv_set_dev_real_sim.setImageResource(R.mipmap.gray);
                    mode = "";
                    auto = false;
                }
            }
        });


    }

    public void saveSetting(){
        if (auto){
            APIConstants.newMode = 2;
            setDialog.setCardMode(2);
        }else{
            if (mode.equals("virtual")){
                APIConstants.newMode = 0;
                setDialog.setCardMode(0);
            }else if(mode.equals("real")){
                APIConstants.newMode = 1;
                setDialog.setCardMode(1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back2:

                if (!auto&&isSelect){
                saveSetting();
                }
                finish();

                break;
            case R.id.iv_set_dev_real_sim:
                iv_set_virtual.setImageResource(R.mipmap.gray);
                iv_set_dev_real_sim.setImageResource(R.mipmap.red);
                mode = "real";
                isSelect = true;
                auto = false;
                break;
            case R.id.iv_set_virtual:
                iv_set_dev_real_sim.setImageResource(R.mipmap.gray);
                iv_set_virtual.setImageResource(R.mipmap.red);
                mode = "virtual";
                isSelect= true;
                auto = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {

        switch (NOTICE_TYPE){
            //省流量模式
            case 46:
//                Log.i("xiugaimima",object.toString());
                APIConstants.deviceInfo.setRealModem(APIConstants.newMode);
                Toast.makeText(this,object.toString().equals("true")? R.string.title_restart_device:R.string.tip_operation_failed,Toast.LENGTH_SHORT).show();;
                break;
        }
    }
}
