package at.tuwien.indmp.util;

public class ModelConstants {

    private ModelConstants() {
        throw new AssertionError();
    }

    private static final String REGEX_STRING = "^$|^[\\p{IsAlphabetic}\\p{Punct}\\p{Digit}\\p{Blank}\\n]+$";

    public static final int RDM_SERVICE_NAME_MIN = 1;
    public static final int RDM_SERVICE_NAME_MAX = 64;
    public static final String RDM_SERVICE_NAME_REGEX = REGEX_STRING;

    public static final int PROPERTY_DMP_IDENTIFIER_MIN = 1;
    public static final int PROPERTY_DMP_IDENTIFIER_MAX = 256;
    public static final String PROPERTY_DMP_IDENTIFIER_REGEX = REGEX_STRING;

    public static final int PROPERTY_CLASS_IDENTIFIER_MIN = 1;
    public static final int PROPERTY_CLASS_IDENTIFIER_MAX = 256;
    public static final String PROPERTY_CLASS_IDENTIFIER_REGEX = REGEX_STRING;

    public static final int PROPERTY_NAME_MIN = 1;
    public static final int PROPERTY_NAME_MAX = 64;
    public static final String PROPERTY_NAME_REGEX = "^[\\p{Alpha}_]+$";

    public static final int PROPERTY_VALUE_MIN = 0;
    public static final int PROPERTY_VALUE_MAX = 4096;
    public static final String PROPERTY_VALUE_REGEX = REGEX_STRING;

}
