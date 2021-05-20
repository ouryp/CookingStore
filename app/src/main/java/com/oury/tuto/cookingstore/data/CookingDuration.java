package com.oury.tuto.cookingstore.data;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CookingDuration {

    private Duration protocol;
    private Duration cooking;

    public CookingDuration(Duration protocolDuration, Duration cookingDuration) {
        this.protocol = protocolDuration;
        this.cooking = cookingDuration;
    }

    public CookingDuration(List<String> stringList) {
        this.protocol = Duration.parse(stringList.get(0));
        this.cooking = Duration.parse(stringList.get(1));
    }

    public long getProtocolHours() {
        return protocol.toHours();
    }

    public long getProtocolMinutes() {
        if(protocol.toHours() > 0) {
            return protocol.toMinutes() - protocol.toHours()*60;
        } else {
            return protocol.toMinutes();
        }
    }

    public long getCookingHours() {
        return cooking.toHours();
    }

    public long getCookingMinutes() {
        if(cooking.toHours() > 0) {
            return cooking.toMinutes() - cooking.toHours()*60;
        } else {
            return cooking.toMinutes();
        }
    }

    public String print() {
        return "Préparation : " + inHoursMinutes(protocol) + ". Cuisson : " + inHoursMinutes(cooking);
    }

    public List<String> toRoom() {
        List<String> room = new ArrayList<>();
        room.add(protocol.toString());
        room.add(cooking.toString());
        return room;
    }

    public static String inHoursMinutes(Duration duration) {
        if(duration.toHours() != 0) {
            return duration.toHours() + "h " + (duration.toMinutes() - 60*duration.toHours()) + "min";
        } else {
            return duration.toMinutes() + "min";
        }
    }
}
