package com.ecommerce.shopapi;

import com.ecommerce.shopapi.ShopApiApplication;
import com.ecommerce.shopapi.dto.product.ProductCreateRequest;
import com.ecommerce.shopapi.model.ProductCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShopApiApplication.class)   // <-- ÖNEMLİ
@AutoConfigureMockMvc
@Testcontainers
class ProductIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("shop_api")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void configureProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void create_list_get_update_delete_product_happy_path() throws Exception {
        var req = new ProductCreateRequest(
                "Test Ürün", "Açıklama",
                new BigDecimal("19.99"), 10, ProductCategory.OTHER);

        var createRes = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());

        String id = objectMapper.readTree(createRes.getResponse().getContentAsString())
                .get("id").asText();

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk());

        String updateJson = """
                {"name":"Güncel Ürün","description":"Yeni açıklama",
                 "price":29.99,"stock":7,"category":"OTHER"}""";

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Güncel Ürün"));

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isNoContent());
    }
}
