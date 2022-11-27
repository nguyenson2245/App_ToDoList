package com.example.app_todolist;

public class CongViec {
    String ten_CV;
    int id_CV;

    public String getTen_CV() {
        return ten_CV;
    }

    public void setTen_CV(String ten_CV) {
        this.ten_CV = ten_CV;
    }

    public int getIdC_V() {
        return id_CV;
    }

    public void setIdC_V(int idC_V) {
        this.id_CV = idC_V;
    }

    public CongViec( int id_CV,String ten_CV) {
        this.ten_CV = ten_CV;
        this.id_CV = id_CV;
    }


}
