/*
The MIT License (MIT)

Copyright (c) 2016 Jason Lam

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */
package com.example.Team8.StockCalc;

import java.util.Arrays;

public class SimpleMovingAverage {

    private double[] results;

    public SimpleMovingAverage calculate(double[] price, int period) throws Exception {
        // ie: if you want 50 SMA then you need 50 data points
        if (price.length < period)
            throw new Exception("Not enough data points, given data size less than the indicated period");

        this.results = new double[price.length];

        int maxLength = price.length - period;

        for (int i = 0; i <= maxLength; i++) {
            this.results[(i + period - 1)] = NumberFormatter
                    .round((Arrays.stream(Arrays.copyOfRange(price, i, (i + period))).sum()) / period);
        }

        return this;
    }

    public double[] getSMA() {
        return this.results;
    }

}