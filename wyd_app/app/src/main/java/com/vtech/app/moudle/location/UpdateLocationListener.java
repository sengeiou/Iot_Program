package com.vtech.app.moudle.location;

import android.location.Location;

public interface UpdateLocationListener {
    void updateLocationChanged(Location location, int gpsCount);
}
