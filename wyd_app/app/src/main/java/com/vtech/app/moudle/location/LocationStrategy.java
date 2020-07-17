package com.vtech.app.moudle.location;

public interface LocationStrategy {

    void requestLocation();

    void stopLocation();

    void setListener(UpdateLocationListener listener);
}
