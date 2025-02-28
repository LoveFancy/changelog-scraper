package com.cursor.model;

public enum RequirementState {
    // 初始状态
    DRAFT("草稿"),
    
    // 需求分析阶段
    ANALYSIS_IN_PROGRESS("分析中"),
    ANALYSIS_COMPLETED("分析完成"),
    
    // 架构设计阶段
    DESIGN_TASK_CREATED("设计任务已创建"),
    ARCHITECTURE_REVIEW("架构评审中"),
    DESIGN_IN_PROGRESS("设计文档编写中"),
    DESIGN_COMPLETED("设计文档完成"),
    
    // 审批阶段
    APPROVAL_IN_PROGRESS("审批中"),
    APPROVAL_REJECTED("审批驳回"),
    APPROVAL_COMPLETED("审批通过"),
    
    // 排期阶段
    SCHEDULING("排期中"),
    SCHEDULED("已排期"),
    
    // 终态
    COMPLETED("完成"),
    CANCELLED("已取消");

    private final String description;

    RequirementState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 