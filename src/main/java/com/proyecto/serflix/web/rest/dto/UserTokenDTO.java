package com.proyecto.serflix.web.rest.dto;

import com.google.gson.annotations.SerializedName;

public class UserTokenDTO {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("expires_in")
    private long expiresIn;

    @SerializedName("scope")
    private String scope;

    public UserTokenDTO() {
    }

    public UserTokenDTO(String accessToken, String tokenType, String grantType, String refreshToken, long expiresIn, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.grantType = grantType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "UserTokenDTO{" +
            "accessToken='" + accessToken + '\'' +
            ", tokenType='" + tokenType + '\'' +
            ", grantType='" + grantType + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", expiresIn=" + expiresIn +
            ", scope='" + scope + '\'' +
            '}';
    }
}
