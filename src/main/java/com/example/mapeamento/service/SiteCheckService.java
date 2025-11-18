package com.seuprojeto.sitemapper.service;

import com.seuprojeto.sitemapper.model.Site;
import com.seuprojeto.sitemapper.model.SiteCheck;
import com.seuprojeto.sitemapper.repository.SiteRepository;
import com.seuprojeto.sitemapper.repository.SiteCheckRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.Duration;
import java.util.List;

@Service
public class SiteCheckService {
    private final SiteRepository siteRepo;
    private final SiteCheckRepository checkRepo;
    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public SiteCheckService(SiteRepository siteRepo, SiteCheckRepository checkRepo) {
        this.siteRepo = siteRepo;
        this.checkRepo = checkRepo;
    }

    public SiteCheck check(Site site) {
        long start = System.nanoTime();
        SiteCheck sc = new SiteCheck();
        sc.setSite(site);

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(site.getUrl()))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<Void> resp = client.send(req, HttpResponse.BodyHandlers.discarding());
            sc.setStatusCode(resp.statusCode());
        } catch (Exception e) {
            sc.setStatusCode(0);
            System.err.println("Erro ao verificar site " + site.getUrl() + ": " + e.getMessage());
        }

        long elapsed = System.nanoTime() - start;
        sc.setResponseTimeMs(Duration.ofNanos(elapsed).toMillis());
        sc.setCheckedAt(Instant.now());

        // Atualizar também o Site
        site.setLastStatus(sc.getStatusCode());
        site.setLastResponseMs(sc.getResponseTimeMs());
        site.setLastCheckedAt(sc.getCheckedAt());
        siteRepo.save(site);

        return checkRepo.save(sc);
    }

    public List<SiteCheck> history(Site site) {
        return checkRepo.findBySiteOrderByCheckedAtDesc(site);
    }
}
