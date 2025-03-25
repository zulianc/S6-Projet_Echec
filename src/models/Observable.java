package models;

import views.Observer;

import java.util.LinkedList;
import java.util.List;

public abstract class Observable {
    private final List<Observer> observers = new LinkedList<>();

    public Observable() {}

    public Observable(List<Observer> observers) {}

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void updateOneWithParams(Object[] params, Observer observer) {
        observer.updateParams(params);
    }

    public void updateAll() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
