package com.carspot.app.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostFilterParameters {

    private String province;
    private List<String> city;
    private List<String> brand;
    private int minValue;
    private int maxValue;

}
