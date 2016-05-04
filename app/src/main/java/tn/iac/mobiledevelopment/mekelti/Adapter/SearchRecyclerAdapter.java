package tn.iac.mobiledevelopment.mekelti.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.iac.mobiledevelopment.mekelti.Activity.LoginActivity;
import tn.iac.mobiledevelopment.mekelti.Fragment.LoginFragment;
import tn.iac.mobiledevelopment.mekelti.Model.NewsFeed;
import tn.iac.mobiledevelopment.mekelti.Model.User;
import tn.iac.mobiledevelopment.mekelti.Model.UserFavoris;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;
import tn.iac.mobiledevelopment.mekelti.Widget.CircleImageView;

/**
 * Created by S4M37 on 02/05/2016.
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private List<NewsFeed> list;
    private Context context;
    private User user;

    public SearchRecyclerAdapter(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recette_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewsFeed newsFeed = list.get(position);
        holder.category.setText(newsFeed.getRecette().getType());
        holder.descripton.setText(newsFeed.getRecette().getDescription());
        holder.title.setText(newsFeed.getRecette().getLabel());
        if (!newsFeed.getRecette().getImg().equals("")) {
            holder.img.setImageUrl(newsFeed.getRecette().getImg());
        } else {
            holder.img.setImageResource(R.drawable.ic_launcher);
        }
        if (holder.img.getDrawable() == null) {
            holder.img.setImageResource(R.drawable.ic_launcher);
        }
        if (newsFeed.getFavoris() == 1) {
            holder.favoris.setImageResource(R.drawable.favoris_full);
        } else {
            holder.favoris.setImageResource(R.drawable.favoris_empty);
        }
        holder.favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (list.get(position).getFavoris() == 1) {
                        holder.favoris.setImageResource(R.drawable.favoris_empty);
                        list.get(position).setFavoris(0);
                        removeFromFavoris(newsFeed.getFavorisId());
                    } else {
                        holder.favoris.setImageResource(R.drawable.favoris_full);
                        list.get(position).setFavoris(1);
                        addToFavoris(newsFeed.getRecette().getId_Recette(), position);
                    }
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.registration_request));
                    builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((LoginActivity) context).showFragment(new LoginFragment());
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
    }

    private void addToFavoris(int recetteId, final int position) {
        Call<ResponseBody> call = Utils.getRetrofitServices().addFavorisToUser(Utils.token,user.getId_user(), recetteId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Gson gson = new Gson();
                    UserFavoris userFavoris = gson.fromJson(jsonObject.getString(Utils.TAG_ROOTE_RESPONSE), UserFavoris.class);
                    list.get(position).setFavorisId(userFavoris.getId_Favoris());
                } catch (JSONException | IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void removeFromFavoris(int favorisId) {
        Call<ResponseBody> call = Utils.getRetrofitServices().deleteUserFavoris(Utils.token,user.getId_user(), favorisId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img;
        private TextView descripton;
        private TextView category;
        private TextView title;
        private ImageView favoris;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.recette_image);
            descripton = (TextView) itemView.findViewById(R.id.recette_description);
            category = (TextView) itemView.findViewById(R.id.recette_category);
            title = (TextView) itemView.findViewById(R.id.recette_title);
            favoris = (ImageView) itemView.findViewById(R.id.recette_favoris);
        }
    }

    public void swap(List<NewsFeed> list) {
        this.list = null;
        this.list = list;
        notifyDataSetChanged();
    }
}

