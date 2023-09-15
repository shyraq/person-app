package org.example.controllers;

import jakarta.validation.Valid;
import org.example.models.Person;
import org.example.services.ItemService;
import org.example.services.PeopleService;
import org.example.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    private final ItemService itemService;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator, ItemService itemService) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.itemService = itemService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());

        itemService.findByOwner(peopleService.findOne(0));
        itemService.findByItemName("IPad");
        peopleService.test();

        return "people/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson (Model model) {
        model.addAttribute("person", new Person());

        return "people/new";
    }

    @PostMapping()
    public String createPerson (@ModelAttribute ("person") @Valid Person person,
                                BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson (@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson (@PathVariable("id") int id, @ModelAttribute ("person") @Valid Person person,
                                BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit";
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id){
        peopleService.delete(id);
        return "redirect:/people";
    }
}
