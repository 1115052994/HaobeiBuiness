package com.netmi.workerbusiness.im.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nim.uikit.business.session.module.list.MessageListPanelEx;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.MessageHistoryActivityBinding;

/**
 * 消息历史查询界面
 * <p/>
 * Created by huangjun on 2015/4/17.
 */
public class MessageHistoryActivity extends BaseActivity<MessageHistoryActivityBinding> implements ModuleProxy {

    private static final String EXTRA_DATA_ACCOUNT = "EXTRA_DATA_ACCOUNT";
    private static final String EXTRA_DATA_SESSION_TYPE = "EXTRA_DATA_SESSION_TYPE";

    // context
    private SessionTypeEnum sessionType;
    private String account; // 对方帐号

    private MessageListPanelEx messageListPanel;

    public static void start(Context context, String account, SessionTypeEnum sessionType) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATA_ACCOUNT, account);
        intent.putExtra(EXTRA_DATA_SESSION_TYPE, sessionType);
        intent.setClass(context, MessageHistoryActivity.class);
        context.startActivity(intent);
    }

    /**
     * ***************************** life cycle *******************************
     */

    @Override
    protected int getContentView() {
        return R.layout.message_history_activity;
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = LayoutInflater.from(this).inflate(R.layout.message_history_activity, null);
        setContentView(rootView);
        onParseIntent();
        getTvTitle().setText(getString(R.string.message_history_query));
        Container container = new Container(this, account, sessionType, this);
        messageListPanel = new MessageListPanelEx(container, rootView, true, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        messageListPanel.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageListPanel.onDestroy();
    }

    protected void onParseIntent() {
        account = getIntent().getStringExtra(EXTRA_DATA_ACCOUNT);
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra(EXTRA_DATA_SESSION_TYPE);
        getTvTitle().setText(UserInfoHelper.getUserTitleName(account, sessionType));
    }

    @Override
    public boolean sendMessage(IMMessage msg) {
        return false;
    }

    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public void onItemFooterClick(IMMessage message) {

    }

    @Override
    public boolean isLongClickEnabled() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (messageListPanel != null) {
            messageListPanel.onActivityResult(requestCode, resultCode, data);
        }
    }
}
