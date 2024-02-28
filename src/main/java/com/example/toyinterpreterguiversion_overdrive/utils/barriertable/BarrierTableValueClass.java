package com.example.toyinterpreterguiversion_overdrive.utils.barriertable;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class BarrierTableValueClass {
    private final SimpleStringProperty address;
    private final SimpleStringProperty capacity;
    private final SimpleStringProperty subscribers;

    public BarrierTableValueClass(Integer addr, Integer cap, List<Integer> usrs) {
        this.address = new SimpleStringProperty(addr.toString());
        this.capacity = new SimpleStringProperty(cap.toString());
        this.subscribers = new SimpleStringProperty(listFormatter(usrs));
    }

    private String listFormatter(List<Integer> params) {
        StringBuilder out = new StringBuilder();
        if (!params.isEmpty()) {
            for (Integer ID : params) {
                out.append(ID.toString()); out.append(", ");
            }
            out.setLength(out.length() - 2);
        }

        return out.toString();
    }

    public String getAddress() {
        return address.get();
    }
    public SimpleStringProperty getAddressProperty() {
        return address;
    }

    public String getCapacity() {
        return capacity.get();
    }
    public SimpleStringProperty getCapacityProperty() {
        return capacity;
    }

    public String getSubscribers() {
        return subscribers.get();
    }
    public SimpleStringProperty getSubscribersProperty() {
        return subscribers;
    }
}
