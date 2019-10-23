package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    CloudinaryConfig cloudc;

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


    @PostMapping("/process")
    public String processChat(@ModelAttribute Chat chat,
                              @RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            chatRepository.save(chat);
            return "redirect:/";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            chat.setPic(uploadResult.get("url").toString());

        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }
        chatRepository.save(chat);
        return "redirect:/";
    }
}
