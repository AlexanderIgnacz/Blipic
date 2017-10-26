package com.trekr.Blipic.Models;

import java.util.HashMap;
import java.util.Map;

public class BLPCategory {
    public String id;
    public String name;
    public String buttonImage;
    public String wallpaper;

    public Map<String, BLPSubCategory> dictSubCategories =  new HashMap<String, BLPSubCategory>();

    public BLPCategory(String id, String name, String buttonImage, String wallpaper) {
        this.id = id;
        this.name = name;
        this.buttonImage = buttonImage;
        this.wallpaper = wallpaper;

        switch (Integer.parseInt(id)) {
            case 1: //mountain
                dictSubCategories.put("key-0", new BLPSubCategory("1", "Hike", "scg_land_hike"));
                dictSubCategories.put("key-1", new BLPSubCategory("2", "Camp", "scg_land_camp"));
                dictSubCategories.put("key-2", new BLPSubCategory("3", "Climb", "scg_land_climb"));
                dictSubCategories.put("key-3", new BLPSubCategory("4", "Walk", "scg_land_walk"));
                dictSubCategories.put("key-4", new BLPSubCategory("5", "Run", "scg_land_run"));
                dictSubCategories.put("key-5", new BLPSubCategory("6", "Hunting", "scg_land_hunting"));
                dictSubCategories.put("key-6", new BLPSubCategory("7", "Archery Bow Hunt", "scg_land_archery"));
                dictSubCategories.put("key-7", new BLPSubCategory("8", "Horseback Ride", "scg_land_horse"));
                dictSubCategories.put("key-8", new BLPSubCategory("9", "Bootcamp Crossfit", "scg_land_bootcamp"));
                dictSubCategories.put("key-9", new BLPSubCategory("10", "Yoga", "scg_land_yoga"));
                break;
            case 2: //snow
                dictSubCategories.put("key-0", new BLPSubCategory("1", "Snow Board", "scg_snow_snow_board"));
                dictSubCategories.put("key-1", new BLPSubCategory("2", "Snowmobile", "scg_snow_snow_mobile"));
                dictSubCategories.put("key-2", new BLPSubCategory("3", "Cross_Country Ski", "scg_snow_cross_country_ski"));
                dictSubCategories.put("key-3", new BLPSubCategory("4", "Skate", "scg_snow_skate"));
                dictSubCategories.put("key-4", new BLPSubCategory("5", "Ski", "scg_snow_ski"));
                dictSubCategories.put("key-5", new BLPSubCategory("6", "Snowshoe", "scg_snow_snow_shoe"));
                dictSubCategories.put("key-6", new BLPSubCategory("7", "empty", "scg_snow_empty1"));
                dictSubCategories.put("key-7", new BLPSubCategory("8", "empty", "scg_misc"));
                dictSubCategories.put("key-8", new BLPSubCategory("9", "Sled", "scg_snow_sled"));
                dictSubCategories.put("key-9", new BLPSubCategory("10", "Ice Hockey", "scg_snow_icehockey"));
                break;
            case 3: //wheels
                dictSubCategories.put("key-0", new BLPSubCategory("1", "Motorcycle", "scg_wheels_motor_cycle"));
                dictSubCategories.put("key-1", new BLPSubCategory("2", "ATV", "scg_wheels_atv"));
                dictSubCategories.put("key-2", new BLPSubCategory("3", "Skateboard", "scg_wheels_skate_board"));
                dictSubCategories.put("key-3", new BLPSubCategory("4", "DirtBike", "scg_wheels_dirt_bike"));
                dictSubCategories.put("key-4", new BLPSubCategory("5", "Cycle", "scg_wheels_cycle"));
                dictSubCategories.put("key-5", new BLPSubCategory("6", "4x4", "scg_wheels_4x4"));
                dictSubCategories.put("key-6", new BLPSubCategory("7", "empty", "scg_snow_empty1"));
                dictSubCategories.put("key-7", new BLPSubCategory("8", "empty", "scg_misc"));
                dictSubCategories.put("key-8", new BLPSubCategory("9", "empty", "scg_snow_empty3"));
                dictSubCategories.put("key-9", new BLPSubCategory("10", "Rollerblade", "scg_wheels_roller_blade"));
                break;
            case 4: //water
                dictSubCategories.put("key-0", new BLPSubCategory("1", "Kayak", "scg_water_kayak"));
                dictSubCategories.put("key-1", new BLPSubCategory("2", "Swim", "scg_water_swim"));
                dictSubCategories.put("key-2", new BLPSubCategory("3", "Cross_Country Stand Up Paddleboarding", "scg_water_paddle"));
                dictSubCategories.put("key-3", new BLPSubCategory("4", "Water Ski", "scg_water_water_ski"));
                dictSubCategories.put("key-4", new BLPSubCategory("5", "Wakeboarding", "scg_water_wake_boarding"));
                dictSubCategories.put("key-5", new BLPSubCategory("6", "Sail", "scg_water_sail"));
                dictSubCategories.put("key-6", new BLPSubCategory("7", "Power Boating", "scg_water_power_boating"));
                dictSubCategories.put("key-7", new BLPSubCategory("8", "Fish", "scg_water_fish"));
                dictSubCategories.put("key-8", new BLPSubCategory("9", "Surf", "scg_water_surf"));
                dictSubCategories.put("key-9", new BLPSubCategory("10", "Wind Surf", "scg_water_wind_surf"));
                break;
        }
    }
}
