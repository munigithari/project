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

import com.example.findmyproject.model.Project;
import com.example.findmyproject.model.Researcher;
import com.example.findmyproject.repository.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class ResearcherJpaService implements ResearcherRepository {
    @Autowired
    private ResearcherJpaRepository repository2;

    @Autowired
    private ProjectJpaRepository repository;

    @Override
    public List<Researcher> getResearchers() {
        List<Researcher> sponsorList = repository2.findAll();
        ArrayList<Researcher> sponsors = new ArrayList<>(sponsorList);
        return sponsors;
    }

    @Override
    public Researcher getResearcherById(int researcherId) {
        try {
            Researcher sponsor = repository2.findById(researcherId).get();
            return sponsor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Researcher addResearcher(Researcher sponsor) {
        List<Integer> eventIds = new ArrayList<>();
        for (Project event : sponsor.getProjects()) {
            eventIds.add(event.getProjectId());
        }

        List<Project> events = repository.findAllById(eventIds);

        if (events.size() != eventIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        sponsor.setProjects(events);

        return repository2.save(sponsor);
    }

    @Override
    public Researcher updateResearcher(int researcherId, Researcher sponsor) {
        try {
            Researcher newSponsor = repository2.findById(researcherId).get();
            if (sponsor.getResearcherName() != null) {
                newSponsor.setResearcherName(sponsor.getResearcherName());
            }
            if (sponsor.getSpecialization() != null) {
                newSponsor.setSpecialization(sponsor.getSpecialization());
            }
            if (sponsor.getProjects() != null) {
                List<Integer> eventIds = new ArrayList<>();
                for (Project event : sponsor.getProjects()) {
                    eventIds.add(event.getProjectId());
                }
                List<Project> events = repository.findAllById(eventIds);
                if (events.size() != eventIds.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                newSponsor.setProjects(events);
            }
            return repository2.save(newSponsor);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteResearcher(int researcherId) {
        try {
            repository2.deleteById(researcherId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Project> getProjectResearcher(int researcherId) {
        try {
            Researcher sponsor = repository2.findById(researcherId).get();
            return sponsor.getProjects();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
