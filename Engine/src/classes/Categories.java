package classes;

import java.util.ArrayList;
import java.util.List;

public class Categories {
    List<String> categoryList = new ArrayList<String>();

    public Categories(){}

    public Categories(List<String> cat){
        categoryList = cat;
    }

    public List<String> getCategories(){
        return categoryList;
    }
}
