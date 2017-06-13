package com.faza.project.fuzzylogicexample;

import android.app.Application;

import java.util.ArrayList;

/**
 * Dibuat oleh Faza Zulfika Permana Putra
 */

public class FuzzyLogicExample extends Application {

    public static ArrayList<FuzzySet> demandSets;
    public static ArrayList<FuzzySet> stockSets;
    public static ArrayList<FuzzySet> productSets;

    @Override
    public void onCreate() {
        super.onCreate();

        setDemandSets();
        setStockSets();
    }

    private void setDemandSets() {
        FuzzySet fuzzySet;

        demandSets = new ArrayList<>();

        fuzzySet = new FuzzySet(1000, 0, 1, "Demand Decrease");
        demandSets.add(fuzzySet);

        fuzzySet = new FuzzySet(5000, 1000, 3, "Demand Decrease");
        demandSets.add(fuzzySet);

        fuzzySet = new FuzzySet(5000, 1000, 2, "Demand Increase");
        demandSets.add(fuzzySet);

        fuzzySet = new FuzzySet(-1, 5000, 1, "Demand Increase");
        demandSets.add(fuzzySet);
    }

    private void setStockSets() {
        FuzzySet fuzzySet;

        stockSets = new ArrayList<>();

        fuzzySet = new FuzzySet(100, 0, 1, "Stock Low");
        stockSets.add(fuzzySet);

        fuzzySet = new FuzzySet(600, 100, 3, "Stock Low");
        stockSets.add(fuzzySet);

        fuzzySet = new FuzzySet(600, 100, 2, "Stock High");
        stockSets.add(fuzzySet);

        fuzzySet = new FuzzySet(-1, 600, 1, "Stock High");
        stockSets.add(fuzzySet);
    }
}
