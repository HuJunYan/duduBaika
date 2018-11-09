package com.dudubaika.model.bean;

/**
 * 简单的产品bean
 */
public class SimpleProductBean {

    private String productId;
    private String productName;
    private String productLogo;

    public SimpleProductBean(String productId, String productName, String productLogo) {
        this.productId = productId;
        this.productName = productName;
        this.productLogo = productLogo;
    }

    public SimpleProductBean() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLogo() {
        return productLogo;
    }

    public void setProductLogo(String productLogo) {
        this.productLogo = productLogo;
    }
}
