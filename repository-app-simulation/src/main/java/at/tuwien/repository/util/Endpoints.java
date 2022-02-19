package at.tuwien.repository.util;

public class Endpoints {

    private Endpoints() {
        throw new AssertionError();
    }

    // Common
    public static final String INIT = "/init";
    public static final String MADMP = "/madmp"; // PUT

    // Test cases
    public static final String TEST_CASE_3 = "/ftc3";
    public static final String TEST_CASE_5 = "/ftc5";
    public static final String TEST_CASE_6 = "/ftc6";
}
