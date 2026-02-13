package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Company;
import com.example.demo.model.Employee;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.EmployeeRepository;

@Controller
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping(value = "/")
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employeeList";
    }

    @RequestMapping(value = "/detail/{id}")
    public String getEmployeeById(@PathVariable(value = "id") Long id, Model model) {
        Employee employee = employeeRepository.getById(id);
        model.addAttribute("employee", employee);
        return "employeeDetail";
    }

    @GetMapping(value = "/update/{id}")
    public String updateEmployee(
            @PathVariable(value = "id") Long id, Model model) {
        Employee employee = employeeRepository.getById(id);
        model.addAttribute("employee", employee);

        return "employeeUpdate";
    }

    @PostMapping(value = "/save")
    public String saveUpdate(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/detail/" + employee.getId();
    }

    @GetMapping(value = "/add")
    public String addEmployee(Model model) {
        Employee employee = new Employee();
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("employee", employee);
        model.addAttribute("companies", companies);
        return "employeeAdd";
    }

    @RequestMapping(value = "/insert")
    public String insertEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/detail/" + employee.getId();
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteEmployee(@PathVariable(value = "id") Long id) {
        if (employeeRepository.findById(id).isPresent()) {
            Employee employee = employeeRepository.findById(id).get();

            employeeRepository.delete(employee);
        }
        return "redirect:/";
    }

}
