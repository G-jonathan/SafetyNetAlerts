package com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel;

import lombok.Data;
import java.util.ArrayList;

@Data
public class HouseHold {
    private String address;
    private ArrayList<HouseHoldMember> houseHoldMemberArrayList;

    public HouseHold(String address, ArrayList<HouseHoldMember> houseHoldMemberArrayList) {
        this.address = address;
        this.houseHoldMemberArrayList = houseHoldMemberArrayList;
    }

    public HouseHold(String address) {
        this.address = address;
    }
}