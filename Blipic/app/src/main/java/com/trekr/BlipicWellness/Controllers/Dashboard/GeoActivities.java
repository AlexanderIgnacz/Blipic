package com.trekr.BlipicWellness.Controllers.Dashboard;

import com.trekr.ApiService.ApiModel.GeoActivitiesModel.DataGeoActivities;
import com.trekr.BlipicWellness.Controllers.Dashboard.EmployeeFiltersView.Filter;

public class GeoActivities {
    public int suns;
    public int destination;
    public int activity;
    public Filter filter;

    public static class GeoActivityPoint {
        int points;
        int circles;
        public GeoActivityPoint(int points, int circles) {
            this.points = points;
            this.circles = circles;
        }
    }

    public GeoActivities(DataGeoActivities model, Filter filter) {
        this.filter = filter;

        suns = model.sunshinePoints;
        destination = model.destinationPoints;
        activity = model.activityPoints;
    }

    public GeoActivityPoint sunPoints() {

        int count = suns / filter.maxSuns;

        return new GeoActivityPoint(suns - count * filter.maxSuns, count);
    }

    public GeoActivityPoint destinationPoints() {
        int count = destination / filter.destinationMax;

        return new GeoActivityPoint(destination - count * filter.destinationMax, count);
    }

    public GeoActivityPoint activityPoints() {
        int count = activity / filter.activityMax;

        return new GeoActivityPoint(activity - count * filter.activityMax, count);
    }

}
