package com.tacticalnuclearstrike.rateallthethings.api;

import com.tacticalnuclearstrike.rateallthethings.model.BarCode;

/**
 * User: Fredrik / 2012-01-08
 */
public interface IService {
    BarCode lookUpBarCode(String format, String code);
    String createUser(String email);
    Boolean updateBarCode(BarCode barCode);
}

