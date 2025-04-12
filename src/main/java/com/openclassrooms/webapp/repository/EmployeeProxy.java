package com.openclassrooms.webapp.repository;

import com.openclassrooms.webapp.CustomProperties;
import com.openclassrooms.webapp.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class EmployeeProxy {

    @Autowired
    private CustomProperties props;

    /**
     * Get all employees
     * @return An iterable of all employees
     */

    final RestTemplate restTemplate = new RestTemplate();

    public Iterable<Employee> getEmployees() {
        String baseApiUrl = props.getApiUrl();
        String getEmployeesUrl = baseApiUrl + "/employees";

        ResponseEntity<Iterable<Employee>> response = restTemplate.exchange(
                getEmployeesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Iterable<Employee>>() {}
        );

        log.debug("Get Employees call {}", response.getStatusCode().toString());

        return response.getBody();
    }

    public Employee createEmployee(Employee e) {
        String baseApiUrl = props.getApiUrl();
        String createEmployeeUrl = baseApiUrl + "/employees";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Employee> request = new HttpEntity<Employee>(e);
        ResponseEntity<Employee> response = restTemplate.exchange(
                createEmployeeUrl,
                HttpMethod.POST,
                request,
                Employee.class);

        log.debug("Create Employee call {}", response.getStatusCode().toString());

        return response.getBody();
    }

    public Employee getEmployee(int id) {
        String url = props.getApiUrl() + "/employees/" + id;
        return restTemplate.getForObject(url, Employee.class);
    }

    public void deleteEmployee(int id) {
        String url = props.getApiUrl() + "/employees/" + id;
        restTemplate.delete(url);
    }

    public Employee updateEmployee(Employee employee) {
        String url = props.getApiUrl() + "/employees/" + employee.getId();
        HttpEntity<Employee> request = new HttpEntity<>(employee);
        ResponseEntity<Employee> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Employee.class
        );
        return response.getBody();
    }
}