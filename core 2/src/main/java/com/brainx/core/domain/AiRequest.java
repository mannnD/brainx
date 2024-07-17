package com.brainx.core.domain;

import com.brainx.core.entity.User;
import lombok.Data;

@Data
public class AiRequest {
    private String userId;
    private String type;
    private String inputData;
}
