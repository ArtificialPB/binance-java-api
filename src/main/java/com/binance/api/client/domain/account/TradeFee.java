package com.binance.api.client.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeFee {
    private List<Symbol> tradeFee;

    public List<Symbol> getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(List<Symbol> tradeFee) {
        this.tradeFee = tradeFee;
    }

    public static class Symbol {
        private String symbol;
        private String maker;
        private String taker;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getMaker() {
            return maker;
        }

        public void setMaker(String maker) {
            this.maker = maker;
        }

        public String getTaker() {
            return taker;
        }

        public void setTaker(String taker) {
            this.taker = taker;
        }
    }
}
