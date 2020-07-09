package com.netmi.baselibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.ToastUtils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/22 15:15
 * 修改备注：
 */
public class InputDialog extends Dialog {

    private OnConfirmCallBack onConfirmCallBack;

    private EditText etInput;

    public InputDialog(Context context) {
        super(context, R.style.transparentDialog);
        initUI();
    }

    public InputDialog(Context context, int themeResId) {
        super(context, themeResId);
        initUI();
    }

    public InputDialog(Context context, OnConfirmCallBack callBack) {
        this(context);
        onConfirmCallBack = callBack;
        initUI();
    }


    /**
     * 对话框布局初始化
     */
    private void initUI() {
        setContentView(R.layout.baselib_dialog_input);
        Button btConfirm = findViewById(R.id.bt_confirm);
        etInput = findViewById(R.id.et_input);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etInput.getText().toString())) {
                    ToastUtils.showShort(etInput.getHint());
                    return;
                }

                if (onConfirmCallBack != null) {
                    onConfirmCallBack.onCallBack(etInput);
                }
                dismiss();
            }
        });

        setDialogPosition();
    }

    public InputDialog setInputText(String inputText) {
        etInput.setText(inputText);
        return this;
    }

    public InputDialog setHintText(String hintText) {
        etInput.setHint(hintText);
        return this;
    }

    public InputDialog setMaxLength(int length) {
        InputFilter[] filters = {new InputFilter.LengthFilter(length)};
        etInput.setFilters(filters);
        return this;
    }

    private void setDialogPosition() {
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = -Densitys.dp2px(getContext(), 40);
            dialogWindow.setAttributes(lp);
        }
    }

    public interface OnConfirmCallBack {
        void onCallBack(EditText editText);
    }

}
