package at.tuwien.indmp.util;

public class Endpoints {

    private Endpoints() {
        throw new AssertionError();
    }

    // maDMP REST API
    public static final String UPDATE_MADMP = "/madmp"; // PUT
    public static final String GET_MADMP = "/madmp"; // GET
    public static final String UPDATE_MADMP_IDENTIFIER = "/madmp/identifier"; // PUT
    public static final String GET_MADMP_IDENTIFIERS = "/madmp/identifier/history"; // GET
    public static final String DELETE_MADMP_INSTANCE = "/madmp/instance/delete"; // PUT

    // RDM service REST API
    public static final String CREATE_RDM_SERVICE = "/service"; // POST
    public static final String READ_RDM_SERVICES = "/services"; // GET
    public static final String SET_PROPERTY_RIGHTS_TO_RDM_SERVICE = "/service/rights/property/{accessRights}"; // PUT
}
