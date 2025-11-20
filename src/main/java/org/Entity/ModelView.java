package org.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ModelView {
    private String view;
    private Map<String, Object> data;

    public ModelView() {
        this.data = new HashMap<>();
    }
 
    public String getView() {
        return this.view;
    }

    public void setView(String v) {
        this.view = v;
    }

    public void addAttribute(String keyString, Object data) {
        this.data.put(keyString, data);
    }

    public Map<String, Object> getAllAttributes() {
        return this.data;
    }
}
