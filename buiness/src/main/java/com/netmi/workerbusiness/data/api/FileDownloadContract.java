package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.ui.BaseView;

import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/13
 * 修改备注：
 */
public interface FileDownloadContract {
    interface View extends BaseView {

        void fileDownloadResult(List<String> imgUrls);

        void fileDownloadProgress(int percentage);

        void fileDownloadFail(String errMsg);
    }

    interface Presenter extends BasePresenter {

        void doDownloadFiles(List<String> filePaths);
    }
}
