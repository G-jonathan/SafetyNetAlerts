package com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;

@Data
public class PersonListCoveredByAFireStation {
    private ArrayList<PersonCoveredByAFireStation> personCoveredByAFireStation;
    private HashMap<String, String> numberOfAdultsAndChildren;

    public PersonListCoveredByAFireStation(ArrayList<PersonCoveredByAFireStation> list, HashMap<String, String> numberOfAdultsAndChildren) {
        this.personCoveredByAFireStation = list;
        this.numberOfAdultsAndChildren = numberOfAdultsAndChildren;
    }
}