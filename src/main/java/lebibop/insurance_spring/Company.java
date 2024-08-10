package lebibop.insurance_spring;

import lombok.Getter;

@Getter
public enum Company {
    COMPANY_A("РЕСО"),
    COMPANY_B("Company B"),
    COMPANY_C("Company C");

    private final String displayName;

    Company(String displayName) {
        this.displayName = displayName;
    }

}
