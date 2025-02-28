package com.cursor.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class Requirement {
    private String id;
    private String title;
    private String description;
    private RequirementState state;
    private String createdBy;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String currentApprover;
    private String designDocUrl;
    
    private final RequirementStateManager stateManager;

    public Requirement() {
        this.state = RequirementState.DRAFT;
        this.stateManager = new RequirementStateManager();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void transitionTo(RequirementState targetState) {
        stateManager.validateTransition(this.state, targetState);
        this.state = targetState;
        this.updatedAt = LocalDateTime.now();
    }

    public Set<RequirementState> getNextPossibleStates() {
        return stateManager.getNextPossibleStates(this.state);
    }

    public boolean canTransitionTo(RequirementState targetState) {
        return stateManager.canTransitionTo(this.state, targetState);
    }
} 