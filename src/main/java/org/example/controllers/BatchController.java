package org.example.controllers;

import org.example.DAO.PersonDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class BatchController {
    private final PersonDAO personDAO;


    public BatchController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping("without")
    public String withoutBatch(){
        personDAO.testMultiUpdate();
        return "redirect:/people";
    }

    @GetMapping("with")
    public String withBatch(){
        personDAO.testBatchUpdate();
        return "redirect:/people";
    }

    @GetMapping()
    public String index(){
        return "batch/index";
    }


}
