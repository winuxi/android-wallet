package org.telegram.crypto.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "currency")
public class Currency {
    @PrimaryKey
    int id;
    int rank;
    String icon;
    String name;
    String symbol;
    String slug;
    String price;
    String today;
    String week;
    String marketCap;
    String volume;
    String circulating_supply;

    public Currency(int rank, int id, String icon, String name, String symbol,
                    String slug, String price, String today, String week,
                    String marketCap, String volume, String circulating_supply) {
        this.rank = rank;
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.symbol = symbol;
        this.slug = slug;
        this.price = price;
        this.today = today;
        this.week = week;
        this.marketCap = marketCap;
        this.volume = volume;
        this.circulating_supply = circulating_supply;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCirculating_supply() {
        return circulating_supply;
    }

    public void setCirculating_supply(String circulating_supply) {
        this.circulating_supply = circulating_supply;
    }
}
