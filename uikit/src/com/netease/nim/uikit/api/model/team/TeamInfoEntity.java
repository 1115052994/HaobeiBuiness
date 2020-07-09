package com.netease.nim.uikit.api.model.team;

import com.netease.nimlib.sdk.team.constant.TeamAllMuteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamBeInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamExtensionUpdateModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.constant.TeamUpdateModeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/27
 * 修改备注：
 */
public class TeamInfoEntity implements Team {


    private String tid;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @Override
    public String getId() {
        return tid;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public TeamTypeEnum getType() {
        return null;
    }

    @Override
    public String getAnnouncement() {
        return null;
    }

    @Override
    public String getIntroduce() {
        return null;
    }

    @Override
    public String getCreator() {
        return null;
    }

    @Override
    public int getMemberCount() {
        return 0;
    }

    @Override
    public int getMemberLimit() {
        return 0;
    }

    @Override
    public VerifyTypeEnum getVerifyType() {
        return null;
    }

    @Override
    public long getCreateTime() {
        return 0;
    }

    @Override
    public boolean isMyTeam() {
        return false;
    }

    @Override
    public void setExtension(String s) {

    }

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public String getExtServer() {
        return null;
    }

    @Override
    public boolean mute() {
        return false;
    }

    @Override
    public TeamMessageNotifyTypeEnum getMessageNotifyType() {
        return null;
    }

    @Override
    public TeamInviteModeEnum getTeamInviteMode() {
        return null;
    }

    @Override
    public TeamBeInviteModeEnum getTeamBeInviteMode() {
        return null;
    }

    @Override
    public TeamUpdateModeEnum getTeamUpdateMode() {
        return null;
    }

    @Override
    public TeamExtensionUpdateModeEnum getTeamExtensionUpdateMode() {
        return null;
    }

    @Override
    public boolean isAllMute() {
        return false;
    }

    @Override
    public TeamAllMuteModeEnum getMuteMode() {
        return null;
    }
}



