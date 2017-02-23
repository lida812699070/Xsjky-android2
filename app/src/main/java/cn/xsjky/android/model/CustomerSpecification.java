package cn.xsjky.android.model;

/**
 * Created by ${lida} on 2017/2/15.
 */
public class CustomerSpecification {
    private String ProductId;
    private String ProductDescription;
    private double Weight;
    private double Length;
    private double Width;
    private double Height;

    @Override
    public String toString() {
        return "CustomerSpecification{" +
                "ProductId='" + ProductId + '\'' +
                ", ProductDescription='" + ProductDescription + '\'' +
                ", Weight='" + Weight + '\'' +
                ", Length='" + Length + '\'' +
                ", Width='" + Width + '\'' +
                ", Height='" + Height + '\'' +
                '}';
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public double getLength() {
        return Length;
    }

    public void setLength(double length) {
        Length = length;
    }

    public double getWidth() {
        return Width;
    }

    public void setWidth(double width) {
        Width = width;
    }
}
