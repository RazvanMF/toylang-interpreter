package com.example.toyinterpreterguiversion_overdrive.utils.dictionary;

import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import javafx.beans.property.SimpleStringProperty;

public class SymTableValueClass {
    private final SimpleStringProperty identifier;
    private final SimpleStringProperty value;

    public SymTableValueClass(String id, IValue val) {
        this.identifier = new SimpleStringProperty(id);
        this.value = new SimpleStringProperty(val.toString());
    }

    public String getIdentifier() {
        return identifier.get();
    }
    public void setIdentifier(String iden) {
        identifier.set(iden);
    }
    public SimpleStringProperty getIdentifierProperty() {
        return identifier;
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
