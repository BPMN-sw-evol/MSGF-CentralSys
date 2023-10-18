package com.MSGFCentralSys.MSGFCentralSys.DTO;

import lombok.Data;

@Data
public class ActivityCompleteDTO {
    private String processId;
    private String assignee;
    private String variables;
    private Boolean value;
}
