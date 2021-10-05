package com.galvanize.customerapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
class CustomerapiApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@BeforeEach
	public void beforeEach() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers"));
	}

	@Test
	public void testAddAndRetrieveCustomer() throws Exception {
		mockMvc.perform(post("/api/customers")
				.content("{\n" +
						"    \"firstName\": \"Araminta\",\n" +
						"    \"lastName\": \"Ross\",\n" +
						"    \"address\": \"1849 Harriet Ave, Auburn, NY 23102\",\n" +
						"    \"phoneNumber\": \"309-555-1370\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("firstName").value("Araminta"))
				.andExpect(jsonPath("lastName").value("Ross"))
				.andExpect(jsonPath("address").value("1849 Harriet Ave, Auburn, NY 23102"))
				.andExpect(jsonPath("phoneNumber").value("309-555-1370"))
				.andExpect(jsonPath("id").isNotEmpty());
	}


	@Test
	public void testGetAllCustomers() throws Exception {

		 ObjectMapper om = new ObjectMapper();


		//Arrange
		mockMvc.perform(
				post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\n" +
								"        \"firstName\": \"Salvator\",\n" +
								"        \"lastName\": \"Di'Mario\",\n" +
								"        \"address\": \"45 Carver Ave, Midland, TX 70134\",\n" +
								"        \"phoneNumber\": \"510-555-7863\"\n" +
								"    }")
		);
		mockMvc.perform(
				post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\n" +
								"        \"firstName\": \"Qin\",\n" +
								"        \"lastName\": \"Zhang\",\n" +
								"        \"address\": \"1 Main Street, Topeka, KS 37891\",\n" +
								"        \"phoneNumber\": \"510-555-2367\"\n" +
								"    }")
		);

		List customers = om.readValue(mockMvc.perform(get("/api/customers"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString(), List.class);





		String firstName = "Salvator", lastname = "Di'Mario", address = "45 Carver Ave, Midland, TX 70134", pn= "510-555-7863";
		String firstName2 = "Qin", lastname2 = "Zhang", address2 = "1 Main Street, Topeka, KS 37891", pn2= "510-555-2367";
		List filteredList = (List) customers.stream().filter(
				cq -> {
					LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) cq;
					return (map.get("firstName").equals(firstName) &&
							map.get("lastName").equals(lastname) &&
							map.get("address").equals(address) &&
							map.get("phoneNumber").equals(pn))
							|| (
							map.get("firstName").equals(firstName2) &&
									map.get("lastName").equals(lastname2) &&
									map.get("address").equals(address2) &&
									map.get("phoneNumber").equals(pn2)
					);}).collect(Collectors.toList());

		Assertions.assertEquals(2, filteredList.size());

	}




	@Test
	public void testRetrieveCustomer() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Customer customer = new Customer(null, "Hanaan", "Altalib",
				"1826 Truth Place, New York, NY, 20127", "204-555-9753");
		String res = mockMvc.perform(post("/api/customers")
				.content(objectMapper.writeValueAsString(customer))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("firstName").value("Hanaan"))
				.andExpect(jsonPath("lastName").value("Altalib"))
				.andExpect(jsonPath("address").value("1826 Truth Place, New York, NY, 20127"))
				.andExpect(jsonPath("phoneNumber").value("204-555-9753"))
				.andExpect(jsonPath("id").isNotEmpty())
				.andReturn().getResponse().getContentAsString();
		Customer tempCustomer = objectMapper.readValue(res, Customer.class);

		UUID customerId = tempCustomer.getId();

		mockMvc.perform(get("/api/customers/" + customerId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("firstName").value("Hanaan"))
				.andExpect(jsonPath("lastName").value("Altalib"))
				.andExpect(jsonPath("address").value("1826 Truth Place, New York, NY, 20127"))
				.andExpect(jsonPath("phoneNumber").value("204-555-9753"));
	}


}
