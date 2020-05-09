package com.auk.fanyi.ui.home;

public class history {
    private String yuan;
    private String mudi;
    private int star,id,pos;
    public String getyuan() {
        return yuan;
    }
    public void setyuan(String yuan) {
        this.yuan = yuan;
    }
    public void setId(int id) {
        this.id = id;
    }
    public  int getId(){
        return id;
    }
    public void setStar(int star) {
        this.star = star;
    }
    public  int getStar(){
        return star;
    }
    public String getmudi() {
        return mudi;
    }
    public void setmudi(String mudi) {
        this.mudi = mudi;
    }
    @Override
    public String toString() {
        return yuan + ", " + mudi;
    }
    public history(String yuan, String mudi,int star,int id) {
        super();
        this.yuan = yuan;
        this.mudi = mudi;
        this.star=star;
        this.id = id;
    }
    public history(String yuan, String mudi,int star) {
        super();
        this.yuan = yuan;
        this.mudi = mudi;
        this.star=star;
        this.id=1111111;
    }

}
