package at.tuwien.indmp.util;

public class Constants {

    private Constants() {
        throw new AssertionError();
    }

    // ------------------------------------
    // Basic
    // ------------------------------------
    public static final String SERVER_LOCALIZATION = "Europe/Berlin";
    public static final String SERVER_TIMEZONE = "UTC";
    public static final String FILES_ENCODING = "utf-8";

    // ------------------------------------
    // Endpoints
    // ------------------------------------
    // maDMP
    public static final String UPDATE_MADMP = "/madmp"; // PUT
    public static final String IDENTIFIER_CHANGE = "/madmp/instance/id"; // PUT
    public static final String DELETE_INSTANCE = "/madmp/instance"; // DELETE
    public static final String GET_MADMP = "/madmp"; // GET
    public static final String GET_MADMP_IDENTIFIERS = "/madmp/identifiers"; // GET

    // System
    public static final String CREATE_SYSTEM = "/system"; // POST
    public static final String GET_ALL_SYSTEMS = "/systems"; // GET

    // ------------------------------------
    // Defaults regex
    // ------------------------------------
    private static final String REGEX_STRING = "^$|^[\\p{IsAlphabetic}\\p{Punct}\\p{Digit}\\p{Blank}\\n]+$";
    private static final String REGEX_ALPHA = "^[\\p{Alpha}]+$";

    // ------------------------------------
    // System
    // ------------------------------------
    public static final int SYSTEM_NAME_MIN = 1;
    public static final int SYSTEM_NAME_MAX = 64;
    public static final String SYSTEM_NAME_REGEX = REGEX_STRING;

    public static final int SYSTEM_API_MIN = 1;
    public static final int SYSTEM_API_MAX = 64;
    public static final String SYSTEM_API_REGEX = REGEX_STRING;

    public static final int SYSTEM_HOST_MIN = 1;
    public static final int SYSTEM_HOST_MAX = 64;
    public static final String SYSTEM_HOST_REGEX = REGEX_STRING;

    public static final int SYSTEM_ENDPOINT_MIN = 1;
    public static final int SYSTEM_ENDPOINT_MAX = 128;
    public static final String SYSTEM_ENDPOINT_REGEX = REGEX_STRING;

    // ------------------------------------
    // Properties
    // ------------------------------------
    public static final int PROPERTY_DMP_IDENTIFIER_MIN = 1;
    public static final int PROPERTY_DMP_IDENTIFIER_MAX = 256;
    public static final String PROPERTY_DMP_IDENTIFIER_REGEX = REGEX_STRING;

    public static final int PROPERTY_CLASS_NAME_MIN = 1;
    public static final int PROPERTY_CLASS_NAME_MAX = 64;
    public static final String PROPERTY_CLASS_NAME_REGEX = "^[\\p{Alpha}_]+$";

    public static final int PROPERTY_CLASS_IDENTIFIER_MIN = 1;
    public static final int PROPERTY_CLASS_IDENTIFIER_MAX = 256;
    public static final String PROPERTY_CLASS_IDENTIFIER_REGEX = REGEX_STRING;

    public static final int PROPERTY_NAME_MIN = 1;
    public static final int PROPERTY_NAME_MAX = 64;
    public static final String PROPERTY_NAME_REGEX = "^[\\p{Alpha}_]+$";

    public static final int PROPERTY_VALUE_MIN = 0;
    public static final int PROPERTY_VALUE_MAX = 4096;
    public static final String PROPERTY_VALUE_REGEX = REGEX_STRING;

    // ------------------------------------
    // Types
    // ------------------------------------
    public static final int TYPES_MIN = 1;
    public static final int TYPES_MAX = 256;
    public static final String TYPES_REGEX = REGEX_ALPHA;

}
