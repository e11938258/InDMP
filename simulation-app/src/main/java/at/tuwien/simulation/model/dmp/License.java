package at.tuwien.simulation.model.dmp;

import java.net.URI;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.simulation.util.ModelConstants;

public class License extends AbstractClassEntity {

    /* Properties */
    @NotNull
    private URI license_ref;

    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private LocalDate start_date;

    public License() {
    }

    public License(URI license_ref, LocalDate start_date) {
        this.license_ref = license_ref;
        this.start_date = start_date;
    }

    public URI getLicense_ref() {
        return this.license_ref;
    }

    public void setLicense_ref(URI license_ref) {
        this.license_ref = license_ref;
    }

    public LocalDate getStart_date() {
        return this.start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getStart_date() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getStart_date()) : null,
                getLicense_ref().toString()
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "start_date",
                "license_ref"
        };
    }

    @Override
    public String getClassIdentifier() {
        return getLicense_ref().toString();
    }
}
