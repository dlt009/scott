package com.teamoptimal.cse110project.data;

import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAutoGeneratedKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIgnore;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.teamoptimal.cse110project.CreateRestroomActivity;
import com.teamoptimal.cse110project.MainActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "Y4R_Restrooms")
public class Restroom {
    private static final String TAG = "Restroom";
    private String id;
    private double longitude;
    private double latitude;
    private String userEmail;
    private String description;
    private String tags ="0000000000000000000000000000000";
    private String floor;
    private double rating;
    private int ratingsCount;
    private float color;


    public Restroom () {
        userEmail = "";
        longitude = 0.0d;
        latitude = 0.0d;
        description = "";
        tags = "0000000000000000000000000000000";
        floor = "1";
        rating = 0.00;
        ratingsCount = 0;
    }

    @DynamoDBHashKey (attributeName = "ID")
    @DynamoDBAutoGeneratedKey
    public String getID() { return id; }
    public void setID(String id) { this.id = id; }

    @DynamoDBAttribute (attributeName = "User")
    public String getUser() { return userEmail; }
    public void setUser(String userEmail) { this.userEmail = userEmail; }

    @DynamoDBAttribute (attributeName = "Latitude")
    public double getLatitude() { return latitude;}
    public void setLatitude(double latitude) { this.latitude = latitude; }

    @DynamoDBAttribute(attributeName = "Longitude")
    public double getLongitude() { return longitude;}
    public void setLongitude(double longitude) { this.longitude = longitude;}

    @DynamoDBAttribute (attributeName = "Description")
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @DynamoDBAttribute (attributeName = "Tags")
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    @DynamoDBAttribute (attributeName = "Floor")
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    @DynamoDBAttribute(attributeName = "Rating")
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    @DynamoDBAttribute(attributeName = "NumberOfRatings")
    public int getRatingsCount() { return ratingsCount; }
    public void setRatingsCount(int ratingsCount) { this.ratingsCount = ratingsCount; }

    @DynamoDBAttribute(attributeName = "Color")
    public float getColor() {return color;}
    public void setColor(float color) {this.color = color;Log.d(TAG, "color of marker");}

    @DynamoDBIgnore
    public void setTag(int index, boolean choice) {
        char[] chars = tags.toCharArray();
        if(choice) { chars[index] = '1'; }
        else { chars[index] = '0'; }
        tags = chars.toString();
    }

    @DynamoDBIgnore
    public double[] getLocation() {
        double[] location = new double[2];
        location[0] = latitude;
        location[1] = longitude;
        return location;
    }

    @DynamoDBIgnore
    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @DynamoDBIgnore
    public void updateRating(double review) {
        double sum = review + (rating * ratingsCount);
        double val = sum / (++ratingsCount);
        setRating(val);
    }

    @DynamoDBIgnore
    public boolean isInitialized() {
        if(longitude != 0.0d && latitude != 0.0d && !userEmail.equals("") && !description.equals(""))
            return true;
        return false;
    }

    public void create() {
        AmazonDynamoDBClient ddb = CreateRestroomActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        mapper.save(this);
    }

    @DynamoDBIgnore
    public static List<Restroom> getRestrooms(double latitude, double longitude, double radius) {
        Log.d(TAG, "getRestrooms");
        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        Condition minLatitude = new Condition()
                .withComparisonOperator(ComparisonOperator.GE)
                .withAttributeValueList(new AttributeValue().withN("" + (latitude - (radius / 2))));
        Condition maxLatitude = new Condition()
                .withComparisonOperator(ComparisonOperator.LE)
                .withAttributeValueList(new AttributeValue().withN("" + (latitude + (radius / 2))));
        Condition minLongitude = new Condition()
                .withComparisonOperator(ComparisonOperator.GE)
                .withAttributeValueList(new AttributeValue().withN("" + (longitude - (radius / 2))));
        Condition maxLongitude = new Condition()
                .withComparisonOperator(ComparisonOperator.LE)
                .withAttributeValueList(new AttributeValue().withN("" + (longitude + (radius / 2))));

        Map<String, Condition> conditions = new HashMap<String, Condition>();
        conditions.put("Latitude", minLatitude);
        conditions.put("Latitude", maxLatitude);
        conditions.put("Longitude", minLongitude);
        conditions.put("Longitude", maxLongitude);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.setScanFilter(conditions);

        List<Restroom> scanResult = mapper.scan(Restroom.class, scanExpression);
        Log.d(TAG, "scanResult, " + scanResult.size());
        return scanResult;
    }
}