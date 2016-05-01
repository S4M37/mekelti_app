package tn.iac.mobiledevelopment.mekelti.Model;

/**
 * Created by S4M37 on 01/05/2016.
 */
public class NewsFeed {
    Recette recette;
    int favoris;
    int favorisId;

    public int getFavorisId() {
        return favorisId;
    }

    public void setFavorisId(int favorisId) {
        this.favorisId = favorisId;
    }

    public Recette getRecette() {
        return recette;
    }

    public void setRecette(Recette recette) {
        this.recette = recette;
    }

    public int getFavoris() {
        return favoris;
    }

    public void setFavoris(int favoris) {
        this.favoris = favoris;
    }
}
