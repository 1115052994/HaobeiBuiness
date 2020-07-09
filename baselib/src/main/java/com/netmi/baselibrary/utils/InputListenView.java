package com.netmi.baselibrary.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class InputListenView implements TextWatcher {

    private EditText[] editTexts;

    private View listenView;

    protected InputListenView(View listenView, EditText... editTexts) {
        if (editTexts == null || listenView == null || editTexts.length == 0) {
            return;
        }

        this.editTexts = editTexts;
        this.listenView = listenView;
        this.listenView.setEnabled(false);
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(this);
        }

    }

    //自己需要另外做到逻辑判断
    public boolean customJudge() {
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        refresh();

    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    public void refresh() {
        boolean enable = true;
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                enable = false;
                break;
            }
        }

        listenView.setEnabled(enable && customJudge());
    }

}
