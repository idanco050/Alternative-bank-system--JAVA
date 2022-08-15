package util;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static int REFRESH_RATE = 2500;

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/chatapp";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String XML_LOAD = FULL_SERVER_PATH + "/xmlservlet";
    public final static String ADD_MONEY = FULL_SERVER_PATH + "/addmoney";
    public final static String DRAW_MONEY = FULL_SERVER_PATH + "/drawmoney";
    public final static String GET_BALANCE = FULL_SERVER_PATH + "/getbalance";
    public final static String CUSTOMER_INFO = FULL_SERVER_PATH + "/customerinfo";
    public final static String PING = FULL_SERVER_PATH + "/ping";
    public final static String LOAN_ENTRY = FULL_SERVER_PATH + "/loanentry";
    public final static String SCRAMBLE = FULL_SERVER_PATH + "/scramble";
    public final static String ASSIGN_LOANS = FULL_SERVER_PATH + "/assignloans";
    public final static String LOAN_PAYMENT = FULL_SERVER_PATH + "/loanpayment";
    public final static String CLOSE_LOAN = FULL_SERVER_PATH + "/closeloan";
    public final static String SELL_LOAN = FULL_SERVER_PATH + "/sellloan";
    public final static String BUY_LOAN = FULL_SERVER_PATH + "/buyloan";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
