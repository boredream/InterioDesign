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
import com.boredream.interiodesign.adapter.HouseTypeAdapter;
import com.boredream.interiodesign.base.BaseFragment;
import com.boredream.interiodesign.constants.CommonConstants;
import com.boredream.interiodesign.entity.HouseType;
import com.boredream.interiodesign.net.HttpRequest;
import com.boredream.interiodesign.net.SimpleSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class HouseTypeFragment extends BaseFragment {

    private View view;
    private MultiPageLoadPresent multiPageLoadPresent;
    private ArrayList<HouseType> datas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_community, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        new TitleBuilder(view).setTitleText(getString(R.string.tab2));

        initDropDownMenu();
    }

    private void initData() {
        loadData();
    }

    private DropDownMenu ddm;

    private String headers[] = {"所有建筑面积", "所有户型"};
    private List<String> sizes = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private String currentSize = headers[0];
    private String currentType = headers[1];
    private List<View> popupViews = new ArrayList<>();

    private ListDropDownAdapter sizeAdapter;
    private ListDropDownAdapter typeAdapter;

    private void initDropDownMenu() {
        ddm = (DropDownMenu) view.findViewById(R.id.ddm);

        final ListView sizeView = (ListView) View.inflate(
                activity, R.layout.listview_dropdown_menu, null);
        sizes.add(headers[0]);
        sizes.add("50㎡以下");
        sizes.add("50-70㎡");
        sizes.add("70-90㎡");
        sizes.add("90-110㎡");
        sizes.add("110-150㎡");
        sizes.add("150-200㎡");
        sizes.add("200-300㎡");
        sizes.add("300㎡以上");
        sizeAdapter = new ListDropDownAdapter(activity, sizes);
        sizeView.setAdapter(sizeAdapter);

        final ListView typeView = (ListView) View.inflate(
                activity, R.layout.listview_dropdown_menu, null);
        types.add(headers[1]);
        types.add("一室");
        types.add("二室");
        types.add("三室");
        types.add("四室");
        types.add("五室及以上");
        typeAdapter = new ListDropDownAdapter(activity, types);
        typeView.setAdapter(typeAdapter);

        //init popupViews
        popupViews.add(sizeView);
        popupViews.add(typeView);

        //add item click event
        sizeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sizeAdapter.setCheckItem(position);
                currentSize = sizes.get(position);
                ddm.setTabText(currentSize);
                ddm.closeMenu();

                loadData();
            }
        });

        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        HouseTypeAdapter adapter = new HouseTypeAdapter(activity, datas);
        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent.load(adapter, datas, pageIndex,
                new MultiPageRequest<ListResponse<HouseType>>() {
                    @Override
                    public Observable<ListResponse<HouseType>> request(int page) {
                        String size = currentSize.equals(headers[0]) ? null : currentSize;
                        String type = currentType.equals(headers[1]) ? null : currentType;
                        return HttpRequest.getHouseType(page, size, type);
                    }
                },
                new SimpleSubscriber<ListResponse<HouseType>>(activity) {
                    @Override
                    public void onNext(ListResponse<HouseType> response) {
                        //
                    }
                });
    }
}
