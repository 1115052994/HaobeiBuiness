package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.databinding.ActivityAdvanceTeamJurisdictionBinding;
import com.netease.nim.uikit.databinding.ItemTeamJurisdictionBinding;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamBeInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.constant.TeamUpdateModeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import java.util.ArrayList;
import java.util.List;

public class AdvanceTeamJurisdictionActivity extends BaseActivity<ActivityAdvanceTeamJurisdictionBinding> {
    public final static String JURISDICTION_TYPE = "jurisdiction_type";
    public final static String CHECK_POSITION = "check_position";
    public final static String TEAM_ID = "team_id";
    public final static String ENUM_TYPE = "enum_type";
    //消息提醒
    public final static int NOTICE = 111;
    //身份验证
    public final static int IDENTITY_VERIFY = 222;
    //邀请他人权限
    public final static int INVITE = 333;
    //群消息修改权限
    public final static int TEAM_INFO_UPDATE = 444;
    //被邀请人身份验证
    public final static int TEAM_INVITED_VERIFY = 555;

    private int title_type;

    private String team_id;

    private RecyclerView recyclerView;

    private int checkPosition;

    private String checkType;

    private List<OptionEntity> optionEntities;

    TeamMessageNotifyTypeEnum teamMessageNotifyTypeEnum;
    VerifyTypeEnum verifyTypeEnum;
    TeamInviteModeEnum teamInviteModeEnum;
    TeamUpdateModeEnum teamUpdateModeEnum;
    TeamBeInviteModeEnum teamBeInviteModeEnum;

    private String[] noticeList = new String[]{"提醒所有消息","不提醒任何消息","只提醒群主消息"};
    private String[] identityList = new String[]{"允许任何人","需要验证","拒绝任何人"};
    private String[] inviteList = new String[]{"群主","所有人"};
    private String[] teamInfoUpdateList = new String[]{"群主","所有人"};
    private String[] invitedList = new String[]{"需要验证","不需要验证"};

    BaseRViewAdapter<OptionEntity, BaseViewHolder> adapter;

    public static void startForResult(Context context,int type,int checkPosition,String team_id,int request_code){
        Intent intent = new Intent();
        intent.putExtra(JURISDICTION_TYPE,type);
        intent.putExtra(CHECK_POSITION,checkPosition);
        intent.putExtra(TEAM_ID,team_id);
        intent.setClass(context, AdvanceTeamJurisdictionActivity.class);
        ((Activity)context).startActivityForResult(intent,request_code);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_advance_team_jurisdiction;
    }

    @Override
    protected void initUI() {
        title_type = getIntent().getIntExtra(JURISDICTION_TYPE,0);
        checkPosition = getIntent().getIntExtra(CHECK_POSITION,0);
        team_id = getIntent().getStringExtra(TEAM_ID);
        initJurosdictionData();
        getRightSetting().setText("保存");
        getRightSetting().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (title_type){
                    case NOTICE:
                        teamMessageNotifyTypeEnum = TeamHelper.getNotifyType(checkType);
                        doNotice(team_id,teamMessageNotifyTypeEnum);
                        break;
                    case IDENTITY_VERIFY:
                        verifyTypeEnum = TeamHelper.getVerifyTypeEnum(checkType);
                        doAuthen(team_id,verifyTypeEnum);
                        break;
                    case INVITE:
                        teamInviteModeEnum = TeamHelper.getInviteModeEnum(checkType);
                        doUpdateInviteMode(team_id,teamInviteModeEnum);
                        break;
                    case TEAM_INFO_UPDATE:
                        teamUpdateModeEnum = TeamHelper.getUpdateModeEnum(checkType);
                        doUpdateInfoUpdateMode(team_id,teamUpdateModeEnum);
                        break;
                    case TEAM_INVITED_VERIFY:
                        teamBeInviteModeEnum = TeamHelper.getBeInvitedModeEnum(checkType);
                        doUpdateBeInvitedMode(team_id,teamBeInviteModeEnum);
                        break;
                }
            }
        });
        recyclerView = mBinding.rvData;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseRViewAdapter<OptionEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_team_jurisdiction;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(final Object item) {
                        super.bindData(item);
                        getBinding().flContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkPosition = position;
                                checkType = ((OptionEntity)item).getTitle();
                                for (OptionEntity entity : optionEntities){
                                    if (optionEntities.indexOf(entity) == checkPosition){
                                        optionEntities.get(checkPosition).setChecked(true);
                                    }else {
                                        optionEntities.get(optionEntities.indexOf(entity)).setChecked(false);
                                    }
                                }
                                notifyDataSetChanged();
                            }
                        });

                    }

                    @Override
                    public ItemTeamJurisdictionBinding getBinding() {
                        return (ItemTeamJurisdictionBinding)super.getBinding();
                    }
                };

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setData(optionEntities);

    }

    @Override
    protected void initData() {

    }

    private void initJurosdictionData(){
        optionEntities = new ArrayList<>();
        switch (title_type){
            case NOTICE:
                getTvTitle().setText("消息提醒");
                for (String option : noticeList){
                    OptionEntity optionEntity = new OptionEntity();
                    optionEntity.setTitle(option);
                    optionEntity.setChecked(false);
                    optionEntities.add(optionEntity);
                }
                optionEntities.get(checkPosition).setChecked(true);
                break;
            case IDENTITY_VERIFY:
                getTvTitle().setText("身份验证");
                for (String option : identityList){
                    OptionEntity optionEntity = new OptionEntity();
                    optionEntity.setTitle(option);
                    optionEntity.setChecked(false);
                    optionEntities.add(optionEntity);
                }
                optionEntities.get(checkPosition).setChecked(true);
                break;
            case INVITE:
                getTvTitle().setText("邀请他人权限");
                for (String option : inviteList){
                    OptionEntity optionEntity = new OptionEntity();
                    optionEntity.setTitle(option);
                    optionEntity.setChecked(false);
                    optionEntities.add(optionEntity);
                }
                optionEntities.get(checkPosition).setChecked(true);
                break;
            case TEAM_INFO_UPDATE:
                getTvTitle().setText("群资料修改权限");
                for (String option : teamInfoUpdateList){
                    OptionEntity optionEntity = new OptionEntity();
                    optionEntity.setTitle(option);
                    optionEntity.setChecked(false);
                    optionEntities.add(optionEntity);
                }
                optionEntities.get(checkPosition).setChecked(true);
                break;
            case TEAM_INVITED_VERIFY:
                getTvTitle().setText("被邀请人身份验证");
                for (String option : invitedList){
                    OptionEntity optionEntity = new OptionEntity();
                    optionEntity.setTitle(option);
                    optionEntity.setChecked(false);
                    optionEntities.add(optionEntity);
                }
                optionEntities.get(checkPosition).setChecked(true);
                break;
        }
        checkType = optionEntities.get(checkPosition).getTitle();

    }

    //消息通知
    public void doNotice(String teamId, final TeamMessageNotifyTypeEnum  type){
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).muteTeam(teamId, type)
                .setCallback(new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ENUM_TYPE,type);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }

    /**
     * 身份验证
     *
     * @param type 验证类型
     */
    private void doAuthen(String teamId, final VerifyTypeEnum type) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.VerifyType, type).setCallback(
                new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ENUM_TYPE,type);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }

    /**
     * 更新邀请他人权限
     *
     * @param type 邀请他人类型
     */
    private void doUpdateInviteMode(String teamId, final TeamInviteModeEnum type) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.InviteMode, type).setCallback(
                new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ENUM_TYPE,type);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }

    /**
     * 更新群资料修改权限
     *
     * @param type 群资料修改类型
     */
    private void doUpdateInfoUpdateMode(String teamId, final TeamUpdateModeEnum type) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.TeamUpdateMode, type).setCallback(
                new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ENUM_TYPE,type);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }

    /**
     * 更新被邀请人权限
     *
     * @param type 被邀请人类型
     */
    private void doUpdateBeInvitedMode(String teamId, final TeamBeInviteModeEnum type) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.BeInviteMode, type).setCallback(
                new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ENUM_TYPE,type);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }
}
