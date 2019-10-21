package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    ChatRepository chatRepository;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("chats", chatRepository.findAll());
        return "index";
    }

    @RequestMapping("/add")
    public String chatForm(Model model){
        model.addAttribute("chat", new Chat());
        return "chatForm";
    }

    @PostMapping("/process")
    public String processForm(@Valid Chat chat, BindingResult result){
        if (result.hasErrors()){
            return "chatForm";
        }
        chatRepository.save(chat);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showChat(@PathVariable("id") long id, Model model){
        model.addAttribute("chat", chatRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateChat(@PathVariable("id") long id, Model model){
        model.addAttribute("chat", chatRepository.findById(id).get());
        return "chatForm";
    }

    @RequestMapping("/delete/{id}")
    public String delChat(@PathVariable("id") long id){
        chatRepository.deleteById(id);
        return "redirect:/";
    }
}
