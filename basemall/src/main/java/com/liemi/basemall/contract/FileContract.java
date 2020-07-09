package com.liemi.basemall.contract;
import com.liemi.basemall.data.entity.FileEntity;
import com.netmi.baselibrary.data.entity.UpFilesEntity;
import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.ui.BaseView;

import java.util.List;

/**
 * Created by Bingo on 2018/6/1.
 */

public interface FileContract {

    interface FilesUpView extends BaseView {
        void filesUpSuccess(List<String> imageUrl);
        void filesUpFailure(String msg);
    }

    interface FileUpView extends BaseView{
        void fileUpSuccess(UpFilesEntity entity);
        void fileFailure(String msg);
    }

    interface Presenter extends BasePresenter {
    }
}
