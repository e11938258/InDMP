package at.tuwien.dmp.util;

public class Endpoints {

    private Endpoints() {
        throw new AssertionError();
    }

    // Common
    public static final String INIT = "/init";
    public static final String MADMP = "/madmp"; // PUT

    // Test cases
    public static final String TEST_CASE_1 = "/ftc1";
    public static final String TEST_CASE_2 = "/ftc2";
    public static final String TEST_CASE_3 = "/ftc3";
    public static final String TEST_CASE_4 = "/ftc4";
    public static final String TEST_CASE_5 = "/ftc5";
    public static final String TEST_CASE_7 = "/ftc7";

    public static final String TEST_CASE_8 = "/nftc8";
    public static final String TEST_CASE_9 = "/nftc9";
}
