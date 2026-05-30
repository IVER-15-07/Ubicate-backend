package com.tulocal.backend.modules.admin.domain.repository;

import com.tulocal.backend.modules.admin.domain.model.AdminBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdminBranchRepository extends JpaRepository<AdminBranch, UUID> {

	List<AdminBranch> findByIsActiveTrue();
}

