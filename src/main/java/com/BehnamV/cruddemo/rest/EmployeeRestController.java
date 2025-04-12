package com.BehnamV.cruddemo.rest;

import com.BehnamV.cruddemo.dao.EmployeeDAO;
import com.BehnamV.cruddemo.entity.Employee;
import com.BehnamV.cruddemo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
    private EmployeeService employeeService;
    private ObjectMapper objectMapper;
    //quick and dirt:inject employee dao
    @Autowired
    public EmployeeRestController(EmployeeService theEmployeeService,ObjectMapper theObjectMapper){
        employeeService = theEmployeeService;
        objectMapper = theObjectMapper;
    }
    // expose"/employees" and return a list of employees
    @GetMapping("employees")
    public List<Employee> findAll(){
        return employeeService.findAll();
    }
    //find employee by id get expose "employees/{employeeId}"
    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId){
        Employee theEmployee = employeeService.findById(employeeId);
        if(theEmployee == null){
            throw  new RuntimeException("Employee id not found -"+employeeId);
        }
        return theEmployee;
    }
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee){
        theEmployee.setId(0);
        Employee dbEmployee =employeeService.save(theEmployee);
        return dbEmployee;
    }
    // add mapping for put /employees -updaete existing employee
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee){
        Employee dbEmployee =employeeService.save(theEmployee);
        return dbEmployee;
    }
    //patch method for partial update /employees/{employeeId}
    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId, @RequestBody Map<String,Object>patchPayload){
        Employee tempEmployee = employeeService.findById(employeeId);
        //throw exception if null
        if(tempEmployee ==null){
            throw new RuntimeException("Employee Not find -"+employeeId);
        }
        //throw exception if request body contain
        if(patchPayload.containsKey("id")){
            throw new RuntimeException("Employee id not allowed in request body - "+employeeId);
        }
        Employee patchEmployeed = apply(patchPayload,tempEmployee);
        Employee dbEmployee = employeeService.save(patchEmployeed);
        return dbEmployee;

    }

    private Employee apply(Map<String, Object> patchPayload, Employee tempEmployee) {
        //convert employee object to a json object node
        ObjectNode employeeNode =objectMapper.convertValue(tempEmployee,ObjectNode.class);
        //convert the path payload mapt to json object node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload,ObjectNode.class);
        //Merge the patch updates into employee node
        employeeNode.setAll(patchNode);
        return objectMapper.convertValue(employeeNode,Employee.class);
    }
    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId){
        Employee tempEmployee = employeeService.findById(employeeId);
        //throw exception if null
        if(tempEmployee ==null){
            throw new RuntimeException("Employee Not find -"+employeeId);
        }
        employeeService.deleteById(employeeId);
        return "Delete employee id - "+employeeId;

    }


}
