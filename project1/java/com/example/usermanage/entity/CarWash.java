package com.example.usermanage.entity;

import lombok.Data;

@Data
public class CarWash {
    private int cw_id;
    private int owner;
    private String cw_name;
    private String cw_image;
    private String openhour;
    private String location;
    private String description;
    private float score;
    private String cw_phone;
}
