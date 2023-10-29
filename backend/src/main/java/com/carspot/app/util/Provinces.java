package com.carspot.app.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Provinces {

    public static List<String> PROVINCES = Arrays.asList("Western Cape", "Mpumulanga", "Gauteng", "Kwazulu-Natal", "Free State", "North West", "Eastern Cape",
            "Northern Cape", "Limpopo").stream().map(province -> province.toLowerCase(Locale.ROOT)).collect(Collectors.toList());
}
