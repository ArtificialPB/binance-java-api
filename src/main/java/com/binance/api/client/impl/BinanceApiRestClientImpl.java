package com.binance.api.client.impl;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.constant.BinanceApiConstants;
import com.binance.api.client.domain.account.*;
import com.binance.api.client.domain.account.request.*;
import com.binance.api.client.domain.event.ListenKey;
import com.binance.api.client.domain.general.Asset;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.ServerTime;
import com.binance.api.client.domain.market.*;
import retrofit2.Call;

import java.net.ProxySelector;
import java.util.List;
import java.util.function.Function;

import static com.binance.api.client.impl.BinanceApiServiceGenerator.createService;

/**
 * Implementation of Binance's REST API using Retrofit with synchronous/blocking method calls.
 */
public class BinanceApiRestClientImpl implements BinanceApiRestClient {
    private final BinanceApiService binanceApiService;
    private final Function<Call<?>, ?> executor;

    public BinanceApiRestClientImpl(String apiKey, String secret) {
        this(apiKey, secret, null);
    }

    public BinanceApiRestClientImpl(String apiKey, String secret, ProxySelector proxySelector) {
        binanceApiService = createService(BinanceApiService.class, apiKey, secret, proxySelector);
        this.executor = BinanceApiServiceGenerator::executeSync;
    }

    public BinanceApiRestClientImpl(final BinanceApiService service, final Function<Call<?>, ?> executor) {
        this.binanceApiService = service;
        this.executor = executor;
    }

    private <R> R executeFunction(final Call<R> call) {
        return (R) executor.apply(call);
    }

    // General endpoints

    @Override
    public void ping() {
        executeFunction(binanceApiService.ping());
    }

    @Override
    public Long getServerTime() {
        final ServerTime result = executeFunction(binanceApiService.getServerTime());
        return result == null ? null : result.getServerTime();
    }

    @Override
    public ExchangeInfo getExchangeInfo() {
        return executeFunction(binanceApiService.getExchangeInfo());
    }

    @Override
    public List<Asset> getAllAssets() {
        return executeFunction(binanceApiService.getAllAssets(BinanceApiConstants.ASSET_INFO_API_BASE_URL + "assetWithdraw/getAllAsset.html"));
    }

    // Market Data endpoints

    @Override
    public OrderBook getOrderBook(String symbol, Integer limit) {
        return executeFunction(binanceApiService.getOrderBook(symbol, limit));
    }

    @Override
    public List<TradeHistoryItem> getTrades(String symbol, Integer limit) {
        return executeFunction(binanceApiService.getTrades(symbol, limit));
    }

    @Override
    public List<TradeHistoryItem> getHistoricalTrades(String symbol, Integer limit, Long fromId) {
        return executeFunction(binanceApiService.getHistoricalTrades(symbol, limit, fromId));
    }

    @Override
    public List<AggTrade> getAggTrades(String symbol, String fromId, Integer limit, Long startTime, Long endTime) {
        return executeFunction(binanceApiService.getAggTrades(symbol, fromId, limit, startTime, endTime));
    }

    @Override
    public List<AggTrade> getAggTrades(String symbol) {
        return getAggTrades(symbol, null, null, null, null);
    }

    @Override
    public List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval interval, Integer limit, Long startTime, Long endTime) {
        return executeFunction(binanceApiService.getCandlestickBars(symbol, interval.getIntervalId(), limit, startTime, endTime));
    }

    @Override
    public List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval interval) {
        return getCandlestickBars(symbol, interval, null, null, null);
    }

    @Override
    public TickerStatistics get24HrPriceStatistics(String symbol) {
        return executeFunction(binanceApiService.get24HrPriceStatistics(symbol));
    }

    @Override
    public List<TickerStatistics> getAll24HrPriceStatistics() {
        return executeFunction(binanceApiService.getAll24HrPriceStatistics());
    }

    @Override
    public TickerPrice getPrice(String symbol) {
        return executeFunction(binanceApiService.getLatestPrice(symbol));
    }

    @Override
    public List<TickerPrice> getAllPrices() {
        return executeFunction(binanceApiService.getLatestPrices());
    }

    @Override
    public List<BookTicker> getBookTickers() {
        return executeFunction(binanceApiService.getBookTickers());
    }

    @Override
    public NewOrderResponse newOrder(NewOrder order) {
        return executeFunction(binanceApiService.newOrder(order.getSymbol(), order.getSide(), order.getType(),
                order.getTimeInForce(), order.getQuantity(), order.getQuoteOrderQty(), order.getPrice(), order.getNewClientOrderId(), order.getStopPrice(),
                order.getIcebergQty(), order.getNewOrderRespType(), order.getRecvWindow(), order.getTimestamp()));
    }

    @Override
    public void newOrderTest(NewOrder order) {
        executeFunction(binanceApiService.newOrderTest(order.getSymbol(), order.getSide(), order.getType(),
                order.getTimeInForce(), order.getQuantity(), order.getQuoteOrderQty(), order.getPrice(), order.getNewClientOrderId(), order.getStopPrice(),
                order.getIcebergQty(), order.getNewOrderRespType(), order.getRecvWindow(), order.getTimestamp()));
    }

    // Account endpoints

    @Override
    public Order getOrderStatus(OrderStatusRequest orderStatusRequest) {
        return executeFunction(binanceApiService.getOrderStatus(orderStatusRequest.getSymbol(),
                orderStatusRequest.getOrderId(), orderStatusRequest.getOrigClientOrderId(),
                orderStatusRequest.getRecvWindow(), orderStatusRequest.getTimestamp()));
    }

    @Override
    public CancelOrderResponse cancelOrder(CancelOrderRequest cancelOrderRequest) {
        return executeFunction(binanceApiService.cancelOrder(cancelOrderRequest.getSymbol(),
                cancelOrderRequest.getOrderId(), cancelOrderRequest.getOrigClientOrderId(), cancelOrderRequest.getNewClientOrderId(),
                cancelOrderRequest.getRecvWindow(), cancelOrderRequest.getTimestamp()));
    }

    @Override
    public List<Order> getOpenOrders(OrderRequest orderRequest) {
        return executeFunction(binanceApiService.getOpenOrders(orderRequest.getSymbol(), orderRequest.getRecvWindow(), orderRequest.getTimestamp()));
    }

    @Override
    public List<Order> getAllOrders(AllOrdersRequest orderRequest) {
        return executeFunction(binanceApiService.getAllOrders(orderRequest.getSymbol(),
                orderRequest.getOrderId(), orderRequest.getLimit(),
                orderRequest.getRecvWindow(), orderRequest.getTimestamp()));
    }

    @Override
    public Account getAccount(Long recvWindow, Long timestamp) {
        return executeFunction(binanceApiService.getAccount(recvWindow, timestamp));
    }

    @Override
    public Account getAccount() {
        return getAccount(BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis());
    }

    @Override
    public List<Trade> getMyTrades(String symbol, Integer limit, Long fromId, Long recvWindow, Long timestamp) {
        return executeFunction(binanceApiService.getMyTrades(symbol, limit, fromId, recvWindow, timestamp));
    }

    @Override
    public List<Trade> getMyTrades(String symbol, Integer limit) {
        return getMyTrades(symbol, limit, null, BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis());
    }

    @Override
    public List<Trade> getMyTrades(String symbol) {
        return getMyTrades(symbol, null, null, BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis());
    }

    @Override
    public WithdrawResult withdraw(String asset, String address, String amount, String name, String addressTag) {
        return executeFunction(binanceApiService.withdraw(asset, address, amount, name, addressTag, BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis()));
    }

    @Override
    public DepositHistory getDepositHistory(String asset) {
        return executeFunction(binanceApiService.getDepositHistory(asset, BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis()));
    }

    @Override
    public WithdrawHistory getWithdrawHistory(String asset) {
        return executeFunction(binanceApiService.getWithdrawHistory(asset, BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis()));
    }

    @Override
    public DepositAddress getDepositAddress(String asset) {
        return executeFunction(binanceApiService.getDepositAddress(asset, BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis()));
    }

    // User stream endpoints

    @Override
    public String startUserDataStream() {
        final ListenKey result = executeFunction(binanceApiService.startUserDataStream());
        return result == null ? null : result.getListenKey();
    }

    @Override
    public void keepAliveUserDataStream(String listenKey) {
        executeFunction(binanceApiService.keepAliveUserDataStream(listenKey));
    }

    @Override
    public void closeUserDataStream(String listenKey) {
        executeFunction(binanceApiService.closeAliveUserDataStream(listenKey));
    }

    @Override
    public TradeFee getTradeFee() {
        return executeFunction(binanceApiService.getTradeFee(BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis()));
    }
}
