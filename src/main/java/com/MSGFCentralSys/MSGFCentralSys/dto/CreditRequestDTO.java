package com.MSGFCentralSys.MSGFCentralSys.dto;



import com.msgfoundation.annotations.BPMNGetVariables;
import lombok.Data;

@Data
@BPMNGetVariables(variables = {"coupleName1", "coupleName2", "marriageYears", "bothEmployees", "housePrices", "quotaValue", "coupleSavings", "requestDate"})
public class CreditRequestDTO {
    private String coupleName1;
    private String coupleName2;
    private String coupleEmail1;
    private String coupleEmail2;
    private String processId;
    private Long marriageYears;
    private Boolean bothEmployees;
    private Long housePrices;
    private Long quotaValue;
    private Long coupleSavings;
    private String requestDate;
    private TaskInfo taskInfo;
    private Long countReviewsCS;
}
