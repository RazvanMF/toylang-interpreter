package com.example.toyinterpreterguiversion_overdrive.utils.proceduretable;

import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;
import javafx.beans.property.SimpleStringProperty;

public class ProcTableValueClass {
    private final SimpleStringProperty signature;
    private final SimpleStringProperty body;

    public ProcTableValueClass(String name, ListADT<String> params, IStatement body) {
        this.signature = new SimpleStringProperty(String.format("%s(%s)", name, listFormatter(params)));
        this.body = new SimpleStringProperty(body.toString());
    }

    private String listFormatter(ListADT<String> params) {
        StringBuilder out = new StringBuilder();
        for (String name : params.getList()) {
            out.append(name); out.append(", ");
        }
        out.setLength(out.length() - 2);
        return out.toString();
    }

    public String getSignature() {
        return signature.get();
    }
    public void setSignature(String sign) {
        signature.set(sign);
    }
    public SimpleStringProperty getSignatureProperty() {
        return signature;
    }

    public String getBody() {
        return body.get();
    }
    public void setBody(IStatement val) {
        body.set(val.toString());
    }
    public SimpleStringProperty getBodyProperty() {
        return body;
    }
}
