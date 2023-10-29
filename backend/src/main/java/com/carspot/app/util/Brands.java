package com.carspot.app.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Brands {

    public static final List<String> CAR_BRANDS = Arrays.asList("Bmw", "Audi", "Toyota", "Mercedes Benz", "Honda", "Hyundai", "Ford",
            "Tata", "Nissan", "Fiat", "Suzuki", "Renault", "Jaguar", "Kia", "Volkswagen", "Isuzu", "Alfa Romeo", "Aston Martin", "Lexus",
            "Mitsubishi", "Chevrolet", "Opel", "Mazda" ).stream().map(city -> city.toLowerCase(Locale.ROOT)).collect(Collectors.toList());
}
