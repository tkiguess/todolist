package com.example.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    
    @Autowired
     TodoRepository todoRepository;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/register", method= RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value ="/register", method=RequestMethod.POST)
    public String processRegistrationPage(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model){
        model.addAttribute("user", user); //here
        if (result.hasErrors()){
            return "registration";
        }else{
            userService.saveUser(user);
            model.addAttribute("message",
                    "User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("/update/{id}")
    public String userUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("todo", todoRepository.findById(id).get());
        return "add";
    }
    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        todoRepository.deleteById(id);
        return "redirect:/todolist";
    }

    @RequestMapping("/detail/{id}")
    public String showJob(@PathVariable("id") long id, Model model){
        model.addAttribute("todo", todoRepository.findById(id).get());
        return "show";
    }

    @GetMapping("/add")
    public String addTodo(Model model){
        model.addAttribute("todo", new Todo());
        return "add";
    }


    @PostMapping("/process2")
    public String processForm( @ModelAttribute Todo todo, BindingResult result, Model model)
    {
        String username = getUser().getUsername();
        todo.setUsername(username);
        todoRepository.save(todo);
        model.addAttribute("todos", todoRepository.findByUsername(username));
        return "todolist";
    }


    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentusername = authentication.getName();
        User user = userRepository.findByUsername(currentusername);
        return user;
    }
    @RequestMapping("/")
    public  String index(Model model){

        model.addAttribute("todos", todoRepository.findAll());
        return "index";
    }

    @RequestMapping("/maintenance")
    public  String windex(Model model){

        return "maintenance";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/logout2")
    public String logout2() {
        return "logoutout";
    }

    @RequestMapping("/todolist")
    public String todolist(HttpServletRequest request, Authentication authentication, Principal principal, Model model){
        Boolean isAdmin = request.isUserInRole("ADMIN");
        Boolean isUser = request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails)
                authentication.getPrincipal();

        String username = principal.getName();
        model.addAttribute("todos", todoRepository.findByUsername(username));
        return "todolist";
    }
}