// SecuritySchemeTypeEnum.java
package com.handcrafts.crafts.config;

import io.swagger.v3.oas.models.security.SecurityScheme;

public enum SecuritySchemeTypeEnum {
    HTTP(SecurityScheme.Type.HTTP);

    private final SecurityScheme.Type swaggerType;

    SecuritySchemeTypeEnum(SecurityScheme.Type swaggerType) {
        this.swaggerType = swaggerType;
    }

    public SecurityScheme.Type getSwaggerType() {
        return swaggerType;
    }
}
