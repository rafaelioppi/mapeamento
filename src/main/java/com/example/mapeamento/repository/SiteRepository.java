package com.seuprojeto.sitemapper.repository;

import com.seuprojeto.sitemapper.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
    boolean existsByUrl(String url);
}
