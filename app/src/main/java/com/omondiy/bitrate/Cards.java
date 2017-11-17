package com.omondiy.bitrate;

public class Cards {
    private String currency;
    private String coin;
    private String text3;
    private String img1;

    Cards(){

    }

    public void setCurrency(String currency){ this.currency = currency; }
    public void setCoin(String coin){ this.coin = coin; }
    public void setText3(String text3){ this.text3 = text3; }
    public void setImg1(String img1){ this.img1 = img1; }


    public String getCurrency(){ return currency; }
    public String getCoin(){ return coin; }
    public String getText3(){ return text3; }
    public String getImg1(){ return img1; }

}