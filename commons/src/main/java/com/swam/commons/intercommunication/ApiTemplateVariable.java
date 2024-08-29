package com.swam.commons.intercommunication;

public final class ApiTemplateVariable {

    // Type
    public static final ApiTemplateVariable TARGET_TYPE = new ApiTemplateVariable("type");
    public static final ApiTemplateVariable TARGET_TYPE_PATTERN = new ApiTemplateVariable("/{" + TARGET_TYPE + "}");
    // WorkflowTemplate
    public static final ApiTemplateVariable WORKFLOW_ID = new ApiTemplateVariable("workflowId");
    public static final ApiTemplateVariable WORKFLOW_ID_PATTERN = new ApiTemplateVariable("/{" + WORKFLOW_ID + "}");
    // Product
    public static final ApiTemplateVariable PRODUCT_ID = new ApiTemplateVariable("productId");
    public static final ApiTemplateVariable PRODUCT_ID_PATTERN = new ApiTemplateVariable("/{" + PRODUCT_ID + "}");
    // Analysis
    public static final ApiTemplateVariable ANALYSIS_ID = new ApiTemplateVariable("analysisId");
    public static final ApiTemplateVariable ANALYSIS_ID_PATTERN = new ApiTemplateVariable("/{" + ANALYSIS_ID + "}");

    private final String templateVariable;

    private ApiTemplateVariable(String templateVariable) {
        this.templateVariable = templateVariable;
    }

    public String value() {
        return this.templateVariable;
    }

    @Override
    public String toString() {
        return templateVariable;
    }
}
