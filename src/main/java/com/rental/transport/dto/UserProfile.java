package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CaseFormat;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 2L;

    private Long id;
    @JsonProperty("original_user_id")
    private Long originalUserId;
    private String type;
    private String originalType;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("agree_pers_data")
    private Boolean agreePersData;
    @JsonProperty("agreement_accepted")
    private Boolean agreementAccepted;
    @JsonProperty("school_id")
    private Long schoolId;
    @JsonProperty("school_shortname")
    private String schoolShortName;
    private List<String> roles;
    @JsonProperty("subject_ids")
    private List<Long> subjectIds;

    public void setType(String type) {
        originalType = type;
        int n = originalType.indexOf("Profile");
        this.type = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, n > -1 ? type.substring(0, n) : type);
    }
}
