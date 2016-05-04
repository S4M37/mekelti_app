package tn.iac.mobiledevelopment.mekelti.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.iac.mobiledevelopment.mekelti.Activity.MainActivity;
import tn.iac.mobiledevelopment.mekelti.Adapter.ProposedRecyclerViewAdapter;
import tn.iac.mobiledevelopment.mekelti.Model.RecetteProposed;
import tn.iac.mobiledevelopment.mekelti.Model.User;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Service.Connectivity;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;

/**
 * Created by S4M37 on 02/05/2016.
 */
public class ProposedFragment extends Fragment {
    private View rootView;
    private User user;
    private ArrayList<RecetteProposed> listProposedRecetteItems;
    private RecyclerView listProposedRecette;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recettes, container, false);
        user = ((MainActivity) getActivity()).getUser();
        inisializeView();
        inisializeRecyclerView();
        getProposedRecette();
        return rootView;
    }

    private void inisializeRecyclerView() {
        listProposedRecette = (RecyclerView) rootView.findViewById(R.id.list_recette);
        layoutManager = new LinearLayoutManager(getContext());
        listProposedRecette.setLayoutManager(layoutManager);
        listProposedRecette.setHasFixedSize(false);
    }

    private void inisializeView() {
        rootView.findViewById(R.id.propose_button).setVisibility(View.GONE);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
    }

    private void getProposedRecette() {
        if (!Connectivity.isOnline(getContext())) {

        } else {
            progressDialog.show();
            Call<ResponseBody> call = Utils.getRetrofitServices().getProposed(Utils.token,user.getId_user());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray(Utils.TAG_ROOTE_RESPONSE);
                        listProposedRecetteItems = new ArrayList();
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            listProposedRecetteItems.add(gson.fromJson(String.valueOf(jsonArray.get(i)), RecetteProposed.class));
                        }
                        ProposedRecyclerViewAdapter proposedRecyclerViewAdapter = new ProposedRecyclerViewAdapter(getContext(), listProposedRecetteItems, user.getId_user());
                        listProposedRecette.setAdapter(proposedRecyclerViewAdapter);
                    } catch (JSONException | IOException | NullPointerException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }
    }
}
