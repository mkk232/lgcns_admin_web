package com.lgcns.admin.common.vo;

import lombok.Data;

import java.util.Map;

@Data
public class SessionVO {
    private int userId;
    private String userLoginId;
    private String userName;
    private String email;
    private String expired;
    private String used;

    public SessionVO(Map<String, Object> loginInfoMap) {
        this.userId = Long.valueOf((Long) loginInfoMap.get("userId")).intValue();
        this.userLoginId = (String) loginInfoMap.get("userLoginId");
        this.userName = (String) loginInfoMap.get("userName");
        this.email = (String) loginInfoMap.get("email");
        this.used = (String) loginInfoMap.get("used");
        this.expired = (String) loginInfoMap.get("expired");
    }
}
