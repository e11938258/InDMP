package at.tuwien.indmp.util;

public class Endpoints {

    private Endpoints() {
        throw new AssertionError();
    }

    // maDMP
    public static final String UPDATE_MADMP = "/madmp"; // PUT
    public static final String GET_MADMP = "/madmp"; // GET
    public static final String UPDATE_MADMP_IDENTIFIER = "/madmp/identifier"; // PUT
    public static final String GET_MADMP_IDENTIFIERS = "/madmp/identifier/history"; // GET
    public static final String DELETE_MADMP_INSTANCE = "/madmp/instance/delete"; // PUT

    // Data service
    public static final String CREATE_DATA_SERVICE = "/service"; // POST
}
