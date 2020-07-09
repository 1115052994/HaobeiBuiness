package com.netmi.workerbusiness.im.session.extension;

import com.alibaba.fastjson.JSONObject;

public class TransferAttachmnet extends CustomAttachment {

    public TransferAttachmnet(int type) {
        super(type);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
