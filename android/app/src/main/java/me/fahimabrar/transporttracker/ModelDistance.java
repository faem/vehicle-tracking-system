package me.fahimabrar.transporttracker;

/**
 * Created by FAHIM on 5/1/2018.
 */

class ModelDistance {
    private float dist, dist24;
    private long lastTime;

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    public float getDist24() {
        return dist24;
    }

    public void setDist24(float dist24) {
        this.dist24 = dist24;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
