package com.epam.dataservice;


import com.epam.data.RoadAccident;
import com.epam.data.TimeOfDay;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AccidentBatchEnricher implements Runnable {

    private PoliceForceService policeForceService;
    private BlockingQueue<List<RoadAccident>> inboundQueue;
    private BlockingQueue<List<RoadAccident>> daytimeOutboundQueue;
    private BlockingQueue<List<RoadAccident>> nighttimeOutboundQueue;

    public AccidentBatchEnricher(PoliceForceService policeForceService,
                                 BlockingQueue<List<RoadAccident>> inboundQueue,
                                 BlockingQueue<List<RoadAccident>> daytimeOutboundQueue,
                                 BlockingQueue<List<RoadAccident>> nighttimeOutboundQueue) {
        this.policeForceService = policeForceService;
        this.inboundQueue = inboundQueue;
        this.daytimeOutboundQueue = daytimeOutboundQueue;
        this.nighttimeOutboundQueue = nighttimeOutboundQueue;
    }


    @Override
    public void run() {
        try {
            long accidentCounter = 0;
            while(true){
                List<RoadAccident> roadAccidents = inboundQueue.take();
                accidentCounter += roadAccidents.size();
                List<RoadAccident> daytimeRoadAccidents = new ArrayList<RoadAccident>();
                List<RoadAccident> nighttimeRoadAccidents = new ArrayList<RoadAccident>();
                roadAccidents.forEach(roadAccident -> {
                        enrich(roadAccident);
                        switch (roadAccident.getDayTime()){
                            case MORNING:
                            case AFTERNOON:
                                daytimeRoadAccidents.add(roadAccident);
                                break;
                            case EVENING:
                            case NIGHT:
                                nighttimeRoadAccidents.add(roadAccident);
                        }
                });
                if(!daytimeRoadAccidents.isEmpty()) daytimeOutboundQueue.put(daytimeRoadAccidents);
                if(!nighttimeRoadAccidents.isEmpty())nighttimeOutboundQueue.put(nighttimeRoadAccidents);
                System.out.println("Enriched ["+ accidentCounter +"] accidents" );
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enrich(RoadAccident roadAccident){
        String forceContact = policeForceService.getContactNo(roadAccident.getPoliceForce());
        roadAccident.setForceContact(forceContact);
//        System.out.println("forceContact: "+ forceContact);

        TimeOfDay dayTime = TimeOfDay.of(roadAccident.getTime().getHour());
        roadAccident.setDayTime(dayTime);
//        System.out.println("dayTime: "+ dayTime);
    }
}