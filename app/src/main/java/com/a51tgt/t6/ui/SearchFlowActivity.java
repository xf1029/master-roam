package com.a51tgt.t6.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t6.R;
import com.a51tgt.t6.adapter.RecyclerviewAdapter;
import com.a51tgt.t6.utils.SPUtils;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页面
 */
public class SearchFlowActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 搜索历史
     */
    RecyclerView rv_search_flow;
    RecyclerviewAdapter recyclerviewAdapter;
    /**
     * 搜索框
     */
    EditText et_search_flow;


    /**
     * 搜索历史集合
     */
    List<String> stringList = new ArrayList<>();
    /**
     * 搜索框-删除
     */
    ImageView iv_search_flow_box_delet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flow);
        stringList.addAll(SPUtils.getInstance(this).getSearch());
        init();
    }
    public void init(){
        rv_search_flow = findViewById(R.id.rv_search_flow);
        et_search_flow = findViewById(R.id.et_search_flow);
        iv_search_flow_box_delet = findViewById(R.id.iv_search_flow_box_delet);
        /**
         * 搜索框输入监听
         */
        et_search_flow.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){//搜索按键action
                    String searchStr = et_search_flow.getText().toString();
                    if (TextUtils.isEmpty(searchStr)){
                        Toast.makeText(SearchFlowActivity.this, R.string.title_shop_search, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    Intent intent = new Intent(SearchFlowActivity.this, FlowActivity.class);
                    intent.putExtra("searchkey",searchStr);
                    startActivity(intent);
                    searchSave();
                    return true;
                }
                return false;
            }


        });
        et_search_flow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    iv_search_flow_box_delet.setVisibility(View.VISIBLE);
                }else {
                    iv_search_flow_box_delet.setVisibility(View.GONE);
                }
            }
        });
        /**
         * RecyclerView 适配器适配
         */
        recyclerviewAdapter = new RecyclerviewAdapter(R.layout.item_search_flow,stringList);
        rv_search_flow.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_search_flow.setAdapter(recyclerviewAdapter);

        /**
         * item中设置以及其他操作
         */
        recyclerviewAdapter.setOnCallBackData(new RecyclerviewAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                final String str = (String) item;
                helper.setText(R.id.tv_item_search_flow,str);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(SearchFlowActivity.this, FlowActivity.class);
                        intent.putExtra("searchkey",str);
                        startActivity(intent);                    }
                });
            }
        });

    }


    public  void searchSave() {
        boolean exist = false;
        int aFew = -1;
        //搜索
        if (et_search_flow.length()>0){
            if (stringList.size()<=0) {
                stringList.add(et_search_flow.getText().toString());
            }else {
                for (int i = 0;i<stringList.size();i++){
                    if (stringList.get(i).equals(et_search_flow.getText().toString())){
                        exist = true;
                        aFew = i;
                    }
                }
                if (exist){
                    if (aFew!=-1){
                        stringList.remove(aFew);
                    }
                }
                stringList.add(0, et_search_flow.getText().toString());
            }
//                        startActivity(new Intent(SearchFlowActivity.this,FlowMallActivity.class));

            recyclerviewAdapter.notifyDataSetChanged();

            Intent intent = new Intent(SearchFlowActivity.this, FlowActivity.class);
            intent.putExtra("searchkey",et_search_flow.getText().toString());
            startActivity(intent);


        }else {
            Toast.makeText(SearchFlowActivity.this,"没有搜索内容",Toast.LENGTH_SHORT).show();
        }
        SPUtils.putSearch(stringList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.tv_search_flow:
                boolean exist = false;
                int aFew = -1;
                //搜索
                if (et_search_flow.length()>0){
                    if (stringList.size()<=0) {
                        stringList.add(et_search_flow.getText().toString());
                    }else {
                        for (int i = 0;i<stringList.size();i++){
                            if (stringList.get(i).equals(et_search_flow.getText().toString())){
                                exist = true;
                                aFew = i;
                            }
                        }
                        if (exist){
                            if (aFew!=-1){
                                stringList.remove(aFew);
                            }
                        }
                        stringList.add(0, et_search_flow.getText().toString());
                    }
//                        startActivity(new Intent(SearchFlowActivity.this,FlowMallActivity.class));

                    recyclerviewAdapter.notifyDataSetChanged();

                    Intent intent = new Intent(SearchFlowActivity.this, FlowActivity.class);
                    intent.putExtra("searchkey",et_search_flow.getText().toString());
                    startActivity(intent);


                }else {
                    Toast.makeText(SearchFlowActivity.this,"没有搜索内容",Toast.LENGTH_SHORT).show();
                }
                SPUtils.putSearch(stringList);
                break;
            case R.id.iv_search_flow_box_delet:
                // 删除搜索内容
                et_search_flow.setText(null);
                iv_search_flow_box_delet.setVisibility(View.GONE);
                break;
            case R.id.iv_search_flow_history_delet:
                stringList.clear();
                SPUtils.putSearch(stringList);
                recyclerviewAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    public void searchEngines(String str){

    }
}
