package com.example.toyinterpreterguiversion_overdrive.utils.latchtable;

import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import javafx.beans.property.SimpleStringProperty;

public class LatchTableValueClass {
    private final SimpleStringProperty address;
    private final SimpleStringProperty value;

    public LatchTableValueClass(Integer addr, Integer val) {
        this.address = new SimpleStringProperty(String.valueOf(addr));
        this.value = new SimpleStringProperty(String.valueOf(val));
    }

    public String getAddress() {
        return address.get();
    }
    public SimpleStringProperty getAddressProperty() {
        return address;
    }

    public String getValue() {
        return value.get();
    }
    public SimpleStringProperty getValueProperty() {
        return value;
    }
}
