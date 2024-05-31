package org.example.projectmd3_smartphone_ecommerce.service.impl;

import org.example.projectmd3_smartphone_ecommerce.dao.impl.CategoryDaoImpl;
import org.example.projectmd3_smartphone_ecommerce.dao.impl.ProductDaoImpl;
import org.example.projectmd3_smartphone_ecommerce.dto.request.ProductRequest;
import org.example.projectmd3_smartphone_ecommerce.entity.Categories;
import org.example.projectmd3_smartphone_ecommerce.entity.Products;
import org.example.projectmd3_smartphone_ecommerce.service.IProductService;
import org.example.projectmd3_smartphone_ecommerce.service.UploadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Repository
public class ProductServiceImpl implements IProductService {
    @Autowired
    CategoryDaoImpl categoryDao;
    @Autowired
    ProductDaoImpl productDao;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<Products> selectAllProducts(int currentPage, int size) {
        return productDao.getAll(currentPage, size);
    }

    public List<Products> FilterByCat(int currentPage, int size, Integer catID) {
        return productDao.FilterByCategory(currentPage, size, catID);
    }

    @Override
    public Products selectProductById(int id) {
        return productDao.findById(id);
    }

    @Autowired
   private UploadService uploadService;

    @Override
    public Boolean insertProduct(ProductRequest product) {
        Products products = mapper.map(product, Products.class);
        MultipartFile imgFile = product.getImage();
        if (imgFile.getSize()>0) {
            String firebaseUrl = uploadService.uploadFileToServer(imgFile);


            // Step 3: Set the product's image URL to the Firebase URL
            products.setImage(firebaseUrl);
            products.setCategories(categoryDao.findById(product.getCategories()));
            productDao.addNew2(products);
            return true;
        }
        return false;
    }
    public Boolean insertProducts1(ProductRequest product) {
        if (categoryDao.getAll(0, 100).isEmpty()) {
            return false;
        }
        Products products = mapper.map(product, Products.class);
        MultipartFile imgFile = product.getImage();
        if (imgFile != null && !imgFile.isEmpty()) {

                // Step 2: Upload the file from the server to Firebase and get the URL
                String firebaseUrl = uploadService.uploadFileToServer(imgFile);

                // Step 3: Set the product's image URL to the Firebase URL
                products.setImage(firebaseUrl);
                products.setCategories(categoryDao.findById(product.getCategories()));
                productDao.addNew2(products);
                return true;
        }
        return false;
    }



    @Override
    public Boolean updateProduct(ProductRequest product) {
        Products products = mapper.map(product, Products.class);
        MultipartFile imgFile = product.getImage();
        if (imgFile != null && !imgFile.isEmpty()) {
            try {

                String firebaseUrl =uploadService.uploadFileToServer(imgFile);

                // Step 3: Set the product's image URL to the Firebase URL
                products.setImage(firebaseUrl);
                products.setCategories(categoryDao.findById(product.getCategories()));
                productDao.update2(products);
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            products.setImage(productDao.findById(products.getId()).getImage());
            products.setCategories(categoryDao.findById(product.getCategories()));
            productDao.update2(products);
            return true;
        }
    }


    @Override
    public boolean deleteProduct(int id) {
        if (productDao.delete(id) == true) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<Products> searchProduct(String product) {
        List<Products> productss = new ArrayList<>();
        for (Products products : productDao.searchProduct(product)) {
            productss.add(products);
        }
        return productss;
    }

    @Override
    public Long countAllProduct() {
        return productDao.countAllProduct();
    }

    public List<Products> soft(String soft, Integer currentPage, Integer size) {
        return productDao.sorf(soft, currentPage, size);
    }


}
