package tn.iac.mobiledevelopment.mekelti.Model;

/**
 * Created by S4M37 on 02/05/2016.
 */
public class RecetteProposed {
    int valid;
    int id_Proposed;
    Recette recette;

    public int getValid() {
        return valid;
    }

    public int getId_Proposed() {
        return id_Proposed;
    }

    public void setId_Proposed(int id_Proposed) {
        this.id_Proposed = id_Proposed;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public Recette getRecette() {
        return recette;
    }

    public void setRecette(Recette recette) {
        this.recette = recette;
    }
}
