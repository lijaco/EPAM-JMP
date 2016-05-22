package com.epam.dbservice;

import java.util.Date;

import com.epam.data.RoadAccident;

public interface AccidentService {
	
	// scenario 1
    RoadAccident findOne(String accidentId);
    
    // scenario 2
    List<Accident> getAllAccidentsByRoadCondition(String roadCondition);
    
    // scenario 3
    List<Accident> getAllAccidentsByWeatherConditionAndYear(String weatherCondition,String year);
    
    // scenario 4
    List<Accident> getAllAccidentsByDate(Date date);

    Boolean update(Accident accident);

}
