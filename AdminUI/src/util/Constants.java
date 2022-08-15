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
    public final static String ADMIN_LOGIN = FULL_SERVER_PATH + "/adminlogin";
    public final static String ADMIN_INFO = FULL_SERVER_PATH + "/admininfo";
    public final static String YAZ_UP = FULL_SERVER_PATH + "/yazup";




    public final static String GET_BALANCE = FULL_SERVER_PATH + "/getbalance";
    public final static String CUSTOMER_INFO = FULL_SERVER_PATH + "/customerinfo";
    public final static String PING = FULL_SERVER_PATH + "/ping";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
