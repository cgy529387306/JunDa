package com.android.mb.zzha.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mb.zzha.R;
import com.android.mb.zzha.activity.LoginActivity;
import com.android.mb.zzha.adapter.Log1Adapter;
import com.android.mb.zzha.constants.ProjectConstants;
import com.android.mb.zzha.entity.CustomApiResult;
import com.android.mb.zzha.entity.Log1;
import com.android.mb.zzha.entity.Log1Resp;
import com.android.mb.zzha.utils.Helper;
import com.android.mb.zzha.utils.JsonHelper;
import com.android.mb.zzha.utils.NavigationHelper;
import com.android.mb.zzha.utils.PreferencesHelper;
import com.android.mb.zzha.utils.ToastHelper;
import com.android.mb.zzha.widget.DividerItemDecoration;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;


public class Log1Fragment extends Fragment {
    private int currentPage = 1;
    private List<Log1> dataList = new ArrayList<>();
    private Log1Adapter log1Adapter;
    private PullLoadMoreRecyclerView recyclerView;
    private TextView tvEmpty;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getLogInfo();
    }

    private void initView(View view){
        tvEmpty = view.findViewById(R.id.tv_empty);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLinearLayout();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setPullRefreshEnable(true);
        recyclerView.setPushRefreshEnable(true);
        log1Adapter = new Log1Adapter(R.layout.item_log1,dataList);
        recyclerView.setAdapter(log1Adapter);
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
                .params("type","2")
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
                        Log1Resp log1Resp = JsonHelper.fromJson(response,Log1Resp.class);
                        if (log1Resp!=null){
                            if (log1Resp.getStatus()==1){
                                ToastHelper.showToast(log1Resp.getMsg());
                                PreferencesHelper.getInstance().putBoolean(ProjectConstants.Preferences.KEY_IS_LOGIN,false);
                                PreferencesHelper.getInstance().putString(ProjectConstants.Preferences.KEY_CURRENT_TOKEN,"");
                                NavigationHelper.startActivity(getActivity(),LoginActivity.class,null,true);
                            }else{
                                recyclerView.setPullLoadMoreCompleted();
                                if (currentPage==1){
                                    if (Helper.isEmpty(log1Resp.getData())){
                                        tvEmpty.setVisibility(View.VISIBLE);
                                    }else{
                                        tvEmpty.setVisibility(View.GONE);
                                        dataList.addAll(log1Resp.getData());
                                        log1Adapter.setNewData(dataList);
                                    }
                                }else{
                                    if (Helper.isEmpty(log1Resp.getData())){
                                        recyclerView.setPushRefreshEnable(false);
                                    }else{
                                        dataList.addAll(log1Resp.getData());
                                        log1Adapter.setNewData(dataList);
                                    }
                                }
                            }

                        }
                    }
                }) {
                });
    }
}
