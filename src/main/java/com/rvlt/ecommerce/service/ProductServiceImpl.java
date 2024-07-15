package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.product.AddProductRq;
import com.rvlt.ecommerce.dto.product.ProductRf;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.User;
import com.rvlt.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseMessage<List<ProductRf>> getAllProduct() {
        ResponseMessage<List<ProductRf>> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            List<Product> productList = productRepository.findAll();
            List<ProductRf> productRfList = new ArrayList<>();
            for (Product product : productList) {
                ProductRf productRf = mapToProductRf(product);
                productRfList.add(productRf);
            }
            rs.setData(productRfList);
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }

    @Override
    public ResponseMessage<ProductRf> getProductById(Long id) {
        ResponseMessage<ProductRf> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Product> product = productRepository.findById(id);
            if(product.isPresent()){
                ProductRf productRf = mapToProductRf(product.get());
                rs.setData(productRf);
            } else {
                status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
                status.setMessage("Product not found");
            }
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }



//    @Override
//    @Transactional
//    public ResponseMessage<Void> addProduct(RequestMessage<AddProductRq> rq){
//        ResponseMessage<Void> rs = new ResponseMessage<>();
//        Status status = new Status();
//        AddProductRq input = rq.getData();
//        try {
//            // check if exist, bad - should check by name
//            if(productRepository.existsById(input.getId())){
//                status.setHttpStatusCode(String.valueOf(HttpStatus.CONFLICT.value()));
//                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
//                status.setMessage("Product already exists");
//            }
//            else {
//                // create product
//                Product product = new Product();
//                product.setId(input.getId());
//                product.setName(input.getName());
//                product.setInStock(input.getInStock());
//                product.setPrice(input.getPrice());
//
//                productRepository.save(product);
//
//                status.setHttpStatusCode(String.valueOf(HttpStatus.CREATED.value()));
//                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.SUCCESS);
//                status.setMessage("Product created");
//            }
//        } catch (Exception e) {
//            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
//            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
//            status.setMessage(e.getMessage());
//            // Manual rollback
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
//        rs.setStatus(status);
//        return rs;
//    }

    private ProductRf mapToProductRf(Product product) {
        ProductRf productRf = new ProductRf();
        productRf.setId(product.getId());
        productRf.setName(product.getName());
        productRf.setInStock(product.getInStock());
        productRf.setPrice(product.getPrice());
        return productRf;
    }

}
