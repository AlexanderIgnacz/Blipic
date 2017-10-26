package com.trekr.ApiService.ApiModel;

import com.trekr.Utils.Utils;

import java.util.Date;
import java.util.List;

public class BlipModel {
    public static class BLPLocation {
        public List<Double> coordinates;

        public double getLat() {
            return coordinates.get(1);
        }
        public double getLon() {
            return coordinates.get(0);
        }
    }

    public static class BLPActivity {
        public String id;
        public String name;
        public String description;
        public String website;
        public String repetition;
        public String privacy;
        public String image;
        public String date;
        public BLPUser createdByUser;
        public BLPActivityType activityType;
        public BLPLocation location;
        public List<BLPUser> users;

        public String getActivityId() {
            return id;
        }

        public String getActivityTitle() {
            return name;
        }

        public String getShortDescription() {
            return description;
        }

        public String getActivityImage() {
            return image;
        }

        public Date getDate() {
            return Utils.dateFromString(date);
        }
    }

    public static class BLPActivityType {
        public String id;
        public String title;
    }

    public static class BlipPlace {
        String place_id;
    }

    public List<BLPBlip> data;
}
