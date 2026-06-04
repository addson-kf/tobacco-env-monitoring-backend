package org.example.model;

import org.example.model.enums.SoilAttribute;
import org.example.model.enums.SoilQuality;

public class SoilClassificationRule
{
    private SoilAttribute soilAttribute;
    private double minValue;
    private double maxValue;
    private SoilQuality classification;

    // Getters and Setters
    public SoilAttribute getSoilAttribute() {
        return soilAttribute;
    }

    public void setSoilAttribute(SoilAttribute attribute) {
        this.soilAttribute = attribute;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public SoilQuality getClassification() {
        return classification;
    }

    public void setClassification(SoilQuality classification) {
        this.classification = classification;
    }

    // toString() method
    @Override
    public String toString() {
        return soilAttribute + " (" + minValue + " - " + maxValue + "): " + classification;
    }



}
