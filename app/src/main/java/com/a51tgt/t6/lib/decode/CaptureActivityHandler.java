package com.a51tgt.t6.lib.decode;

import android.os.Handler;
import android.os.Message;

import com.a51tgt.t6.R;
import com.a51tgt.t6.lib.Capture2Activity;
import com.a51tgt.t6.lib.camera.CameraManager;


/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:23:32
 *
 * 版本: V_1.0.0
 *
 * 描述: 扫描消息转发
 */
public final class CaptureActivityHandler extends Handler {

	DecodeThread decodeThread = null;
	Capture2Activity activity = null;
	private State state;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	public CaptureActivityHandler(Capture2Activity activity) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity);
		decodeThread.start();
		state = State.SUCCESS;
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {

		switch (message.what) {
		case R.id.auto_focus:
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}
			break;
		case R.id.restart_preview:
			restartPreviewAndDecode();
			break;
		case R.id.decode_succeeded:
			state = State.SUCCESS;
			activity.handleDecode((String) message.obj);// 解析成功，回调
			break;

		case R.id.decode_failed:
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			break;
		}

	}

	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
		removeMessages(R.id.decode);
		removeMessages(R.id.auto_focus);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
		}
	}

}
