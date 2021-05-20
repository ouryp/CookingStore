package com.oury.tuto.cookingstore.room;

public interface CookingRoomEvent {
    void onInsertCallback(long id);
    void onUpdateCallback();
}
