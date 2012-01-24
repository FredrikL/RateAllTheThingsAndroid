package com.tacticalnuclearstrike.rateallthethings.api;

public interface ISettings {
    void setEmail(String email);
    String getEmail();

    void setPassword(String password);
    String getPassword();
    
    Boolean hasEmailAndPassword();
    
    String getTag();
}

