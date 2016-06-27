package com.boredream.interiodesign.entity;

import com.boredream.interiodesign.base.BaseEntity;

import java.util.ArrayList;

/**
 * 小区
 */
public class Community extends BaseEntity {

    /**
     * 地理坐标
     */
    private String map_location;

    /**
     * 小区名
     */
    private String name;

    /**
     * 公摊面积
     */
    private String public_offset;

    /**
     * 地址
     */
    private String address;

    /**
     * 地区
     */
    private String area;

    /**
     * 小区类型: 住宅/公寓/商铺
     */
    private ArrayList<String> community_types;

    /**
     * 小区id
     */
    private String community_id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMap_location() {
        return map_location;
    }

    public void setMap_location(String map_location) {
        this.map_location = map_location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublic_offset() {
        return public_offset;
    }

    public void setPublic_offset(String public_offset) {
        this.public_offset = public_offset;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public ArrayList<String> getCommunity_types() {
        return community_types;
    }

    public void setCommunity_types(ArrayList<String> community_types) {
        this.community_types = community_types;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCommunityTypeStr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < community_types.size(); i++) {
            if (i > 0) {
                sb.append("、");
            }
            sb.append(community_types.get(i));
        }
        return sb.toString();
    }
}
