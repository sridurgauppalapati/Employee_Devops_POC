package com.employee.app.controller;

import com.employee.app.model.Employee;
import com.employee.app.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("pageTitle", "All Employees");
        return "employees/list";
    }

    @GetMapping("/search")
    public String searchEmployees(@RequestParam(value = "q", required = false) String query, Model model) {
        model.addAttribute("employees", employeeService.search(query));
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("pageTitle", "Search Employees");
        return "employees/search";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee(null, "", "", "", "", ""));
        model.addAttribute("pageTitle", "Add Employee");
        return "employees/form";
    }

    @PostMapping
    public String createEmployee(@Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Employee");
            return "employees/form";
        }
        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully.");
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
        model.addAttribute("employee", employee);
        model.addAttribute("pageTitle", "Update Employee");
        return "employees/form";
    }

    @PostMapping("/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Update Employee");
            return "employees/form";
        }
        employeeService.save(employee.withId(id));
        redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully.");
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (employeeService.delete(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found.");
        }
        return "redirect:/employees";
    }
}
