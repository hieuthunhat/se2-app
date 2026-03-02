package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Company;
import com.example.demo.model.Employee;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.EmployeeRepository;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping(value = "/list")
    public String getAllEmployees(
        @RequestParam(value = "company", required = false, defaultValue = "0") Long comId,
        @RequestParam(value = "sort", required = false, defaultValue = "0") int sortMode,
        Model model) {
        List<Employee> employees = null;
        List<Company> companies = companyRepository.findAll();

        Sort.Direction sortDirection = Sort.Direction.DESC;
        String sortColumn = "id";
        
        if (sortMode == 1) {
            // Oldest - sort by id ascending
            sortDirection = Sort.Direction.ASC;
            sortColumn = "id";
        } else if (sortMode == 2) {
            // By name ASC
            sortDirection = Sort.Direction.ASC;
            sortColumn = "name";
        } else if (sortMode == 3) {
            // By name DESC
            sortDirection = Sort.Direction.DESC;
            sortColumn = "name";
        }
        // sortMode == 0: Latest - sort by id descending (default)
        
        Sort sort = Sort.by(sortDirection, sortColumn);
        
        if (comId != 0) {
            Optional<Company> comp = companyRepository.findById(comId);
            if (comp.isPresent()) {
                employees = employeeRepository.findByCompany(comp.get(), sort);
            }
        }
        
        if (employees == null) {
            employees = employeeRepository.findAll(sort);
        }

        model.addAttribute("companies", companies);
        model.addAttribute("employees", employees);
        model.addAttribute("comId", comId);
        model.addAttribute("sortMode", sortMode);
        return "employeeList";
    }

    @RequestMapping(value = "/detail/{id}")
    public String getEmployeeById(@PathVariable(value = "id") Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        model.addAttribute("employee", employee);
        return "employeeDetail";
    }

    @GetMapping(value = "/update/{id}")
    public String updateEmployee(
            @PathVariable(value = "id") Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        model.addAttribute("employee", employee);

        return "employeeUpdate";
    }

    @PostMapping(value = "/save")
    public String saveUpdate(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employee/detail/" + employee.getId();
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
        return "redirect:/employee/detail/" + employee.getId();
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteEmployee(@PathVariable(value = "id") Long id) {
        if (employeeRepository.findById(id).isPresent()) {
            Employee employee = employeeRepository.findById(id).get();

            employeeRepository.delete(employee);
        }
        return "redirect:/employee/list";
    }
}
