package com.example.toyinterpreterguiversion_overdrive.utils.locktable;

import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import javafx.beans.property.SimpleStringProperty;

public class LockTableValueClass {
    private final SimpleStringProperty address;
    private final SimpleStringProperty id;

    public LockTableValueClass(Integer addr, Integer val) {
        this.address = new SimpleStringProperty(String.valueOf(addr));
        this.id = new SimpleStringProperty(String.valueOf(val));
    }

    public String getAddress() {
        return address.get();
    }
    public void setAddress(Integer addr) {
        address.set(String.valueOf(addr));
    }
    public SimpleStringProperty getAddressProperty() {
        return address;
    }

    public String getId() {
        return id.get();
    }
    public void setId(IValue val) {
        id.set(val.toString());
    }
    public SimpleStringProperty getIdProperty() {
        return id;
    }
}
