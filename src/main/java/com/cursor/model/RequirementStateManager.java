package com.cursor.model;

import java.util.*;

public class RequirementStateManager {
    private static final Map<RequirementState, Set<RequirementState>> ALLOWED_TRANSITIONS = new HashMap<>();

    static {
        // 初始化允许的状态转换
        // 从草稿开始
        ALLOWED_TRANSITIONS.put(RequirementState.DRAFT, 
            new HashSet<>(Arrays.asList(RequirementState.ANALYSIS_IN_PROGRESS, RequirementState.CANCELLED)));

        // 分析阶段
        ALLOWED_TRANSITIONS.put(RequirementState.ANALYSIS_IN_PROGRESS,
            new HashSet<>(Arrays.asList(RequirementState.ANALYSIS_COMPLETED, RequirementState.CANCELLED)));
        ALLOWED_TRANSITIONS.put(RequirementState.ANALYSIS_COMPLETED,
            new HashSet<>(Arrays.asList(RequirementState.DESIGN_TASK_CREATED, RequirementState.CANCELLED)));

        // 设计阶段
        ALLOWED_TRANSITIONS.put(RequirementState.DESIGN_TASK_CREATED,
            new HashSet<>(Arrays.asList(RequirementState.ARCHITECTURE_REVIEW, RequirementState.CANCELLED)));
        ALLOWED_TRANSITIONS.put(RequirementState.ARCHITECTURE_REVIEW,
            new HashSet<>(Arrays.asList(RequirementState.DESIGN_IN_PROGRESS, RequirementState.CANCELLED)));
        ALLOWED_TRANSITIONS.put(RequirementState.DESIGN_IN_PROGRESS,
            new HashSet<>(Arrays.asList(RequirementState.DESIGN_COMPLETED, RequirementState.CANCELLED)));
        ALLOWED_TRANSITIONS.put(RequirementState.DESIGN_COMPLETED,
            new HashSet<>(Arrays.asList(RequirementState.APPROVAL_IN_PROGRESS, RequirementState.CANCELLED)));

        // 审批阶段
        ALLOWED_TRANSITIONS.put(RequirementState.APPROVAL_IN_PROGRESS,
            new HashSet<>(Arrays.asList(RequirementState.APPROVAL_COMPLETED, 
                RequirementState.APPROVAL_REJECTED, RequirementState.CANCELLED)));
        ALLOWED_TRANSITIONS.put(RequirementState.APPROVAL_REJECTED,
            new HashSet<>(Arrays.asList(RequirementState.DESIGN_IN_PROGRESS, RequirementState.CANCELLED)));
        ALLOWED_TRANSITIONS.put(RequirementState.APPROVAL_COMPLETED,
            new HashSet<>(Arrays.asList(RequirementState.SCHEDULING, RequirementState.CANCELLED)));

        // 排期阶段
        ALLOWED_TRANSITIONS.put(RequirementState.SCHEDULING,
            new HashSet<>(Arrays.asList(RequirementState.SCHEDULED, RequirementState.CANCELLED)));
        ALLOWED_TRANSITIONS.put(RequirementState.SCHEDULED,
            new HashSet<>(Arrays.asList(RequirementState.COMPLETED, RequirementState.CANCELLED)));

        // 终态
        ALLOWED_TRANSITIONS.put(RequirementState.COMPLETED, Collections.emptySet());
        ALLOWED_TRANSITIONS.put(RequirementState.CANCELLED, Collections.emptySet());
    }

    public boolean canTransitionTo(RequirementState currentState, RequirementState targetState) {
        Set<RequirementState> allowedStates = ALLOWED_TRANSITIONS.get(currentState);
        return allowedStates != null && allowedStates.contains(targetState);
    }

    public void validateTransition(RequirementState currentState, RequirementState targetState) {
        if (!canTransitionTo(currentState, targetState)) {
            throw new IllegalStateException(
                String.format("Invalid state transition from %s to %s", 
                    currentState.getDescription(), 
                    targetState.getDescription())
            );
        }
    }

    public Set<RequirementState> getNextPossibleStates(RequirementState currentState) {
        return ALLOWED_TRANSITIONS.getOrDefault(currentState, Collections.emptySet());
    }
} 