package lebibop.insurance_spring.enums;

import lombok.Getter;

@Getter
public enum Company {
    COMPANY_A("РЕНЕССАНС"),
    COMPANY_B("ИНГОССТРАХ"),
    COMPANY_C("РОСГОССТРАХ"),
    COMPANY_D("ЗЕТТА"),
    COMPANY_E("АЛЬФА"),
    COMPANY_F("ГЕЛИОС"),
    COMPANY_G("ВСК"),
    COMPANY_H("РЕСО");


    private final String displayName;

    Company(String displayName) {
        this.displayName = displayName;
    }

}
