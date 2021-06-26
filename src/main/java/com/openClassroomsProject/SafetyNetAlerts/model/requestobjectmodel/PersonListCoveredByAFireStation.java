package com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel;

import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonCoveredByAFireStation;
import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;

@Data
public class PersonListCoveredByAFireStation {
    private ArrayList<PersonCoveredByAFireStation> list;
    private HashMap<String, String> numberOfAdultsAndChildren;

    public PersonListCoveredByAFireStation(ArrayList<PersonCoveredByAFireStation> list, HashMap<String, String> numberOfAdultsAndChildren) {
        this.list = list;
        this.numberOfAdultsAndChildren = numberOfAdultsAndChildren;
    }
}