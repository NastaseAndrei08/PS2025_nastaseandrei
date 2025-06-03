package com.example.demo.repository;

import com.example.demo.entity.ModerationAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ModerationActionRepository extends JpaRepository<ModerationAction, Long> {
    boolean existsByUserIdAndActionType(Long userId, ModerationAction.ActionType actionType);
    @Transactional
    @Modifying
    @Query("DELETE FROM ModerationAction m WHERE m.userId = :userId AND m.actionType = :actionType")
    void deleteByUserIdAndActionType(@Param("userId") Long userId, @Param("actionType") ModerationAction.ActionType actionType);

}
