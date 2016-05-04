package tn.iac.mobiledevelopment.mekelti.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.iac.mobiledevelopment.mekelti.Activity.LoginActivity;
import tn.iac.mobiledevelopment.mekelti.Activity.MainActivity;
import tn.iac.mobiledevelopment.mekelti.Adapter.SearchRecyclerAdapter;
import tn.iac.mobiledevelopment.mekelti.Model.NewsFeed;
import tn.iac.mobiledevelopment.mekelti.Model.User;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Service.CompteManager;
import tn.iac.mobiledevelopment.mekelti.Service.RetrofitServices;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;

/**
 * Created by S4M37 on 02/05/2016.
 */
public class SearchFragment extends Fragment {
    private static final String TAG_ACTION = "action";
    private View rootView;
    private RecyclerView listSearch;
    private ProgressBar progressBar;
    private RecyclerView.LayoutManager layoutManager;
    private Button proposeButton;
    private EditText inputSearch;
    private Call<ResponseBody> call;
    private RetrofitServices retrofitServices = Utils.getRetrofitServices();
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private User user;
    private int userId;

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        user = CompteManager.getCurrentUser(getContext());
        if (user != null) {
            userId = user.getId_user();
        } else {
            userId = 0;
        }
        insializeView();
        insializeRecylcerView();
        return rootView;

    }

    private void insializeRecylcerView() {
        listSearch = (RecyclerView) rootView.findViewById(R.id.list_recette);
        layoutManager = new LinearLayoutManager(getContext());
        listSearch.setLayoutManager(layoutManager);
        listSearch.setHasFixedSize(false);
        searchRecyclerAdapter = new SearchRecyclerAdapter(getContext(), user);
        listSearch.setAdapter(searchRecyclerAdapter);
    }

    private void insializeView() {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        proposeButton = (Button) rootView.findViewById(R.id.propose_button);
        proposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    ((MainActivity) getActivity()).showFragment(new FragmentAddRecette());
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getString(R.string.registration_request));
                    builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((LoginActivity) getActivity()).showFragment(new LoginFragment());
                            dialog.dismiss();
                        }
                    }).setNegativeButton("non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        });

        inputSearch = (EditText) rootView.findViewById(R.id.input_search);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 0) {
                    if (str.charAt(s.length() - 1) == ' ') {
                        str = str.substring(0, str.length()-1);
                    }
                }
                progressBar.setVisibility(View.VISIBLE);
                JsonObject postParams = new JsonObject();
                postParams.addProperty("label", str);
                postParams.addProperty("id_User", String.valueOf(userId));
                call = retrofitServices.searchByALl(postParams);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("responseCode", response.code() + "");
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            JSONArray jsonArray = jsonObject.getJSONArray(Utils.TAG_ROOTE_RESPONSE);
                            Gson gson = new Gson();
                            ArrayList<NewsFeed> list = new ArrayList();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getString(i), NewsFeed.class));
                            }
                            searchRecyclerAdapter.swap(list);
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        inputSearch.setText("");

    }
}
