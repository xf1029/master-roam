package com.a51tgt.t6.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.FlowProductInfo;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.ui.view.NumberButton;
import com.a51tgt.t6.ui.view.ObservableScrollView;

public class DetailActivity extends BaseActivity implements View.OnClickListener,  ObservableScrollView.OnObservableScrollViewListener {


    private ObservableScrollView mObservableScrollView;
    private LinearLayout mHeaderContent,purchaseCountLayout;
    private ImageView mTextView,mBack;
    private TextView tv_packageName,tv_price,tv_total_flow,tv_use_day,tv_country,tv_notice,tv_total_price;
    private Button btn_buy;
    private  View more;
//产品信息
    private String url, title, price, price_type, payType, coverage,
            total_flow, effective_days, notice, product_name, coverImage,
            productnumber, activedays, producttype, product_id;

    private int mHeight;
    private NumberButton numberButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        findViewById(R.id.bt_commit).setOnClickListener(this);

//取值

        product_id = getIntent().getStringExtra("product_id");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        price_type = getIntent().getStringExtra("price_type");
        activedays = getIntent().getStringExtra("activedays");

        coverage = getIntent().getStringExtra("coverage");
        total_flow = getIntent().getStringExtra("total_flow");
        if (total_flow == null || total_flow.isEmpty() || total_flow.equals("unlimited")){
            total_flow = getResources().getString(R.string.tag_unlimited);
        }
        effective_days = getIntent().getStringExtra("effective_days");
        notice = getIntent().getStringExtra("notice");
        productnumber = getIntent().getStringExtra("productnumber");
        activedays = getIntent().getStringExtra("activedays");
        producttype = getIntent().getStringExtra("producttype");





        //初始化控件
        mObservableScrollView = (ObservableScrollView) findViewById(R.id.sv_container);
        mTextView = (ImageView) findViewById(R.id.iv_bg);

        mHeaderContent = (LinearLayout) findViewById(R.id.ll_header_content);
        mBack = findViewById(R.id.iv_header_left);
        tv_packageName = findViewById(R.id.tv_package_name) ;
        tv_price = findViewById(R.id.tv_price_currency);
        tv_total_flow = findViewById(R.id.tv_total_flow);
        tv_use_day = findViewById(R.id.tv_user_day);
        tv_country = findViewById(R.id.tv_coverage);
        tv_total_price = findViewById(R.id.tv_price_total);
        btn_buy = findViewById(R.id.bt_commit);
        purchaseCountLayout = findViewById(R.id.ll_buy_more);
        numberButton = findViewById(R.id.number_button);
        more = findViewById(R.id.view_buy_more);




        if (producttype.equals("DAILY")&&effective_days.equals("1")){

            purchaseCountLayout.setVisibility(View.VISIBLE);
            more.setVisibility(View.VISIBLE);
            numberButton.setBuyMax(Integer.parseInt(activedays));
            numberButton.setOnWarnListener(new NumberButton.OnWarnListener() {
                @Override
                public void onWarningForInventory(int inventory) {
                    ImageView add= numberButton.findViewById(R.id.button_add);
//                    add.setImageDrawable(getResources().getDrawable(R.mipmap.icon_max));
                }

                @Override
                public void onWarningForBuyMax(int max) {

                    ImageView add= numberButton.findViewById(R.id.button_add);
//                    add.setImageDrawable(getResources().getDrawable(R.mipmap.icon_max));

//                 add.setColorFilter(Color.GRAY);
                    String string = getString(R.string.title_buy_warn)+max+getString(R.string.tilte_number);


                    Toast.makeText(DetailActivity.this,string,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void numberAdd(int number) {

                    Float actual_price = Float.valueOf(price)*number;
                    tv_total_price.setText(price_type+" "+String.valueOf(actual_price));
//                    Toast.makeText(FlowProductDetailActivity.this,"数量增加",Toast.LENGTH_SHORT).show();


                }

                @Override
                public void numberSub(int number) {
                    ImageView add= numberButton.findViewById(R.id.button_add);
//                    add.setColorFilter(Color.WHITE);


//                    add.setImageDrawable(getResources().getDrawable(R.mipmap.increase_eleme));
//                    Toast.makeText(FlowProductDetailActivity.this,"数量减少",Toast.LENGTH_SHORT).show();

                }
            });

        }

        tv_packageName.setText(title);
        tv_price.setText(price_type+" "+price);
        tv_use_day.setText(effective_days);
        tv_total_flow.setText(total_flow);
        tv_country.setText(coverage);
        tv_total_price.setText(price_type+" "+price);

//        tv_notice.setText(notice);



        mBack.setOnClickListener(this);

        //获取标题栏高度
        ViewTreeObserver viewTreeObserver = mTextView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHeight = mTextView.getHeight() - mHeaderContent.getHeight();//这里取的高度应该为图片的高度-标题栏
                //注册滑动监听
                mObservableScrollView.setOnObservableScrollViewListener(DetailActivity.this);
            }
        });



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_commit:

                Intent intent = new Intent(this,SubmitOrderActivity.class);
                intent.putExtra("product_id", product_id);
                intent.putExtra("price", tv_total_price.getText());
                intent.putExtra("product_count", numberButton.getNumber());
                intent.putExtra("effective_days", effective_days);
                intent.putExtra("price_type", price_type);
                intent.putExtra("title", title);
                startActivity(intent);


                break;
            case R.id.iv_header_left:
                finish();
                default:
                    break;
        }

    }



    @Override
    public void onObservableScrollViewListener(int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //顶部图处于最顶部，标题栏透明
            mHeaderContent.setBackgroundColor(Color.argb(0, 48, 63, 159));
        } else if (t > 0 && t < mHeight) {
            //滑动过程中，渐变
            float scale = (float) t / mHeight;//算出滑动距离比例
            float alpha = (255 * scale);//得到透明度
            mHeaderContent.setBackgroundColor( getResources().getColor(R.color.baseColorLight));

            mHeaderContent.setBackgroundColor(Color.argb((int) alpha, 16, 181, 255));
        } else {
            //过顶部图区域，标题栏定色
//            mHeaderContent.setBackgroundColor( getResources().getColor(R.color.baseColorLight));
            mHeaderContent.setBackgroundColor(Color.argb(255, 16, 181, 255));
        }
    }
}
