package com.example.restaurantapp.Model;

public class Result {
    private String utc_offset;

    private String formatted_address;

    private String wheelchair_accessible_entrance;

    private String icon;

    private String rating;

    private String icon_background_color;

    private Photos[] photos;

    private String reference;

    private Current_opening_hours current_opening_hours;

    private String user_ratings_total;

    private Reviews[] reviews;

    private String icon_mask_base_uri;

    private String adr_address;

    private String place_id;

    private String[] types;

    private String website;

    private String business_status;

    private Address_components[] address_components;

    private String url;

    private String name;

    private Opening_hours opening_hours;

    private Geometry geometry;

    private String vicinity;

    private Plus_code plus_code;

    private String formatted_phone_number;

    private String international_phone_number;

    public String getUtc_offset ()
    {
        return utc_offset;
    }

    public void setUtc_offset (String utc_offset)
    {
        this.utc_offset = utc_offset;
    }

    public String getFormatted_address ()
    {
        return formatted_address;
    }

    public void setFormatted_address (String formatted_address)
    {
        this.formatted_address = formatted_address;
    }

    public String getWheelchair_accessible_entrance ()
    {
        return wheelchair_accessible_entrance;
    }

    public void setWheelchair_accessible_entrance (String wheelchair_accessible_entrance)
    {
        this.wheelchair_accessible_entrance = wheelchair_accessible_entrance;
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

    public Current_opening_hours getCurrent_opening_hours ()
    {
        return current_opening_hours;
    }

    public void setCurrent_opening_hours (Current_opening_hours current_opening_hours)
    {
        this.current_opening_hours = current_opening_hours;
    }

    public String getUser_ratings_total ()
    {
        return user_ratings_total;
    }

    public void setUser_ratings_total (String user_ratings_total)
    {
        this.user_ratings_total = user_ratings_total;
    }

    public Reviews[] getReviews ()
    {
        return reviews;
    }

    public void setReviews (Reviews[] reviews)
    {
        this.reviews = reviews;
    }

    public String getIcon_mask_base_uri ()
    {
        return icon_mask_base_uri;
    }

    public void setIcon_mask_base_uri (String icon_mask_base_uri)
    {
        this.icon_mask_base_uri = icon_mask_base_uri;
    }

    public String getAdr_address ()
    {
        return adr_address;
    }

    public void setAdr_address (String adr_address)
    {
        this.adr_address = adr_address;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    public String[] getTypes ()
    {
        return types;
    }

    public void setTypes (String[] types)
    {
        this.types = types;
    }

    public String getWebsite ()
    {
        return website;
    }

    public void setWebsite (String website)
    {
        this.website = website;
    }

    public String getBusiness_status ()
    {
        return business_status;
    }

    public void setBusiness_status (String business_status)
    {
        this.business_status = business_status;
    }

    public Address_components[] getAddress_components ()
    {
        return address_components;
    }

    public void setAddress_components (Address_components[] address_components)
    {
        this.address_components = address_components;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Opening_hours getOpening_hours ()
    {
        return opening_hours;
    }

    public void setOpening_hours (Opening_hours opening_hours)
    {
        this.opening_hours = opening_hours;
    }

    public Geometry getGeometry ()
    {
        return geometry;
    }

    public void setGeometry (Geometry geometry)
    {
        this.geometry = geometry;
    }

    public String getVicinity ()
    {
        return vicinity;
    }

    public void setVicinity (String vicinity)
    {
        this.vicinity = vicinity;
    }

    public Plus_code getPlus_code ()
    {
        return plus_code;
    }

    public void setPlus_code (Plus_code plus_code)
    {
        this.plus_code = plus_code;
    }

    public String getFormatted_phone_number ()
    {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number (String formatted_phone_number)
    {
        this.formatted_phone_number = formatted_phone_number;
    }

    public String getInternational_phone_number ()
    {
        return international_phone_number;
    }

    public void setInternational_phone_number (String international_phone_number)
    {
        this.international_phone_number = international_phone_number;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [utc_offset = "+utc_offset+", formatted_address = "+formatted_address+", wheelchair_accessible_entrance = "+wheelchair_accessible_entrance+", icon = "+icon+", rating = "+rating+", icon_background_color = "+icon_background_color+", photos = "+photos+", reference = "+reference+", current_opening_hours = "+current_opening_hours+", user_ratings_total = "+user_ratings_total+", reviews = "+reviews+", icon_mask_base_uri = "+icon_mask_base_uri+", adr_address = "+adr_address+", place_id = "+place_id+", types = "+types+", website = "+website+", business_status = "+business_status+", address_components = "+address_components+", url = "+url+", name = "+name+", opening_hours = "+opening_hours+", geometry = "+geometry+", vicinity = "+vicinity+", plus_code = "+plus_code+", formatted_phone_number = "+formatted_phone_number+", international_phone_number = "+international_phone_number+"]";
    }
}
