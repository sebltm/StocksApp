package com.example.Team8.utils;

import java.math.BigDecimal;
import java.util.Date;

public class AnalysisPoint extends DataPoint {

    private AnalysisType analysisType;

    public AnalysisPoint(AnalysisType analysisType, BigDecimal value, Date dateTime) {
        super(value, dateTime);
        this.pointType = "analysis_point";
        this.analysisType = analysisType;
    }

    public AnalysisType getAnalysisType() {
        return analysisType;
    }

    @Override
    public String toString() {
        return String.format("%2$s | TYPE: %1$s", analysisType, super.toString());
    }
}
