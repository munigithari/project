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
import com.example.findmyproject.service.ProjectJpaService;

@RestController
public class ProjectController {
    @Autowired
    private ProjectJpaService service;

    @GetMapping("/projects")
    public List<Project> getProjects() {
        return service.getProjects();
    }

    @GetMapping("/projects/{projectId}")
    public Project getProjectById(@PathVariable("projectId") int projectId) {
        return service.getProjectById(projectId);
    }

    @PostMapping("/projects")
    public Project addProject(@RequestBody Project project) {
        return service.addProject(project);
    }

    @PutMapping("/projects/{projectId}")
    public Project updateProject(@PathVariable("projectId") int projectId, @RequestBody Project project) {
        return service.updateProject(projectId, project);
    }

    @DeleteMapping("/projects/{projectId}")
    public void deleteProject(@PathVariable("projectId") int projectId) {
        service.deleteProject(projectId);
    }

    @GetMapping("/projects/{projectId}/researchers")
    public List<Researcher> getResearcherProjects(@PathVariable("projectId") int projectId) {
        return service.getResearcherProjects(projectId);
    }
}