package com.capstone2.gamestoreproductservice.controller;

import com.capstone2.gamestoreproductservice.dao.ProductDaoJdbcTemplateImpl;
import com.capstone2.gamestoreproductservice.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductDaoJdbcTemplateImpl mockProductDao;

    private ObjectMapper om = new ObjectMapper();

    private Product testProduct;
    @Before
    public void setUp() {
        testProduct = new Product(
                "OnePlus6",
                "Unlocked Phone",
                new BigDecimal(625.00) ,
                new BigDecimal(300)
        );
    }

    @Test
    public void testPostProduct() throws Exception {
        Product productReturned = new Product(
                "OnePlus6",
                "Unlocked Phone",
                new BigDecimal (625.00) ,
                new BigDecimal(300)
        );
        productReturned.setId(1);

        String inputProductJson = om.writeValueAsString(testProduct);
        String outputProductJson = om.writeValueAsString(productReturned);

        Mockito.when(mockProductDao.postAProduct(testProduct)).thenReturn(productReturned);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                        .content(inputProductJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputProductJson));
    }

    @Test
    public void testGetAllProductsRoute() throws Exception {
        Product product1 = new Product(
                "OnePlus4",
                "Phone",
                new BigDecimal (425.00) ,
                new BigDecimal(150)
        );
        product1.setId(1);

        Product product2= new Product(
                "OnePlus7",
                "Unlocked Phone",
                new BigDecimal (700) ,
                new BigDecimal(300)
        );
        product2.setId(2);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Mockito.when(mockProductDao.getAllProducts()).thenReturn(products);

        String listProductJson = om.writeValueAsString(products);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(listProductJson)
        );
    }

    @Test
    public void testGetProductByIdRoute() throws Exception {
        Product product1 = new Product(
                "OnePlus4",
                "Phone",
                new BigDecimal (425.00) ,
                new BigDecimal(150)
        );
        product1.setId(1);

        Mockito.when(mockProductDao.getProductById(1)).thenReturn(product1);

        String productJson = om.writeValueAsString(product1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(productJson));

    }

    @Test
    public void testPutProductRoute() throws Exception {
        testProduct.setId(1);

        Product product1 = new Product(
                "OnePlus5",
                "Unlocked Phone",
                new BigDecimal (525.00) ,
                new BigDecimal(250)
        );
        product1.setId(0);

        String validProduct = om.writeValueAsString(testProduct);
        String invalidProduct = om.writeValueAsString(product1);

        Mockito.when(mockProductDao.updateProduct(testProduct)).thenReturn("success");
        Mockito.when(mockProductDao.updateProduct(product1)).thenReturn("unsuccessful");

        this.mockMvc.perform(MockMvcRequestBuilders.put("/products")
                    .content(validProduct)
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/products")
                .content(invalidProduct)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("unsuccessful"));

    }

    @Test
    public void testDeleteProductRoute() throws Exception {
        Mockito.when(mockProductDao.deleteProduct(1)).thenReturn("Deleted");
        Mockito.when(mockProductDao.deleteProduct(0)).thenReturn("Not deleted");

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/products/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/products/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Not deleted"));
    }

}
