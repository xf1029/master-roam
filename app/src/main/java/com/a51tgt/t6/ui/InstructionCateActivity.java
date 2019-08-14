package com.a51tgt.t6.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.a51tgt.t6.R;
import com.a51tgt.t6.adapter.ListViewAdapter;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InstructionCateActivity extends BaseActivity implements View.OnClickListener {
	private RelativeLayout japanese,french,german,spain,korea;
	private ImageView iv_back;
	JSONArray   instructions;
	private ListView listView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instru_cate);
		initData();
		initView();

	}

	private  void initData(){
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[0];

		new SendRequest("http://mall.51tgt.com/locales/instructions.json", null, new MyHandler(), 1);




	}
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (msg == null || msg.obj == null) {
				return;
			}

			switch (msg.what){
				case 1:

//					try {
//						JSONObject OBJ = new JSONArray (msg.obj.toString());
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}

					Log.i("prucut555:",msg.toString());

					try {
						JSONArray OBJ = new JSONArray(msg.obj.toString());
						instructions = OBJ;
//						String[] options = getResources().getStringArray(R.array.languages);

						listView.setAdapter(new ListViewAdapter(InstructionCateActivity.this,instructions));
						Log.i("prucut555:",OBJ.toString());


					} catch (JSONException e) {

						Log.i("error",e.toString());
					}

					break;
				default:
					break;
			}
		}
	}
	protected void initView() {

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listView=(ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				try {
					JSONObject object = instructions.getJSONObject(position);
					skip(object.getString("item"));


				} catch (JSONException e) {
					e.printStackTrace();
				}



			}
		});

//		japanese = (RelativeLayout) findViewById(R.id.ja);
//		french = (RelativeLayout) findViewById(R.id.fre);
//		german = findViewById(R.id.ge);
//		spain = findViewById(R.id.xi);
//		korea = findViewById(R.id.han);
//		japanese.setOnClickListener(this);


//		japanese.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				Intent intent_instruction = new Intent(InstructionCateActivity.this, com.a51tgt.t6.ui.InstructionActivity.class);
//				startActivity(intent_instruction);
//			}
//		});



	}
private void skip (String item){

	Intent intent_instruction = new Intent(InstructionCateActivity.this, com.a51tgt.t6.ui.InstructionActivity.class);
	intent_instruction.putExtra("item", item);

	startActivity(intent_instruction);


}

	@Override
	public void onClick(View v) {
		switch (v.getId()){

//			case R.id.ja:
//				skip();
//				break;
//			case R.id.fre:
//				skip();
//				break;
//			case R.id.ge:
//				skip();
//				break;
//			case R.id.han:
//				skip();
//				break;
//			case R.id.xi:
//				skip();
//				break;
			default:
				break;


		}
	}
}
