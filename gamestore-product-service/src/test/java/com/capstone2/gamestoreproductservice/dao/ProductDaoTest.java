package com.capstone2.gamestoreproductservice.dao;

import com.capstone2.gamestoreproductservice.model.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    public Product expectedProduct;

    @Before
    public void setUp() {
        List<Product> allProduct = productDao.getAllProducts();
        allProduct.stream().forEach(product -> productDao.deleteProduct(product.getId()));

        expectedProduct = new Product(
                "OnePlus5",
                "Unlocked Phone",
                new BigDecimal (525.00) ,
                new BigDecimal(200)
        );
    }

    @Test
    public void createProduct(){
        //Start of creating a product...
        Product actualProduct = productDao.postAProduct(expectedProduct);

        int id = actualProduct.getId();
        expectedProduct.setId(id);

        Assert.assertEquals(expectedProduct, actualProduct);

        // Start of getting a product...
        Product productFound = productDao.getProductById(id);
        Assert.assertEquals(expectedProduct, productFound);

        // Start of deleting a product...
        String successful = "Deletion successful. Product with product_id '" + id + "' has been deleted";
        String deletedMessage = productDao.deleteProduct(id);
        Assert.assertEquals(successful, deletedMessage);

        String unsuccessful = "Deletion NOT successful. No product with product_id '" + 0 + "' was found.";
        String notDeletedMessage = productDao.deleteProduct(0);
        Assert.assertEquals(unsuccessful, notDeletedMessage);

    }

    @Test
    public void updateProduct() {
        Product product = productDao.postAProduct(expectedProduct);

        expectedProduct.setId(product.getId());
        expectedProduct.setListPrice(new BigDecimal(288.88));
        expectedProduct.setUnitCost(new BigDecimal(190.00));

        String successful = "Update successful: "+ expectedProduct.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        String updatedMessage = productDao.updateProduct(expectedProduct);
        Assert.assertEquals(successful, updatedMessage);

        expectedProduct.setId(0);
        expectedProduct.setDescription("One plus is a phone");

        String noUpdateMessage = productDao.updateProduct(expectedProduct);
        Assert.assertEquals(unsuccessful, noUpdateMessage);
    }

    @Test
    public void getAllProducts() {
        Product product2 = new Product(
                "OnePlus6",
                "Unlocked Phone",
                new BigDecimal (625.00) ,
                new BigDecimal(300)
        );

        productDao.postAProduct(expectedProduct);
        productDao.postAProduct(product2);

        List<Product> products = productDao.getAllProducts();

        Assert.assertEquals(2, products.size());

    }
}
