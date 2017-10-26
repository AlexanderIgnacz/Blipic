package com.trekr.Common.LoadMoreRecycler;

import java.util.ArrayList;
import java.util.List;

public class DataView {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static List<DataView> getDataViews(int size, int loadedCount) {
        List<DataView> dataViews=new ArrayList<>();
        for(int i=0;i<size;i++) {
            DataView dataView=new DataView();
            dataView.setName("Test: " + (i + loadedCount));
            dataViews.add(dataView);
        }
        return dataViews;
    }
}
