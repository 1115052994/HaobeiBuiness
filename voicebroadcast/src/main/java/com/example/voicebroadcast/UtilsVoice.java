package com.example.voicebroadcast;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class UtilsVoice {
    private static String TAG = UtilsVoice.class.getSimpleName();
    // 默认发音人
    private static String voicer = "xiaoyan";

    private static SpeechSynthesizer syn;
    private static Context contextA;
    public static void init(Context context){
        contextA = context;

        //实例化文字转语言对象
        syn = SpeechSynthesizer.createSynthesizer(contextA, ini);
        // 清空参数
        syn.setParameter(SpeechConstant.PARAMS,null);
        // 根据合成引擎设置相应参数
        if(SpeechConstant.TYPE_CLOUD.equals(SpeechConstant.TYPE_CLOUD)){
            // 在线引擎类型
            syn.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置合成语速
            syn.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            syn.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            syn.setParameter(SpeechConstant.VOLUME, "50");
        }else {
            syn.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            syn.setParameter(SpeechConstant.VOICE_NAME, voicer);
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        syn.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        syn.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        syn.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        syn.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    private static InitListener ini=new InitListener(){
        @Override
        public void onInit(int i) {
            if(i!= ErrorCode.SUCCESS){
                Toast.makeText(contextA, "初始化失败", Toast.LENGTH_SHORT).show();
            }else {

            }
        }
    };

    public static void startVoice(String text){
        //这个就是获取到tv1控件上面的文字然后转成语言
        syn.startSpeaking(text, new SynthesizerListener() {
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
    }



    //退出释放连接
    public static void dis(){
        if( null != syn ){
            syn.stopSpeaking();
            // 退出时释放连接
            syn.destroy();
        }
    }
}
