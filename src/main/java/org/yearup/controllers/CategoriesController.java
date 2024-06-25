package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController {

    private  CategoryDao categoryDao;
    private  ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @RequestMapping( method = RequestMethod.GET)
    public List<Category> getAll() {
        return categoryDao.getAllCategories();
    }

    @RequestMapping(path = "/categories/{id}", method = RequestMethod.GET)
    public Category getById(@PathVariable int id) {
        return categoryDao.getById(id);
    }

    @RequestMapping(path = "/categories/{categoryId}/products", method = RequestMethod.GET)
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return productDao.listByCategoryId(categoryId);
    }

    @RequestMapping(path = "/categories", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Category addCategory(@RequestBody Category category) {
        return categoryDao.create(category);
    }

    @RequestMapping(path = "/categories/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        categoryDao.update(id, category);
    }

    @RequestMapping(path = "/categories/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable int id) {
        categoryDao.delete(id);
    }
}
