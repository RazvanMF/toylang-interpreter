package com.example.toyinterpreterguiversion_overdrive.utils.heap;

import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import javafx.beans.property.SimpleStringProperty;

public class HeapValueClass {
    private final SimpleStringProperty address;
    private final SimpleStringProperty value;

    public HeapValueClass(Integer addr, IValue val) {
        this.address = new SimpleStringProperty(String.valueOf(addr));
        this.value = new SimpleStringProperty(val.toString());
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

    public String getValue() {
        return value.get();
    }
    public void setValue(IValue val) {
        value.set(val.toString());
    }
    public SimpleStringProperty getValueProperty() {
        return value;
    }
}
