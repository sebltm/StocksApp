/*
The MIT License (MIT)

Copyright (c) 2016 Jason Lam

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */
package com.example.Team8.StockCalc;

import androidx.annotation.NonNull;

import java.util.Locale;

public class ExponentialMovingAverage {

    private double[] prices;

    private double[] periodSma;
    private double smoothingConstant;
    private double[] periodEma;

    public ExponentialMovingAverage calculate(double[] prices, int period) throws Exception {

        if (period >= prices.length)
            throw new Exception("Given period is bigger than the given set of prices");

        this.prices = prices;

        this.smoothingConstant = 2d / (period + 1);

        this.periodEma = new double[this.prices.length];

        SimpleMovingAverage sma = new SimpleMovingAverage();

        this.periodSma = sma.calculate(prices, period).getSMA();

        for (int i = (period - 1); i < this.prices.length; i++) {

            if (i == (period - 1)) {
                this.periodEma[i] = this.periodSma[i] + (this.prices[i] - this.periodSma[i]) * this.smoothingConstant;
            } else if (i > (period - 1)) {
                // Formula: (Close - EMA(previous day)) x multiplier +
                // EMA(previous day)
                this.periodEma[i] = (this.prices[i] - periodEma[i - 1]) * this.smoothingConstant
                        + this.periodEma[i - 1];
            }

            this.periodEma[i] = NumberFormatter.round(this.periodEma[i]);
        }

        return this;
    }

    public double[] getEMA() {
        return this.periodEma;
    }

    @NonNull
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.prices.length; i++) {
            sb.append(String.format(Locale.US, "%02.2f", this.prices[i]));
            sb.append(" ");
            sb.append(String.format(Locale.US, "%02.2f", this.periodSma[i]));
            sb.append(" ");
            sb.append(String.format(Locale.US, "%02.2f", this.smoothingConstant));
            sb.append(" ");
            sb.append(String.format(Locale.US, "%02.2f", this.periodEma[i]));
            sb.append("\n");
        }

        return sb.toString();
    }
}