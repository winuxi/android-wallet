package org.telegram.crypto.models;

public class USD {

    String price;
    String volume_24h;
    String percent_change_1h;
    String percent_change_24h;
    String percent_change_7d;
    String percent_change_30d;
    String percent_change_60d;
    String percent_change_90d;
    String market_cap;
    String market_cap_dominance;
    String fully_diluted_market_cap;
    String last_updated;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(String volume_24h) {
        this.volume_24h = volume_24h;
    }

    public String getPercent_change_1h() {
        return percent_change_1h;
    }

    public void setPercent_change_1h(String percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public String getPercent_change_24h() {
        return percent_change_24h;
    }

    public void setPercent_change_24h(String percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public String getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(String percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    public String getPercent_change_30d() {
        return percent_change_30d;
    }

    public void setPercent_change_30d(String percent_change_30d) {
        this.percent_change_30d = percent_change_30d;
    }

    public String getPercent_change_60d() {
        return percent_change_60d;
    }

    public void setPercent_change_60d(String percent_change_60d) {
        this.percent_change_60d = percent_change_60d;
    }

    public String getPercent_change_90d() {
        return percent_change_90d;
    }

    public void setPercent_change_90d(String percent_change_90d) {
        this.percent_change_90d = percent_change_90d;
    }

    public String getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(String market_cap) {
        this.market_cap = market_cap;
    }

    public String getMarket_cap_dominance() {
        return market_cap_dominance;
    }

    public void setMarket_cap_dominance(String market_cap_dominance) {
        this.market_cap_dominance = market_cap_dominance;
    }

    public String getFully_diluted_market_cap() {
        return fully_diluted_market_cap;
    }

    public void setFully_diluted_market_cap(String fully_diluted_market_cap) {
        this.fully_diluted_market_cap = fully_diluted_market_cap;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }
}