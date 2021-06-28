package com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel;

import lombok.Data;
import java.util.ArrayList;

@Data
public class ChildrenAndOtherMembers {
    private ArrayList<Children> childrenArrayList;
    private ArrayList<UniqueIdentifier> OtherHouseHoldMembers;

    public ChildrenAndOtherMembers(ArrayList<Children> childrenArrayList, ArrayList<UniqueIdentifier> otherHouseHoldMembers) {
        this.childrenArrayList = childrenArrayList;
        OtherHouseHoldMembers = otherHouseHoldMembers;
    }

    public ChildrenAndOtherMembers() {
    }
}