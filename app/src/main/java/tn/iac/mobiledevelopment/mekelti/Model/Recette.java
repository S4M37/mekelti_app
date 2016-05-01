package tn.iac.mobiledevelopment.mekelti.Model;

/**
 * Created by S4M37 on 01/05/2016.
 */
public class Recette {
    int id_Recette;
    String label;
    String description;
    String type;
    String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId_Recette() {
        return id_Recette;
    }

    public void setId_Recette(int id_Recette) {
        this.id_Recette = id_Recette;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
