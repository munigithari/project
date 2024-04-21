/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.web.bind.annotation.*;
 * 
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 */

// Write your code here

package com.example.findmyproject.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import com.example.findmyproject.model.Project;
import com.example.findmyproject.model.Researcher;
import com.example.findmyproject.service.*;

@RestController
public class ResearcherController {
    @Autowired
    private ResearcherJpaService service;

    @GetMapping("/researchers/projects")
    public List<Researcher> getResearchers() {
        return service.getResearchers();
    }

    @GetMapping("/researchers/projects/{researcherId}")
    public Researcher getResearcherById(@PathVariable("researcherId") int researcherId) {
        return service.getResearcherById(researcherId);
    }

    @PostMapping("/researchers/projects")
    public Researcher addResearcher(@RequestBody Researcher researcher) {
        return service.addResearcher(researcher);
    }

    @PutMapping("/researchers/projects/{researcherId}")
    public Researcher updateResearcher(@PathVariable("researcherId") int researcherId,
            @RequestBody Researcher researcher) {
        return service.updateResearcher(researcherId, researcher);
    }

    @DeleteMapping("/researchers/projects/{researcherId}")
    public void deleteResearcher(@PathVariable("researcherId") int researcherId) {
        service.deleteResearcher(researcherId);
    }

    @GetMapping("/researchers/{researcherId}/projects")
    public List<Project> getProjectResearcher(@PathVariable("researcherId") int researcherId) {
        return service.getProjectResearcher(researcherId);
    }
}