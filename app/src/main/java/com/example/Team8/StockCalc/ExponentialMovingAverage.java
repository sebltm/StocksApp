package com.example.Team8.StockCalc;

import androidx.annotation.NonNull;

import java.util.Locale;

public class ExponentialMovingAverage {

    private double[] prices;
    private int period;

    private double[] periodSma;
    private double smoothingConstant;
    private double[] periodEma;

    public ExponentialMovingAverage calculate(double[] prices, int period) throws Exception {

        if (period >= prices.length)
            throw new Exception("Given period is bigger then given set of prices");

        this.prices = prices;
        this.period = period;

        this.smoothingConstant = 2d / (this.period + 1);

        this.periodSma = new double[this.prices.length];
        this.periodEma = new double[this.prices.length];

        SimpleMovingAverage sma = new SimpleMovingAverage();

        this.periodEma = sma.calculate(prices, this.period).getSMA();

        for (int i = (period - 1); i < this.prices.length; i++) {

            if (i == (period - 1)) {
                this.periodEma[i] = this.periodSma[i];
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