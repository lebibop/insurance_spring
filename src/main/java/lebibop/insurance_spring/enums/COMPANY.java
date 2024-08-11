package lebibop.insurance_spring.enums;

import lombok.Getter;

@Getter
public enum COMPANY {
    RENINS("РЕНЕССАНС", "#d0b3e1"),
    INGOS("ИНГОССТРАХ", "#aedcf8"),
    SBER("СБЕР", "#b9e3ae"),
    SOVCOM("СОВКОМ", "#cbab8d"),
    ALFA("АЛЬФА", "#ff9797"),
    RESO("РЕСО", "#6699aa"),
    RGS("РОСГОССТРАХ", "#ffdddd"),
    VSK("ВСК", "#b5bef5"),
    ZETTA("ЗЕТТА", "#c3e5e5"),
    GELLIOS("ГЕЛЛИОС", "#efe9f4"),
    UGORIA("ЮГОРИЯ", "#daddd8");
// #f3de8a  #cae7b9


    private final String name;
    private final String color;

    COMPANY(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
