package com.netmi.workerbusiness.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.BindAliasCmdMessage;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.igexin.sdk.message.UnBindAliasCmdMessage;
import com.liemi.basemall.utils.NotificationUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.PushMessageEntity;
import com.netmi.workerbusiness.ui.MainActivity;


/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/8
 * 修改备注：
 */
public class XyPushIntentService extends GTIntentService {
    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    //接收cid
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        boolean b = PushManager.getInstance().bindAlias(getApplicationContext(), Constant.PUSH_PREFIX + AccessTokenCache.get().getUid(), PushConsts.BIND_ALIAS_RESULT + "");
        Log.e(TAG, "onReceiveClientId -> " + "aliadid = " + Constant.PUSH_PREFIX + AccessTokenCache.get().getUid() + "====" + b);
    }

    //处理透传消息
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        //透传消息的处理
        Log.e(TAG, "onReceiveMessageData -> " + "gtTransmitMessage = " + gtTransmitMessage.toString());

        String appid = gtTransmitMessage.getAppid();
        String taskid = gtTransmitMessage.getTaskId();
        String messageid = gtTransmitMessage.getMessageId();
        byte[] payload = gtTransmitMessage.getPayload();
        String pkg = gtTransmitMessage.getPkgName();
        String cid = gtTransmitMessage.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);
            //TODO:个推消息设置标题
            getNotification(data, messageid, "客商e宝商家端");
        }
    }

    //cid离线上线通知
    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    //各种事件处理回执
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.d(TAG, "onReceiveCommandResult -> " + gtCmdMessage);

        int action = gtCmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) gtCmdMessage);
        } else if (action == PushConsts.BIND_ALIAS_RESULT) {
            bindAliasResult((BindAliasCmdMessage) gtCmdMessage);
        } else if (action == PushConsts.UNBIND_ALIAS_RESULT) {
            unbindAliasResult((UnBindAliasCmdMessage) gtCmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) gtCmdMessage);
        }
    }

    // 通知到达，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.e(TAG, "onNotificationMessageArrived -> " + "gtNotificationMessage = " + gtNotificationMessage.getTitle());
        GTNotificationMessage gtNotificationMessage1 = gtNotificationMessage;
        String content = gtNotificationMessage1.getContent();
    }

    // 通知点击，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.e(TAG, "onNotificationMessageClicked -> " + "gtNotificationMessage = " + gtNotificationMessage.toString());
    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        int text = R.string.add_tag_unknown_exception;
        switch (Integer.parseInt(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = R.string.add_tag_success;
                break;
            case PushConsts.SETTAG_ERROR_COUNT:
                text = R.string.add_tag_error_count;
                break;
            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = R.string.add_tag_error_frequency;
                break;
            case PushConsts.SETTAG_ERROR_REPEAT:
                text = R.string.add_tag_error_repeat;
                break;
            case PushConsts.SETTAG_ERROR_UNBIND:
                text = R.string.add_tag_error_unbind;
                break;
            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = R.string.add_tag_unknown_exception;
                break;
            case PushConsts.SETTAG_ERROR_NULL:
                text = R.string.add_tag_error_null;
                break;
            case PushConsts.SETTAG_NOTONLINE:
                text = R.string.add_tag_error_not_online;
                break;
            case PushConsts.SETTAG_IN_BLACKLIST:
                text = R.string.add_tag_error_black_list;
                break;
            case PushConsts.SETTAG_NUM_EXCEED:
                text = R.string.add_tag_error_exceed;
                break;
            case PushConsts.SETTAG_TAG_ILLEGAL:
                text = R.string.add_tag_error_tagIllegal;
                break;
            default:
                break;
        }
        Log.d(TAG, "settag result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));
    }

    private void bindAliasResult(BindAliasCmdMessage bindAliasCmdMessage) {
        String sn = bindAliasCmdMessage.getSn();
        String code = bindAliasCmdMessage.getCode();

        int text = R.string.bind_alias_unknown_exception;
        switch (Integer.parseInt(code)) {
            case PushConsts.BIND_ALIAS_SUCCESS:
                text = R.string.bind_alias_success;
                break;
            case PushConsts.ALIAS_ERROR_FREQUENCY:
                text = R.string.bind_alias_error_frequency;
                break;
            case PushConsts.ALIAS_OPERATE_PARAM_ERROR:
                text = R.string.bind_alias_error_param_error;
                break;
            case PushConsts.ALIAS_REQUEST_FILTER:
                text = R.string.bind_alias_error_request_filter;
                break;
            case PushConsts.ALIAS_OPERATE_ALIAS_FAILED:
                text = R.string.bind_alias_unknown_exception;
                break;
            case PushConsts.ALIAS_CID_LOST:
                text = R.string.bind_alias_error_cid_lost;
                break;
            case PushConsts.ALIAS_CONNECT_LOST:
                text = R.string.bind_alias_error_connect_lost;
                break;
            case PushConsts.ALIAS_INVALID:
                text = R.string.bind_alias_error_alias_invalid;
                break;
            case PushConsts.ALIAS_SN_INVALID:
                text = R.string.bind_alias_error_sn_invalid;
                break;
            default:
                break;

        }
        Log.d(TAG, "bindAlias result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));

    }

    private void unbindAliasResult(UnBindAliasCmdMessage unBindAliasCmdMessage) {
        String sn = unBindAliasCmdMessage.getSn();
        String code = unBindAliasCmdMessage.getCode();

        int text = R.string.unbind_alias_unknown_exception;
        switch (Integer.parseInt(code)) {
            case PushConsts.UNBIND_ALIAS_SUCCESS:
                text = R.string.unbind_alias_success;
                break;
            case PushConsts.ALIAS_ERROR_FREQUENCY:
                text = R.string.unbind_alias_error_frequency;
                break;
            case PushConsts.ALIAS_OPERATE_PARAM_ERROR:
                text = R.string.unbind_alias_error_param_error;
                break;
            case PushConsts.ALIAS_REQUEST_FILTER:
                text = R.string.unbind_alias_error_request_filter;
                break;
            case PushConsts.ALIAS_OPERATE_ALIAS_FAILED:
                text = R.string.unbind_alias_unknown_exception;
                break;
            case PushConsts.ALIAS_CID_LOST:
                text = R.string.unbind_alias_error_cid_lost;
                break;
            case PushConsts.ALIAS_CONNECT_LOST:
                text = R.string.unbind_alias_error_connect_lost;
                break;
            case PushConsts.ALIAS_INVALID:
                text = R.string.unbind_alias_error_alias_invalid;
                break;
            case PushConsts.ALIAS_SN_INVALID:
                text = R.string.unbind_alias_error_sn_invalid;
                break;
            default:
                break;

        }
        Log.d(TAG, "unbindAlias result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));
    }


    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        Log.d(TAG, "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

    private void getNotification(String data, String id, String title) {
        try {
            //这里对透传消息进行发送 通过App中的方法进行处理
            Intent intent = new Intent();
            String message = "";
            Gson gson = new Gson();
            PushMessageEntity pushMessageEntity;
            try {
                pushMessageEntity = gson.fromJson(data, PushMessageEntity.class);
            } catch (Exception e) {
                //如果解析出错，就当作普通消息处理
                pushMessageEntity = new PushMessageEntity();
                pushMessageEntity.setType(PushMessageEntity.APP_NORMAL_MESSAGE);
                PushMessageEntity.PushContent pushContent = new PushMessageEntity.PushContent();
                pushContent.setContent(data);
                pushMessageEntity.setData(pushContent);
            }

            switch (pushMessageEntity.getType()) {
                case PushMessageEntity.APP_NORMAL_MESSAGE:
                    //普通消息
                    message = pushMessageEntity.getData().getContent();
                    intent.setClass(this, MainActivity.class);
                    break;
                case PushMessageEntity.APP_UPDATE_MESSAGE:
                    message = getString(R.string.sharemall_new_version_tips);
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(pushMessageEntity.getData().getContent()));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    break;
                case PushMessageEntity.APP_ORDER_MESSAGE:
                    //我的订单详情
//                    intent.setClass(this, MineOrderDetailsActivity.class);
//                    intent.putExtra(MineOrderDetailsActivity.ORDER_DETAILS_ID, pushMessageEntity.getData().getId());
                    break;
            }
            NotificationUtils.sendNotification(NotificationUtils.MESSAGE_NORMAL,
                    Strings.toInt(id),
                    title,
                    message,
                    PendingIntent.getActivity(MApplication.getAppContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
