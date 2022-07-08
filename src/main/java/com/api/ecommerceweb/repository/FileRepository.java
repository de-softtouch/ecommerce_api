package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileUpload, Long> {

    Optional<FileUpload> findByName(String name);
}
