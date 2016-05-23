package com.epam.mentoring.restapi.controller;

import com.epam.mentoring.restapi.modal.Employee;
import com.epam.mentoring.restapi.repository.EmployeeRepository;
import com.epam.mentoring.restapi.web.exception.BadRequestError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;
import org.apache.log4j.Logger;

@RestController
public class EmployeeController {

    private Logger LOG = Logger.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeRepository repository;

    @RequestMapping(method= RequestMethod.GET, value = "/employee")
    public List<Employee> getAllEmployee(){
        return repository.findAll();
    }

    @RequestMapping(method= RequestMethod.GET, value = "/employee/id/{id}")
    @ResponseBody
    public Employee getEmployeeById(@PathVariable Long id){
        Employee employee = repository.findOne(id);
        if(employee == null){
            throw new BadRequestError("Employee with id["+id+"] not found");
        }
        return employee;
    }

    @RequestMapping(method= RequestMethod.GET, value = "/employee/name/{name}")
    @ResponseBody
    public List<Employee> getEmployeeByName(@PathVariable String name){
        return repository.findByName(URLDecoder.decode(name));
    }

    @RequestMapping(method= RequestMethod.POST, value= "/employee", headers="Accept=application/json")
    @ResponseBody
    public Employee save(@RequestBody Employee employee) {
        LOG.info("Saving Employee: " + employee.getName());
        repository.save(employee);
        return employee;
    }

    //add Restful services to put, response 404(Not Found) when encounter the exception that the id is not exist
    @RequestMapping(method= RequestMethod.PUT, value= "employee/{id}", headers="Accept=application/json")
    @ResponseBody
    public Employee update(@RequestBody Employee employee, @PathVariable long id){
        LOG.info("Update Employee: " + id + "," + employee.getName());
        Employee oldEmployee = repository.findOne(id);
        if(oldEmployee == null){
            throw new BadRequestError("Employee with id["+id+"] not found");
        }
        employee.setId(oldEmployee.getId());
        return repository.save(employee);
    }


    @RequestMapping(method= RequestMethod.DELETE, value= "employee/{id}", headers="Accept=application/json")
    public void delete(@PathVariable long id){
        LOG.info("Delete Employee: "+ id);
        repository.delete(id);
    }

}
