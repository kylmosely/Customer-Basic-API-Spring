package com.mosely.SpringThink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class SpringThinkApplication {
	private final CustomerRepository customerRepository;

	public SpringThinkApplication(CustomerRepository customerRepository){
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringThinkApplication.class, args);
	}
	@GetMapping
	public List<Customer> getCustomers(){
		return customerRepository.findAll();
	}

	record NewCustomerRequest(
			String name,
			String email,
			Integer age
	){}
	@PostMapping
	public void addCustomer(@RequestBody NewCustomerRequest request){
		Customer customer = new Customer();
		customer.setName(request.name());
		customer.setEmail(request.email());
		customer.setAge(request.age());
		customerRepository.save(customer);
	}

	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer id) {
		customerRepository.deleteById(id);
	}

	record UpdateCustomerRequest(
			String name,
			String email,
			Integer age
	) {}

	@PutMapping("{customerId}")
	public ResponseEntity<Customer> updateCustomerRequest(@PathVariable("customerId")Integer id,
														  @Validated @RequestBody Customer customerDetails) throws Exception{
		Customer customer = customerRepository.findById(id).orElseThrow(() -> new Exception("Id not found"));
		customer.setEmail(customerDetails.getEmail());
		customer.setName(customerDetails.getName());
		customer.setAge(customerDetails.getAge());
		final Customer updatedCustomer = customerRepository.save(customer);
		return ResponseEntity.ok(updatedCustomer);
	}
}