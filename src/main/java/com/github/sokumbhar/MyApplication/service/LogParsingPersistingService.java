package com.github.sokumbhar.MyApplication.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.interview.anandankush0908.loganalyzer.model.Event;
import com.interview.anandankush0908.loganalyzer.model.EventState;
import com.interview.anandankush0908.loganalyzer.persistence.model.Alert;
import com.interview.anandankush0908.loganalyzer.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
public class LogParsingPersistingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogParsingPersistingService.class);

    @Autowired
    private AlertRepository alertRepository;

    //@Autowired
    //private ThresholdConfig thresholdConfig;
    //Or
    @Value("${time-threshold}")
    private int timeThreshold;

    @Value("${data-structure-threshold}")
    private int dataStructureThreshold;

    public void parseAndPersistEvents(String filePath) throws IOException {
        //Maintain a map of events
        Map<String, Event> eventsMap = new HashMap<>();

        //Maintain a map of alerts
        Map<String, Alert> alertsMap = new HashMap<>();

        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(filePath);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                //Read a line from the file
                String json = sc.nextLine();

                try {
                    //Convert to Event
                    Event event = new Gson().fromJson(json, Event.class);

                    //Check if already exists in Map
                    if (eventsMap.containsKey(event.getId())) {
                        Event oldEvent = eventsMap.get(event.getId());

                        //Identify which is STARTED and which is FINISHED
                        Event startEvent = null;
                        if (EventState.STARTED.equals(event.getState()))
                            startEvent = event;

                        if (EventState.STARTED.equals(oldEvent.getState()))
                            startEvent = oldEvent;

                        Event finishedEvent = null;
                        if (EventState.FINISHED.equals(event.getState()))
                            finishedEvent = event;

                        if (EventState.FINISHED.equals(oldEvent.getState()))
                            finishedEvent = oldEvent;

                        if (startEvent == null || finishedEvent == null)
                            throw new NullPointerException();

                        //Get the execution time
                        long executionTime = finishedEvent.getTimestamp() - startEvent.getTimestamp();

                        //Create an Alert
                        Alert alert = new Alert(event.getId(), new Long(executionTime).intValue(), event.getType(), event.getHost(), false);

                        // if the execution time is more than the specified threshold, flag the alert as TRUE
                        if (executionTime > timeThreshold) {
                            alert.setFlagAlert(Boolean.TRUE);
                            LOGGER.trace("The execution time for the event {} is {}ms", event.getId(), executionTime);
                        }

                        //Add in AlertsMap
                        alertsMap.put(event.getId(), alert);

                        // remove from the EventsMap
                        eventsMap.remove(event.getId());
                    } else {
                        eventsMap.put(event.getId(), event);
                    }
                } catch (JsonParseException ex) {
                    LOGGER.error("Unable to parse the event: {}", ex.getMessage());
                }
            }
            if (alertsMap.size() != 0) {
                LOGGER.debug("Saving alerts in DB. The size of alert map is: {}", alertsMap.size());
                alertRepository.saveAll(alertsMap.values());
            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }
}
