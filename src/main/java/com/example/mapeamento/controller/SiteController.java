package com.seuprojeto.sitemapper.controller;

import com.seuprojeto.sitemapper.model.Site;
import com.seuprojeto.sitemapper.repository.SiteRepository;
import com.seuprojeto.sitemapper.service.SiteCheckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class SiteController {
    private final SiteRepository repo;
    private final SiteCheckService service;

    public SiteController(SiteRepository repo, SiteCheckService service) {
        this.repo = repo;
        this.service = service;
    }

    // Página inicial
    @GetMapping
    public String index(Model model) {
        model.addAttribute("sites", repo.findAll());
        return "index"; // renderiza index.html
    }

    // Alternativa: acessar diretamente /sites
    @GetMapping("/sites")
    public String listSites(Model model) {
        model.addAttribute("sites", repo.findAll());
        return "index"; // mesma view da página inicial
    }

    // Adicionar novo site
    @PostMapping("/sites")
    public String add(@RequestParam String url) {
        if (!repo.existsByUrl(url)) {
            Site s = new Site();
            s.setUrl(url.trim());
            repo.save(s);
        }
        return "redirect:/"; // volta para a página inicial
    }

    // Forçar verificação de um site
    @PostMapping("/sites/{id}/check")
    public String check(@PathVariable Long id) {
        var s = repo.findById(id).orElseThrow();
        service.check(s);
        return "redirect:/";
    }

    // Remover site
    @PostMapping("/sites/{id}/delete")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/";
    }

    // Histórico de verificações
    @GetMapping("/sites/{id}/history")
    public String history(@PathVariable Long id, Model model) {
        var site = repo.findById(id).orElseThrow();
        var checks = service.history(site);

        // Extrair dados para o gráfico
        var labels = checks.stream().map(c -> c.getCheckedAt().toString()).toList();
        var statusCodes = checks.stream().map(c -> c.getStatusCode()).toList();
        var responseTimes = checks.stream().map(c -> c.getResponseTimeMs()).toList();

        model.addAttribute("site", site);
        model.addAttribute("checks", checks);
        model.addAttribute("labels", labels);
        model.addAttribute("statusCodes", statusCodes);
        model.addAttribute("responseTimes", responseTimes);

        return "history"; // renderiza history.html
    }
}
