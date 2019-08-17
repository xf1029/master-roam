package com.a51tgt.t6.ui;

import android.os.Bundle;
import android.view.View;

import com.a51tgt.t6.R;

public class SubmitOrderActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_commit:


                break;
                default:
                    break;
        }

    }
}
