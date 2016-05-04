package tn.iac.mobiledevelopment.mekelti.Model;

/**
 * Created by S4M37 on 01/05/2016.
 */
public class UserFavoris {
    int id_Favoris;
    Recette recette;

    public Recette getRecette() {
        return recette;
    }

    public void setRecette(Recette recette) {
        this.recette = recette;
    }

    public int getId_Favoris() {
        return id_Favoris;
    }

    public void setId_Favoris(int id_Favoris) {
        this.id_Favoris = id_Favoris;
    }

}
