package com.seuprojeto.sitemapper.repository;

import com.seuprojeto.sitemapper.model.SiteCheck;
import com.seuprojeto.sitemapper.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteCheckRepository extends JpaRepository<SiteCheck, Long> {
    List<SiteCheck> findBySiteOrderByCheckedAtDesc(Site site);
}
