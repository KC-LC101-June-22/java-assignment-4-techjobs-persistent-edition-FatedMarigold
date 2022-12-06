package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");
        model.addAttribute("jobs", jobRepository.findAll());

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers",employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            model.addAttribute("employers",employerRepository.findAll());
            model.addAttribute("skills", skillRepository.findAll());
            return "add";
        }

        Optional<Employer> oEmployer = employerRepository.findById(employerId);
        if (!oEmployer.isPresent()) {
            model.addAttribute("title", "Employer Unavailable With Id" + employerId);
        } else {
            Employer employer = oEmployer.get();
            newJob.setEmployer(employer);
        }

//        Employer employer = employerRepository.findById(employerId).orElse(new Employer());
//        newJob.setEmployer(employer);
//
//        List<Skill> skillList = new ArrayList<>();
//        if (skills != null) {
//            for (int skillId : skills) {
//                Optional<Skill> oSkill = skillRepository.findById(skillId);
//                if (oSkill.isEmpty()) {
//                    model.addAttribute("title", "Skill Unavailable with Id" + skillId);
//                    return "add";
//                } else {
//                    skillList.add(oSkill.get());
//                }
//            }
//            newJob.setSkills((List<Skill>) skillRepository.findAllById(skills));
//        }

        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);
//        skillRepository.findAllById(skills);

        jobRepository.save(newJob);

        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {


        Optional <Job> ojob = jobRepository.findById(jobId);
        if (ojob.isEmpty()) {
            model.addAttribute("title", "Job Unavailable With Id " + jobId);
            return "jobs";
        } else {
            Job job = ojob.get();
            model.addAttribute(job);
        }

        return "view";
    }


}
