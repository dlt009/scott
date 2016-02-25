package com.teamoptimal.cse110project.data;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAutoGeneratedKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIgnore;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.teamoptimal.cse110project.ReportActivity;

@DynamoDBTable (tableName = "Y4R_Reports")
public class Report {
    private String ID;
    private String[] Object;
    private String Target;
    private String Description;
    private String Reporter;

    public Report(){}

    @DynamoDBHashKey (attributeName = "ID")
    @DynamoDBAutoGeneratedKey
    public String getID(){return ID;}
    public void setID(String newID){ID = newID;}

    @DynamoDBAttribute(attributeName = "ObjectType")
    public String getObjectType(){return Object[0];}
    public void setObjectType(String type){Object[0] = type;}

    @DynamoDBAttribute (attributeName = "ObjectID")
    public String getObjectID(){return Object[1];}
    public void setObjectID(String obj){Object[1] = obj;}

    @DynamoDBAttribute (attributeName = "Target")
    public String getTarget(){return Target;}
    public void setTarget(String newT){Target = newT;}

    @DynamoDBAttribute (attributeName = "Decription")
    public String getDescription(){return Description;}
    public void setDescription(String desc){Description = desc;}

    @DynamoDBAttribute (attributeName = "Reporter")
    public String getReporter(){return Reporter;}
    public void setReporter(String report){Reporter = report;}

    @DynamoDBIgnore
    public String[] getObject(){return Object;}
    public void setObject(String type, String obj){
        Object[0] = type;
        Object[1] = obj;
    }


    public void create() {
        AmazonDynamoDBClient ddb = ReportActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        mapper.save(this);
    }


}
