package com.example.webbyskymarket.enams;

public enum ProductCategory {
    ANIMALS("Animals"),
    REAL_ESTATE("Real Estate"),
    TRANSPORT("Transport"),
    ELECTRONICS("Electronics"),
    FURNITURE("Furniture"),
    CLOTHING("Clothing"),
    SPORTS("Sports"),
    BEAUTY_AND_HEALTH("Beauty and Health"),
    FOOD("Food"),
    BOOKS("Books"),
    TOOLS("Tools");

    public final String category;

    ProductCategory(String category) {
        this.category = category;
    }
}
