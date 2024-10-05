package com.testreact.reacttest.repo;

import com.testreact.reacttest.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
