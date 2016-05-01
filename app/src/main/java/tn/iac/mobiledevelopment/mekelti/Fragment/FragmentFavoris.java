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
import android.widget.Button;

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
import tn.iac.mobiledevelopment.mekelti.Adapter.FavorisRecyclerAdapter;
import tn.iac.mobiledevelopment.mekelti.Model.User;
import tn.iac.mobiledevelopment.mekelti.Model.UserFavoris;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Service.Connectivity;
import tn.iac.mobiledevelopment.mekelti.Service.DialogFactory;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;

/**
 * Created by S4M37 on 01/05/2016.
 */
public class FragmentFavoris extends Fragment {
    View rootView;
    RecyclerView listFavoris;
    ArrayList<UserFavoris> listFavorisItem;
    Button proposeButton;
    User user;
    ProgressDialog progressDialog;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_favoris, container, false);
        user = ((MainActivity) getActivity()).getUser();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        insializeView();
        insializeRecylcerView();
        getFavoris();
        return rootView;
    }

    private void getFavoris() {
        if (!Connectivity.isOnline(getContext())) {

        } else {
            progressDialog.show();
            Call<ResponseBody> call = Utils.getRetrofitServices().getUserFavoris(user.getId_user());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray(Utils.TAG_ROOTE_RESPONSE);
                        listFavorisItem = new ArrayList();
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            listFavorisItem.add(gson.fromJson(String.valueOf(jsonArray.get(i)), UserFavoris.class));
                        }
                        FavorisRecyclerAdapter favorisRecyclerAdapter = new FavorisRecyclerAdapter(getContext(), listFavorisItem, user.getId_user());
                        listFavoris.setAdapter(favorisRecyclerAdapter);
                    } catch (JSONException | IOException | NullPointerException e) {
                        e.printStackTrace();
                        DialogFactory.showAlertDialog(getContext(), getString(R.string.server_error), getString(R.string.title_server_error));
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    DialogFactory.showAlertDialog(getContext(), getString(R.string.server_error), getString(R.string.title_server_error));
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void insializeRecylcerView() {
        listFavoris = (RecyclerView) rootView.findViewById(R.id.list_favoris);
        layoutManager = new LinearLayoutManager(getContext());
        listFavoris.setLayoutManager(layoutManager);
        listFavoris.setHasFixedSize(false);
    }

    private void insializeView() {
        proposeButton = (Button) rootView.findViewById(R.id.propose_button);
        proposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showFragment(new FragmentAddRecette());
            }
        });
    }

    public static FragmentFavoris newInstance() {

        Bundle args = new Bundle();

        FragmentFavoris fragment = new FragmentFavoris();
        fragment.setArguments(args);
        return fragment;
    }
}
