package com.netease.nim.uikit.business.session.actions;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.picker.activity.PreviewImageFromCameraActivity;
import com.netease.nim.uikit.common.util.file.AttachmentStore;
import com.netease.nim.uikit.common.util.media.ImageUtil;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

import static com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher.IMAGE_PICK;
import static com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher.IMAGE_TAKE;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class PhotographAction extends PickImageAction {


    public PhotographAction() {
        super(R.drawable.nim_message_plus_video_pressed, R.string.input_panel_photograph, true);
    }

    @Override
    public void onClick() {
        int requestCode = makeRequestCode(RequestCode.PICK_IMAGE);
        showSelector(getTitleId(), requestCode, multiSelect, IMAGE_TAKE);
    }


    @Override
    protected void onPicked(File file) {
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
            message = ChatRoomMessageBuilder.createChatRoomImageMessage(getAccount(), file, file.getName());
        } else {
            message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        }
        sendMessage(message);
    }

}

