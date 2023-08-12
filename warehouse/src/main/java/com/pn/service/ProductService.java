package com.pn.service;

import com.pn.entity.Product;
import com.pn.entity.Result;
import com.pn.page.Page;

public interface ProductService {

    //分页查询商品的业务方法
    public Page queryProductPage(Page page, Product product);

    //添加商品的业务方法
    public Result saveProduct(Product product);

    //修改商品上下架状态的业务方法
    public Result updateProductState(Product product);

    //删除商品的业务方法
    public Result deleteProduct(Integer productId);

    //修改商品的业务方法
    public Result updateProduct(Product product);
}
