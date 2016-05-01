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
import tn.iac.mobiledevelopment.mekelti.Adapter.NewsFeedRecyclerAdapter;
import tn.iac.mobiledevelopment.mekelti.Model.NewsFeed;
import tn.iac.mobiledevelopment.mekelti.Model.User;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Service.Connectivity;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;

/**
 * Created by S4M37 on 17/04/2016.
 */
public class FragmentMain extends Fragment {
    private View rootView;
    RecyclerView listRecette;
    ArrayList<NewsFeed> listRecetteItem;
    Button proposeButton;
    User user;
    ProgressDialog progressDialog;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        insializeView();
        user = ((MainActivity) getActivity()).getUser();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        insializeRecylcerView();
        getRecette();
        return rootView;
    }

    private void getRecette() {
        if (!Connectivity.isOnline(getContext())) {

        } else {
            progressDialog.show();
            Call<ResponseBody> call = Utils.getRetrofitServices().getNewsFeed(user.getId_user());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray(Utils.TAG_ROOTE_RESPONSE);
                        listRecetteItem = new ArrayList();
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            listRecetteItem.add(gson.fromJson(String.valueOf(jsonArray.get(i)), NewsFeed.class));
                        }
                        NewsFeedRecyclerAdapter newsFeedRecyclerAdapter = new NewsFeedRecyclerAdapter(getContext(), listRecetteItem, user.getId_user());
                        listRecette.setAdapter(newsFeedRecyclerAdapter);
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

    private void insializeRecylcerView() {
        listRecette = (RecyclerView) rootView.findViewById(R.id.list_recette);
        layoutManager = new LinearLayoutManager(getContext());
        listRecette.setLayoutManager(layoutManager);
        listRecette.setHasFixedSize(false);
    }

    private void insializeView() {
        proposeButton = (Button) rootView.findViewById(R.id.propose_button);
        proposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showFragment(new FragmentAddRecette());
            }
        });
    }
}
