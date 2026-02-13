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
import com.example.demo.repository.CompanyRepository;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping(value = "/{id}")
    public String getCompanyById(@PathVariable(value = "id") Long id, Model model) {
        Company company = companyRepository.getById(id);
        model.addAttribute("company", company);
        model.addAttribute("employees", company.getEmployees());
        return "companyDetail";
    }

    @RequestMapping(value = "/list")
    public String getAllCompanies(Model model) {
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        return "companyList.html";
    }

    @GetMapping(value = "/add")
    public String addCompany(Model model) {
        Company company = new Company();
        model.addAttribute("company", company);
        return "companyAdd";
    }

    @PostMapping(value = "/save")
    public String saveCompany(Company company) {
        companyRepository.save(company);
        return "redirect:/company/" + company.getId();
    }

    @GetMapping(value = "/update/{id}")
    public String updateCompany(@PathVariable(value = "id") Long id, Model model) {
        Company company = companyRepository.getById(id);
        model.addAttribute("company", company);
        return "companyUpdate";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteCompany(@PathVariable(value = "id") Long id, Model model) {
        if (companyRepository.findById(id).isPresent()) {
            companyRepository.deleteById(id);
        }
        return "redirect:/company/list";
    }
}


