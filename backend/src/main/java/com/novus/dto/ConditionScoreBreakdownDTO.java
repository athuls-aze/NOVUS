package com.novus.dto;

public class ConditionScoreBreakdownDTO {
    private String component;
    private double weight;
    private int rawScore;
    private double weightedScore;

    public ConditionScoreBreakdownDTO(String component, double weight, int rawScore, double weightedScore) {
        this.component = component;
        this.weight = weight;
        this.rawScore = rawScore;
        this.weightedScore = weightedScore;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getRawScore() {
        return rawScore;
    }

    public void setRawScore(int rawScore) {
        this.rawScore = rawScore;
    }

    public double getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(double weightedScore) {
        this.weightedScore = weightedScore;
    }
}
