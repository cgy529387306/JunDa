package com.android.mb.junda.tts;

import android.content.Context;
import android.os.Bundle;

import com.android.mb.junda.utils.LogHelper;
import com.android.mb.junda.utils.ToastHelper;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * @author yuanchun
 * @content 内部Activity管理实例
 * @time 2016/5/3
 */
public class TtsManager {

    private static TtsManager ttsManager = null;

    private SpeechSynthesizer mTts;
    private TtsManager(Context context) {
        initTTS(context);
    }

    /**
     * 返回activity管理器的唯一实例对象。
     *
     * @return ActivityTaskManager
     */
    public static synchronized TtsManager getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TtsManager(context.getApplicationContext());
        }
        return ttsManager;
    }

    private void initTTS(Context context){
        mTts = SpeechSynthesizer.createSynthesizer(context, new InitListener() {
            @Override
            public void onInit(int code) {
                LogHelper.e("InitListener init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    ToastHelper.showLongToast("初始化失败,错误码："+code);
                }
            }
        });
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED,"50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
    }


    public void playText(String text){
        int code = mTts.startSpeaking(text, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
        if (code != ErrorCode.SUCCESS) {
            ToastHelper.showLongToast("语音合成失败,错误码: " + code);
        }
    }

}
