package at.tuwien.indmp.util;

public class Endpoints {

    private Endpoints() {
        throw new AssertionError();
    }

    // maDMP
    public static final String UPDATE_MADMP = "/madmp"; // PUT
    public static final String UPDATE_MADMP_IDENTIFIER = "/madmp/identifier"; // PUT
    public static final String GET_MADMP_IDENTIFIERS = "/madmp/identifier/history"; // GET
    public static final String DELETE_MADMP_INSTANCE = "/madmp/instance/delete"; // PUT

    public static final String GET_MADMP = "/madmp"; // GET - just for test cases

    // Data service
    public static final String CREATE_DATA_SERVICE = "/service"; // POST
    public static final String EXISTS_DATA_SERVICE = "/service/exists"; // GET
}
