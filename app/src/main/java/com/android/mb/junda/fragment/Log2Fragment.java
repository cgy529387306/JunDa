package com.android.mb.junda.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mb.junda.R;
import com.android.mb.junda.adapter.Log2Adapter;
import com.android.mb.junda.constants.ProjectConstants;
import com.android.mb.junda.entity.CustomApiResult;
import com.android.mb.junda.entity.Log2;
import com.android.mb.junda.entity.Log2Resp;
import com.android.mb.junda.utils.Helper;
import com.android.mb.junda.utils.JsonHelper;
import com.android.mb.junda.utils.PreferencesHelper;
import com.android.mb.junda.utils.ToastHelper;
import com.android.mb.junda.widget.DividerItemDecoration;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;


public class Log2Fragment extends Fragment {
    private int currentPage = 1;
    private List<Log2> dataList = new ArrayList<>();
    private Log2Adapter log2Adapter;
    private PullLoadMoreRecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getLogInfo();
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLinearLayout();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setPullRefreshEnable(true);
        recyclerView.setPushRefreshEnable(true);
        log2Adapter = new Log2Adapter(R.layout.item_log2,dataList);
        recyclerView.setAdapter(log2Adapter);
        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                currentPage++;
                getLogInfo();
            }
        });
    }

    private void refresh(){
        recyclerView.setPushRefreshEnable(true);
        currentPage = 1;
        dataList.clear();
        getLogInfo();
    }

    /**
     * type 类型：1登录日志；2收款日志
     */
    private void getLogInfo(){
        String userName = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_USERNAME);
        String token = PreferencesHelper.getInstance().getString(ProjectConstants.Preferences.KEY_CURRENT_TOKEN);
        EasyHttp.post(ProjectConstants.Url.GET_LOG_INFO)
                .params("username",userName)
                .params("token",token)
                .params("type","1")
                .params("page",String.valueOf(currentPage))
                .execute(new CallBackProxy<CustomApiResult<String>, String>(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (Helper.isNotEmpty(e.getMessage())){
                            ToastHelper.showToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String response) {
                        Log2Resp log2Resp = JsonHelper.fromJson(response,Log2Resp.class);
                        if (log2Resp!=null){
                            recyclerView.setPullLoadMoreCompleted();
                            if (currentPage==1){
                                if (Helper.isEmpty(log2Resp.getData())){
                                    //TODO showEmpty
                                }else{
                                    dataList.addAll(log2Resp.getData());
                                    log2Adapter.setNewData(dataList);
                                }
                            }else{
                                if (Helper.isEmpty(log2Resp.getData())){
                                    recyclerView.setPushRefreshEnable(false);
                                }else{
                                    dataList.addAll(log2Resp.getData());
                                    log2Adapter.setNewData(dataList);
                                }
                            }
                        }
                    }
                }) {
                });
    }
}
