package edu.uph.m24si2.lapakumkmapp;

import java.util.ArrayList;
import java.util.List;

public class RentalManager {
    private static RentalManager instance;
    private List<RentalRequest> requests;

    private RentalManager() {
        requests = new ArrayList<>();
    }

    public static synchronized RentalManager getInstance() {
        if (instance == null) {
            instance = new RentalManager();
        }
        return instance;
    }

    public void addRequest(RentalRequest request) {
        requests.add(request);
    }

    public List<RentalRequest> getRequests() {
        return requests;
    }

    public List<RentalRequest> getRequestsByStatus(RentalRequest.Status status) {
        List<RentalRequest> filtered = new ArrayList<>();
        for (RentalRequest r : requests) {
            if (r.getStatus() == status) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public void clearAll() {
        requests.clear();
    }
}
