/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * 
 * import java.util.*;
 *
 */

// Write your code here

package com.example.findmyproject.service;

import java.util.*;

import com.example.findmyproject.model.Project;
import com.example.findmyproject.model.Researcher;
import com.example.findmyproject.repository.*;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProjectJpaService implements ProjectRepository {
    @Autowired
    private ProjectJpaRepository repository;

    @Autowired
    private ResearcherJpaRepository repository2;

    @Override
    public List<Project> getProjects() {
        List<Project> eventList = repository.findAll();
        ArrayList<Project> events = new ArrayList<>(eventList);
        return events;
    }

    @Override
    public Project getProjectById(int projectId) {
        try {
            Project event = repository.findById(projectId).get();
            return event;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Project addProject(Project project) {
        List<Integer> researcherIds = new ArrayList<>();
        for (Researcher researcher : project.getResearchers()) {
            researcherIds.add(researcher.getResearcherId());
        }

        List<Researcher> researchers = repository2.findAllById(researcherIds);
        project.setResearchers(researchers);

        for (Researcher researcher : researchers) {
            researcher.getProjects().add(project);
        }

        Project savedProject = repository.save(project);
        repository2.saveAll(researchers);

        return savedProject;
    }

    @Override
    public Project updateProject(int projectId, Project project) {
        try {
            Project newProject = repository.findById(projectId).get();
            if (project.getProjectName() != null) {
                newProject.setProjectName(project.getProjectName());
            }
            if (project.getBudget() != 0) {
                newProject.setBudget(project.getBudget());
            }
            if (project.getResearchers() != null) {
                List<Researcher> researchers = newProject.getResearchers();
                for (Researcher researcher : researchers) {
                    researcher.getProjects().remove(newProject);
                }
                repository2.saveAll(researchers);
                List<Integer> newSponsorIds = new ArrayList<>();
                for (Researcher sponsor : project.getResearchers()) {
                    newSponsorIds.add(sponsor.getResearcherId());
                }
                List<Researcher> newSponsors = repository2.findAllById(newSponsorIds);
                for (Researcher sponsor : newSponsors) {
                    sponsor.getProjects().add(newProject);
                }
                repository2.saveAll(newSponsors);
                newProject.setResearchers(newSponsors);
            }
            return repository.save(newProject);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteProject(int projectId) {
        try {
            Project project = repository.findById(projectId).get();

            List<Researcher> researchers = project.getResearchers();
            for (Researcher researcher : researchers) {
                researcher.getProjects().remove(project);
            }

            repository2.saveAll(researchers);

            repository.deleteById(projectId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Researcher> getResearcherProjects(int projectId) {
        try {
            Project project = repository.findById(projectId).get();
            return project.getResearchers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
