package zy.example.appa;


import java.util.Observable;

public final class DataManager extends Observable {

    private String data = "Init by App A Module";

    private DataManager() {
    }

    public static DataManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final DataManager INSTANCE = new DataManager();
    }

    public String getData() {
        return data;
    }

    public void resetData() {
        setChanged();
        data = "Init by App A Module";
    }
}
