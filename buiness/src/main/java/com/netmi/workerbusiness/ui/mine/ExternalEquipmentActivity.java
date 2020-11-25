package com.netmi.workerbusiness.ui.mine;

import android.view.View;
import android.widget.Toast;

import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MessApi;
import com.netmi.workerbusiness.databinding.ActivityExternalEquipmentBinding;
import com.netmi.workerbusiness.widget.SoundBindDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

//外部设备
public class ExternalEquipmentActivity extends BaseActivity<ActivityExternalEquipmentBinding> {



    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_external_equipment;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("外部设备");

        String type = getIntent().getStringExtra("type");
        if(type!=null&&!type.equals("")){
            SoundBindDialog.show(ExternalEquipmentActivity.this,type);
            SoundBindDialog.setlict(new SoundBindDialog.MyListener() {
                @Override
                public void getData(String res) {
                    doRequ(res,"1");
                }
            });
        }

        String audio_device_id = UserInfoCache.get().getAudio_device_id();
        if(audio_device_id==null||audio_device_id.equals("")){
            mBinding.tvIsBind.setText("去绑定");
            mBinding.tvSoundId.setVisibility(View.INVISIBLE);
        }else {
            mBinding.tvIsBind.setText("解绑");
            mBinding.tvSoundId.setText("设备ID："+audio_device_id);
        }
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.relayout_sound) {   //音响设备
            String trim = mBinding.tvIsBind.getText().toString().trim();
            if(trim.equals("去绑定")){
                SoundBindDialog.show(ExternalEquipmentActivity.this);
                SoundBindDialog.setlict(new SoundBindDialog.MyListener() {
                    @Override
                    public void getData(String res) {
                        doRequ(res,"1");

                    }
                });
            }else if(trim.equals("解绑")){
                doRequ("","2");
            }
        }
    }
    public void doRequ(String  audio_device_id,String type){
        RetrofitApiFactory.createApi(MessApi.class)
                .bindAudioSystem(audio_device_id, type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        if(type.equals("1")){
                            mBinding.tvIsBind.setText("解绑");
                            Toast.makeText(ExternalEquipmentActivity.this,"设备音响绑定成功",Toast.LENGTH_LONG).show();
                            UserInfoCache.get().setAudio_device_id(audio_device_id);
                            mBinding.tvSoundId.setVisibility(View.VISIBLE);
                            mBinding.tvSoundId.setText("设备ID："+audio_device_id);
                        }else if(type.equals("2")){
                            mBinding.tvIsBind.setText("去绑定");
                            Toast.makeText(ExternalEquipmentActivity.this,"设备音响解绑成功",Toast.LENGTH_LONG).show();
                            UserInfoCache.get().setAudio_device_id("");
                            mBinding.tvSoundId.setVisibility(View.GONE);
                        }
                        SoundBindDialog.dismiss();

                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        showError(ex.getMessage().toString());
                    }
                });
    }
}
