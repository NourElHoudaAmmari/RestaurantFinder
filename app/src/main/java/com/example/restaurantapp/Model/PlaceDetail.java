package com.example.restaurantapp.Model;

import java.util.Arrays;

public class PlaceDetail {
    private Result result;
    private String[]html_attributions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    public Result getResult()
    {
        return result;
    }
    public void setResult(Result result)
    {
        this.result =result;
    }
    public String[] getHtml_attributions()
    {
        return html_attributions;
    }
    public  void setHtml_attributions(String[]html_attributions)
    {
        this.html_attributions = html_attributions;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", html_attributions = "+html_attributions+", status = "+status+"]";
    }

}
