package com.pruebaandroid.topapps.DataObjects;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Andres on 05/09/2016.
 */
@DatabaseTable
public class Entry {

    @DatabaseField(generatedId = true)
    private int IdEntry = 0;
    @DatabaseField
    private String summary;
    @DatabaseField
    private String urlimagen;
    @DatabaseField
    private String name;
    @DatabaseField
    private String artist;
    @DatabaseField
    private String category;
    @DatabaseField
    private String releaseDate;
    @DatabaseField
    private String priceamount;
    @DatabaseField
    private String pricecurrency;


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public void setUrlimagen(String urlimagen) {
        this.urlimagen = urlimagen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPriceamount() {
        return priceamount;
    }

    public void setPriceamount(String priceamount) {
        this.priceamount = priceamount;
    }

    public String getPricecurrency() {
        return pricecurrency;
    }

    public void setPricecurrency(String pricecurrency) {
        this.pricecurrency = pricecurrency;
    }

    public int getIdEntry() {
        return IdEntry;
    }

    public void setIdEntry(int idEntry) {
        IdEntry = idEntry;
    }
}
