package at.tuwien.repository.model.dmp;

import java.net.URI;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.repository.util.DMPConstants;

public class License {

    /* Properties */
    @NotNull
    private URI license_ref;

    @NotNull
    @JsonFormat(pattern = DMPConstants.DATE_FORMAT_ISO_8601)
    private Date start_date;

    public License() {
    }

    public URI getLicense_ref() {
        return this.license_ref;
    }

    public void setLicense_ref(URI license_ref) {
        this.license_ref = license_ref;
    }

    public Date getStart_date() {
        return this.start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
}
