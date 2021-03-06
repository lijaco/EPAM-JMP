package com.epam.processor;

import com.epam.data.RoadAccident;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * This is to be completed by mentees
 */
public class DataProcessor {

    private final List<RoadAccident> roadAccidentList;

    public DataProcessor(List<RoadAccident> roadAccidentList){
        this.roadAccidentList = roadAccidentList;
    }


//    First try to solve task using java 7 style for processing collections

    /**
     * Return road accident with matching index
     * @param index
     * @return
     */
    public RoadAccident getAccidentByIndex7(String index){
        for(RoadAccident roadAccident : roadAccidentList) {
            if (roadAccident.getAccidentId().equals(index)) {
                return roadAccident;
            }
        }
        return null;
    }


    /**
     * filter list by longtitude and latitude values, including boundaries
     * @param minLongitude
     * @param maxLongitude
     * @param minLatitude
     * @param maxLatitude
     * @return
     */
    public Collection<RoadAccident> getAccidentsByLocation7(float minLongitude, float maxLongitude, float minLatitude, float maxLatitude){
        List<RoadAccident> roadAccidents = new ArrayList<>();
        for(RoadAccident roadAccident : roadAccidentList) {
            if (isInCoordinate(roadAccident, minLongitude, maxLongitude, minLatitude, maxLatitude)) {
                roadAccidents.add(roadAccident);
            }
        }
        return roadAccidents;
    }

    private boolean isInCoordinate(RoadAccident roadAccident, float minLongitude, float maxLongitude, float minLatitude, float maxLatitude) {
        return roadAccident.getLongitude() >= minLongitude && roadAccident.getLongitude() <= maxLongitude &&
                roadAccident.getLatitude() >= minLatitude && roadAccident.getLatitude() <= maxLatitude;
    }

    /**
     * count incidents by road surface conditions
     * ex:
     * wet -> 2
     * dry -> 5
     * @return
     */
    public Map<String, Long> getCountByRoadSurfaceCondition7(){
        Map<String, Long> roadAccidentCountMaps = new HashMap<String, Long>();
        for(RoadAccident roadAccident : roadAccidentList) {
            String roadSurfaceCondition = roadAccident.getRoadSurfaceConditions();
            Long number = roadAccidentCountMaps.get(roadSurfaceCondition);
            number = number == null ? 1L : (number + 1L);
            roadAccidentCountMaps.put(roadSurfaceCondition, number);
        }
        return roadAccidentCountMaps;
    }

    /**
     * find the weather conditions which caused the top 3 number of incidents
     * as example if there were 10 accidence in rain, 5 in snow, 6 in sunny and 1 in foggy, then your result list should contain {rain, sunny, snow} - top three in decreasing order
     * @return
     */
    public List<String> getTopThreeWeatherCondition7() {
        Map<String, Long> roadAccidentCountMap = new HashMap<String, Long>();
        for(RoadAccident roadAccident : roadAccidentList){
            String weatherCondition = roadAccident.getWeatherConditions();
            Long number = roadAccidentCountMap.get(weatherCondition);
            number = number == null ? 1L : ( number + 1L );
            roadAccidentCountMap.put(weatherCondition, number);
        }
        List<String> weatherConditions = new ArrayList<String>();
        weatherConditions.addAll(roadAccidentCountMap.keySet());
        Collections.sort(weatherConditions, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return roadAccidentCountMap.get(o2).compareTo(roadAccidentCountMap.get(o1));
            }
        });
        List<String> ToThree = new ArrayList<String>();
        for(int i=0;i<3;i++){
            ToThree.add(weatherConditions.get(i));
        }
        return ToThree;
    }

    /**
     * return a multimap where key is a district authority and values are accident ids
     * ex:
     * authority1 -> id1, id2, id3
     * authority2 -> id4, id5
     * @return
     */
    public Multimap<String, String> getAccidentIdsGroupedByAuthority7(){
        Multimap<String, String> authorityMap = ArrayListMultimap.create();
        roadAccidentList.stream().forEach(accident -> authorityMap.put(accident.getDistrictAuthority(), accident.getAccidentId()));
        return authorityMap
                ;
    }


    // Now let's do same tasks but now with streaming api



    public RoadAccident getAccidentByIndex(String index){
        Optional<RoadAccident> roadAccident = roadAccidentList.stream()
                .filter(accident -> accident.getAccidentId().equals(index))
                .findFirst();
        return roadAccident.get() != null ?  roadAccident.get() : null;
    }


    /**
     * filter list by longtitude and latitude fields
     * @param minLongitude
     * @param maxLongitude
     * @param minLatitude
     * @param maxLatitude
     * @return
     */
    public Collection<RoadAccident> getAccidentsByLocation(float minLongitude, float maxLongitude, float minLatitude, float maxLatitude){
        return roadAccidentList.stream()
                .filter(accident -> isInCoordinate(accident, minLongitude, maxLongitude, minLatitude, maxLatitude))
                .collect(toList());
    }

    /**
     * find the weather conditions which caused max number of incidents
     * @return
     */
    public List<String> getTopThreeWeatherCondition(){
    	Map<String, Long> groupbyWeather = roadAccidentList.stream()
                .collect(Collectors.groupingBy(RoadAccident::getWeatherConditions, Collectors.counting()));
        return groupbyWeather.keySet().stream()
                .sorted((w1,w2) -> groupbyWeather.get(w2).compareTo(groupbyWeather.get(w1)))
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * count incidents by road surface conditions
     * @return
     */
    public Map<String, Long> getCountByRoadSurfaceCondition(){
        return roadAccidentList.stream()
                .collect(Collectors.groupingBy(RoadAccident::getRoadSurfaceConditions, Collectors.counting()));
    }

    /**
     * To match streaming operations result, return type is a java collection instead of multimap
     * @return
     */
    public Map<String, List<String>> getAccidentIdsGroupedByAuthority(){
        Map<String, List<String>> authorityMap = new HashMap<String, List<String>>();
        roadAccidentList.stream().forEach(roadAccident -> {
            List<String> authorityList = authorityMap.get(roadAccident.getDistrictAuthority());
            if (authorityList == null) {
                authorityList = new ArrayList<String>();
            }
            authorityList.add(roadAccident.getAccidentId());
            authorityMap.put(roadAccident.getDistrictAuthority(), authorityList);
        });
        return authorityMap;
    }

}
