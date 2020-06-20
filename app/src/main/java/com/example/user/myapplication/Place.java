package com.example.user.myapplication;

import java.util.Objects;

public class Place {
    private int type;
    private String title;
    private Position position;
    private String workTime;

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Position getPosition() {
        return position;
    }

    public String getWorkTime() {
        return workTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return type == place.type &&
                title.equals(place.title) &&
                position.equals(place.position) &&
                Objects.equals(workTime, place.workTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, position, workTime);
    }

    @Override
    public String toString() {
        return "Place{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", position=" + position +
                ", workTime='" + workTime + '\'' +
                '}';
    }
}