/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author kien3
 */
public class UnitConversion {

    private Category category;

    private Material material;
    private String baseunit;
    private String convertedunit;
    private String conversionfactor;
    private String note;
    private String categorypar;
//     private int conversionid;
//    private int materialid;

    public UnitConversion() {
    }

    public UnitConversion(Category category, Material material, String baseunit, String convertedunit, String conversionfactor, String note,String categorypar) {
        this.category = category;

        this.material = material;
        this.baseunit = baseunit;
        this.convertedunit = convertedunit;
        this.conversionfactor = conversionfactor;
        this.note = note;
        this.categorypar = categorypar;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getBaseunit() {
        return baseunit;
    }

    public void setBaseunit(String baseunit) {
        this.baseunit = baseunit;
    }

    public String getConvertedunit() {
        return convertedunit;
    }

    public void setConvertedunit(String convertedunit) {
        this.convertedunit = convertedunit;
    }

    public String getConversionfactor() {
        return conversionfactor;
    }

    public void setConversionfactor(String conversionfactor) {
        this.conversionfactor = conversionfactor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategorypar() {
        return categorypar;
    }

    public void setCategorypar(String categorypar) {
        this.categorypar = categorypar;
    }

    @Override
    public String toString() {
        return "unitConversion{" + "category=" + category + ", material=" + material + ", baseunit=" + baseunit + ", convertedunit=" + convertedunit + ", conversionfactor=" + conversionfactor + ", note=" + note + ", categorypar=" + categorypar + '}';
    }

   

}
