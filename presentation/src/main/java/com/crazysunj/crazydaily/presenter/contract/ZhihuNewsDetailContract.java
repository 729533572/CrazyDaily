/**
 * Copyright 2017 Sun Jian
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crazysunj.crazydaily.presenter.contract;

import com.crazysunj.crazydaily.base.IView;
import com.crazysunj.crazydaily.base.IPresenter;
import com.crazysunj.domain.entity.ZhihuNewsDetailEntity;

/**
 * author: sunjian
 * created on: 2017/9/19 下午5:05
 * description: https://github.com/crazysunj/CrazyDaily
 */
public interface ZhihuNewsDetailContract {

    interface View extends IView {

        void showContent(ZhihuNewsDetailEntity zhihuNewsDetailEntity);
    }

    interface  Presenter extends IPresenter<View> {

        void getZhihuNewsDetail(long id);
    }
}
