package com.boredream.interiodesign.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boredream.bdcodehelper.adapter.ListDropDownAdapter;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.bdcodehelper.view.DropDownMenu;
import com.boredream.interiodesign.R;
import com.boredream.interiodesign.adapter.CommunityAdapter;
import com.boredream.interiodesign.base.BaseFragment;
import com.boredream.interiodesign.constants.CommonConstants;
import com.boredream.interiodesign.entity.Community;
import com.boredream.interiodesign.net.HttpRequest;
import com.boredream.interiodesign.net.SimpleSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class CommunityFragment extends BaseFragment {

    private View view;
    private MultiPageLoadPresent multiPageLoadPresent;
    private ArrayList<Community> datas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_community, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        new TitleBuilder(view).setTitleText(getString(R.string.tab1));

        initDropDownMenu();
    }

    private void initData() {
        loadData();
    }

    private DropDownMenu ddm;

    private String headers[] = {"所有行政区", "所有类型"};
    private List<String> areas = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private String currentArea = headers[0];
    private String currentType = headers[1];
    private List<View> popupViews = new ArrayList<>();

    private ListDropDownAdapter areaAdapter;
    private ListDropDownAdapter typeAdapter;

    private void initDropDownMenu() {
        ddm = (DropDownMenu) view.findViewById(R.id.ddm);

        //init check in status menu
        final ListView checkinStatusView = (ListView) View.inflate(
                activity, R.layout.listview_dropdown_menu, null);
        areas.add(headers[0]);
        areas.add("田家庵区");
        areas.add("山南新区");
        areas.add("大通区");
        areas.add("谢家集区");
        areas.add("八公山区");
        areas.add("凤台县");
        areas.add("潘集区");
        areas.add("经开区");
        areas.add("毛集区");
        areas.add("寿县");
        areaAdapter = new ListDropDownAdapter(activity, areas);
        checkinStatusView.setAdapter(areaAdapter);

        //init age menu
        final ListView ageView = (ListView) View.inflate(
                activity, R.layout.listview_dropdown_menu, null);
        types.add(headers[1]);
        types.add("住宅");
        types.add("商铺");
        types.add("写字楼");
        types.add("别墅");
        types.add("公寓");
        types.add("其他");
        typeAdapter = new ListDropDownAdapter(activity, types);
        ageView.setAdapter(typeAdapter);

        //init popupViews
        popupViews.add(checkinStatusView);
        popupViews.add(ageView);

        //add item click event
        checkinStatusView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaAdapter.setCheckItem(position);
                currentArea = areas.get(position);
                ddm.setTabText(currentArea);
                ddm.closeMenu();

                loadData();
            }
        });

        ageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                currentType = types.get(position);
                ddm.setTabText(currentType);
                ddm.closeMenu();

                loadData();
            }
        });

        View include_refresh_list = View.inflate(activity, R.layout.include_refresh_list, null);
        multiPageLoadPresent = new MultiPageLoadPresent(activity, include_refresh_list.findViewById(R.id.srl));

        //init context view
        include_refresh_list.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        //init dropdownview
        ddm.setDropDownMenu(Arrays.asList(headers), popupViews, include_refresh_list);
    }

    private void loadData() {
        datas.clear();

        CommunityAdapter adapter = new CommunityAdapter(activity, datas);
        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent.load(adapter, datas, pageIndex,
                new MultiPageRequest<ListResponse<Community>>() {
                    @Override
                    public Observable<ListResponse<Community>> request(int page) {
                        String area = currentArea.equals(headers[0]) ? null : currentArea;
                        ArrayList<String> type = null;
                        if(!currentType.equals(headers[1])) {
                            type = new ArrayList<>();
                            type.add(currentType);
                        }
                        return HttpRequest.getCommunity(page, area, type);
                    }
                },
                new SimpleSubscriber<ListResponse<Community>>(activity) {
                    @Override
                    public void onNext(ListResponse<Community> response) {
                        //
                    }
                });
    }
}
