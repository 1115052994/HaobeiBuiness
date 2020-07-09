package com.liemi.basemall.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.ConfirmDialogEntity;
import com.liemi.basemall.databinding.DialogConfirmBottomBinding;
import com.liemi.basemall.databinding.ItemConfirmDialogBottomBinding;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.Densitys;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/2/25
 * 修改备注：
 */
public class ConfirmBottomDialog extends Dialog {
    private static final String TAG = "ConfirmBottomDialog";
    //按钮显示文字
    private int[] buttonsTtitle;
    private String[] buttonsTtitles;
    private int[] titlesColor;
    private int[] contentColor;
    //取消按钮文字
    private int confirm = R.string.confirm;
    //标题文字
    private int title = -1;
    //点击事件
    private ItemClickListener confirmListner;
    private ItemClickListener[] listeners;
    private DialogConfirmBottomBinding binding;
    private BaseRViewAdapter<ConfirmDialogEntity, BaseViewHolder<ConfirmDialogEntity>> adapter;

    public ConfirmBottomDialog(@NonNull Context context) {
        this(context, com.netmi.baselibrary.R.style.showDialog);
        adapter = new BaseRViewAdapter<ConfirmDialogEntity, BaseViewHolder<ConfirmDialogEntity>>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_confirm_dialog_bottom;
            }

            @Override
            public BaseViewHolder<ConfirmDialogEntity> holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<ConfirmDialogEntity>(binding) {
                    @Override
                    public void bindData(ConfirmDialogEntity item) {
                        super.bindData(item);
                        ItemConfirmDialogBottomBinding itemBinding = (ItemConfirmDialogBottomBinding) binding;
                        if (item.getTitleColor() != 0) {
                            itemBinding.tvTitle.setTextColor(getContext().getResources().getColor(item.getTitleColor()));
                        }
                        if (item.getContentColor() != 0) {
                            itemBinding.tvContent.setTextColor(getContext().getResources().getColor(item.getContentColor()));
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (listeners != null && listeners.length > position && listeners[position] != null)
                            listeners[position].onClick(items.get(position).getContentText());
                    }
                };
            }
        };
    }

    public ConfirmBottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public ConfirmBottomDialog buttonsTitle(@StringRes int... buttonsTtitle) {
        this.buttonsTtitle = buttonsTtitle;
        return this;
    }

    //传参为string类型
    public ConfirmBottomDialog buttonsTitles(String... buttonsTtitle) {
        this.buttonsTtitles = buttonsTtitle;
        return this;
    }


    public ConfirmBottomDialog titlesColor(int... titlesColor) {
        this.titlesColor = titlesColor;
        return this;
    }

    public ConfirmBottomDialog contentColor(int... contentColor) {
        this.contentColor = contentColor;
        return this;
    }

    public ConfirmBottomDialog buttonsContent(String... content) {
        List<ConfirmDialogEntity> newData = new ArrayList<>();
        for (int i = 0; i < buttonsTtitle.length; i++) {
            ConfirmDialogEntity data = new ConfirmDialogEntity(getContext().getString(buttonsTtitle[i]), getContentText(content, i));
            if (titlesColor != null && titlesColor.length > i && titlesColor[i] != 0)
                data.setTitleColor(titlesColor[i]);
            if (contentColor != null && contentColor.length > i && contentColor[i] != 0)
                data.setContentColor(contentColor[i]);
            newData.add(data);
        }
        adapter.clear();
        adapter.insert(newData);
        return this;
    }

    //buttonsTtitles传参为string类型
    public ConfirmBottomDialog buttonsContents(String... content) {
        List<ConfirmDialogEntity> newData = new ArrayList<>();
        for (int i = 0; i < buttonsTtitles.length; i++) {
            ConfirmDialogEntity data = new ConfirmDialogEntity(buttonsTtitles[i], getContentText(content, i));
            if (titlesColor != null && titlesColor.length > i && titlesColor[i] != 0)
                data.setTitleColor(titlesColor[i]);
            if (contentColor != null && contentColor.length > i && contentColor[i] != 0)
                data.setContentColor(contentColor[i]);
            newData.add(data);
        }
        adapter.clear();
        adapter.insert(newData);
        return this;
    }


    private String getContentText(String[] content, int i) {
        try {
            return content[i] == null ? "" : content[i];
        } catch (Exception e) {
            return "";
        }
    }

    public ConfirmBottomDialog clickListener(ItemClickListener... listeners) {
        this.listeners = listeners;
        return this;
    }

    public ConfirmBottomDialog confirmClick(ItemClickListener listener) {
        confirmListner = listener;
        return this;
    }

    public ConfirmBottomDialog confirmText(@StringRes int confirmText) {
        confirm = confirmText;
        return this;
    }

    public ConfirmBottomDialog titleText(@StringRes int title) {
        this.title = title;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_confirm_bottom, null, false);
        setContentView(binding.getRoot());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.recyclerView.setAdapter(adapter);

        binding.btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (confirmListner != null)
                    confirmListner.onClick(getContext().getString(confirm));
            }
        });


        binding.btConfirm.setText(confirm);
        if (title != -1) {
            binding.tvTitle.setText(title);
            binding.tvTitle.setVisibility(View.VISIBLE);
        }

        binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    //dialog显示在底部
    public void showBottom() {
        show();
        //获取当前dialog显示的window
        Window mDialogWindow = getWindow();
        if (mDialogWindow != null) {
            WindowManager.LayoutParams params = mDialogWindow.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            mDialogWindow.setAttributes(params);
            mDialogWindow.setWindowAnimations(com.netmi.baselibrary.R.style.dialog_bottom_anim_style);
            mDialogWindow.getDecorView().setPadding(Densitys.dp2px(getContext(), 0), Densitys.dp2px(getContext(), 0), Densitys.dp2px(getContext(), 0), Densitys.dp2px(getContext(), 0));
        }
    }

    //item点击的接口
    public interface ItemClickListener {
        void onClick(String string);
    }
}
