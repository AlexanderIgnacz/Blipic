package com.trekr.ApiService.ApiModel;

import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.trekr.Utils.Utils;

import java.util.Date;
import java.util.List;

public class BLPBlip implements ClusterItem {
    public enum BLPBlipTypes{
        BLPBlipTypeActivity,
        BLPBlipTypeDestination
    };

    //region Model datas from api response
    public String _id;
    private String blipId;
    public String gPoint;
    public BlipModel.BlipPlace place;
    public String name;
    public String shortDescription;
    public String longDescription;
    public String website;
    public String type;
    public double googleRating;
    public String date;
    public String hostedName;
    public String hostedIcon;
    public double timelapse;
    public double distance;
    public boolean isLiked;
    public boolean isFavorite;
    public boolean isAttending;
    public double likesCount;
    public double attendingCount;
    public double friendsAttendingCount;
    public double upcomingActivitiesCount;
    public BLPUser author;
    public BlipModel.BLPLocation location;
    public BlipModel.BLPActivity activity;
    public List<BLPUser> usersGoing;
    public List<String> activityTypes;
    public List<BlipModel.BLPActivity> nearActivities;
    public List<BLPUser> favorites;
    public List<BLPUser> likes;
    public List<UserModel.BLPImage> images;
    public UserModel.BLPVideo video;
    public Boolean exactName;
    public String id;
    public String formatted_street;
    public String neighborhood;
    public String locality;
    public String city;
    public String state;
    public String zip;
    public String formatted_address;
    //endregion

    // Custom data models
    private String _blipId, _type, _pointId;
    public String geofencingBlipId;
    public boolean fromGoogle;
    private LatLng coordinate;

    public String getBlipId() {
        if (!TextUtils.isEmpty(_blipId)) {
            return _blipId;
        }
        if (!TextUtils.isEmpty(_id)) {
            _blipId = _id;
        } else if (!TextUtils.isEmpty(blipId)) {
            _blipId = blipId;
            geofencingBlipId = blipId;
        } else {
            _blipId = place.place_id;
        }
        return _blipId;
    }

    public String getType() {
        if (!TextUtils.isEmpty((_type))) {
            return _type;
        }
        switch (type) {
            case "location":
            case "activity":
                _type = type;
                break;
            case "event":
                _type = "activity";
                break;
            default:
                _type = "location";
                fromGoogle = true;
                break;
        }
        return _type;
    }

    public String getPointId() {
        if (!TextUtils.isEmpty((_pointId))) {
            return _pointId;
        }
        if (type.equals("location") && !TextUtils.isEmpty(gPoint)) {
            _pointId = gPoint;
        } else if (type.equals("location") && !TextUtils.isEmpty(_id)) {
            _pointId = _id;
        }
        return _pointId;
    }

    public Date getDate() {
        if (!TextUtils.isEmpty(date)) {
            return Utils.dateFromString(date);
        }
        return null;
    }

    public boolean getLike() {
        return isLiked;
    }

    public void setLike(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public String getLocationId() {
        return id;
    }

    public BLPUser getCreatedByUser() {
        return author;
    }

    public LatLng getCoordinate() {
        if (coordinate != null)
            return coordinate;
        coordinate = new LatLng(location.getLat(), location.getLon());
        return coordinate;
    }

    public void setCoordinate(LatLng _coordinate) {
        coordinate = _coordinate;
    }

    private String[] kBLPBlipTypes = {"activity", "location", null};

    public boolean isKindOfBlip(BLPBlipTypes enumVal) {
        return type.equals(kBLPBlipTypes[enumVal.ordinal()]);
    }

    public String dmsLatitudeFromDecimalDegrees() {
        double degrees = location.getLat();
        double minutes = (degrees - (int)degrees) * 60;
        double seconds = (minutes - (int)minutes) * 60;

        String direction = (degrees < 0) ? "S" : "N";

        return String.format("%d° %d' %d\" %s",
            Math.abs((int)degrees),
            Math.abs((int)minutes),
            Math.abs((int)seconds),
            direction);
    }

    public String dmsLongitudeFromDecimalDegrees() {
        double degrees = location.getLon();
        double minutes = (degrees - (int)degrees) * 60;
        double seconds = (minutes - (int)minutes) * 60;

        String direction = (degrees < 0) ? "W" : "E";

        return String.format("%d° %d' %d\" %s",
            Math.abs((int)degrees),
            Math.abs((int)minutes),
            Math.abs((int)seconds),
            direction);
    }

    @Override
    public LatLng getPosition() {
        return getCoordinate();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
