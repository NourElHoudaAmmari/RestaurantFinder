package com.example.restaurantapp.Model;

public class Results {
    private String[] types;

    private String business_status;

    private String icon;

    private String rating;

    private String icon_background_color;

    private Photos[] photos;

    private String reference;

    private String user_ratings_total;

    private String scope;

    private String name;

    private Geometry geometry;

    private String icon_mask_base_uri;

    private String vicinity;

    private String place_id;

    public String[] getTypes ()
    {
        return types;
    }

    public void setTypes (String[] types)
    {
        this.types = types;
    }

    public String getBusiness_status ()
    {
        return business_status;
    }

    public void setBusiness_status (String business_status)
    {
        this.business_status = business_status;
    }

    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getIcon_background_color ()
    {
        return icon_background_color;
    }

    public void setIcon_background_color (String icon_background_color)
    {
        this.icon_background_color = icon_background_color;
    }

    public Photos[] getPhotos ()
    {
        return photos;
    }

    public void setPhotos (Photos[] photos)
    {
        this.photos = photos;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    public String getUser_ratings_total ()
    {
        return user_ratings_total;
    }

    public void setUser_ratings_total (String user_ratings_total)
    {
        this.user_ratings_total = user_ratings_total;
    }

    public String getScope ()
    {
        return scope;
    }

    public void setScope (String scope)
    {
        this.scope = scope;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Geometry getGeometry ()
    {
        return geometry;
    }

    public void setGeometry (Geometry geometry)
    {
        this.geometry = geometry;
    }

    public String getIcon_mask_base_uri ()
    {
        return icon_mask_base_uri;
    }

    public void setIcon_mask_base_uri (String icon_mask_base_uri)
    {
        this.icon_mask_base_uri = icon_mask_base_uri;
    }

    public String getVicinity ()
    {
        return vicinity;
    }

    public void setVicinity (String vicinity)
    {
        this.vicinity = vicinity;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [types = "+types+", business_status = "+business_status+", icon = "+icon+", rating = "+rating+", icon_background_color = "+icon_background_color+", photos = "+photos+", reference = "+reference+", user_ratings_total = "+user_ratings_total+", scope = "+scope+", name = "+name+", geometry = "+geometry+", icon_mask_base_uri = "+icon_mask_base_uri+", vicinity = "+vicinity+", place_id = "+place_id+"]";
    }
}
