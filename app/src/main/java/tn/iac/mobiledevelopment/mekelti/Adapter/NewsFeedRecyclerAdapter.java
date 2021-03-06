package tn.iac.mobiledevelopment.mekelti.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.iac.mobiledevelopment.mekelti.Model.NewsFeed;
import tn.iac.mobiledevelopment.mekelti.Model.UserFavoris;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;
import tn.iac.mobiledevelopment.mekelti.Widget.CircleImageView;

/**
 * Created by S4M37 on 01/05/2016.
 */
public class NewsFeedRecyclerAdapter extends RecyclerView.Adapter<NewsFeedRecyclerAdapter.ViewHolder> {

    private final int userId;
    private List<NewsFeed> list;
    private Context context;

    public NewsFeedRecyclerAdapter(Context context, List<NewsFeed> list, int userId) {
        this.list = list;
        this.context = context;
        this.userId = userId;
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
        if (!newsFeed.getRecette().getImg().equals("")) {
            holder.img.setImageUrl(newsFeed.getRecette().getImg());
        } else {
            holder.img.setImageResource(R.drawable.ic_launcher);
        }
        if (holder.img.getDrawable() == null) {
            holder.img.setImageResource(R.drawable.ic_launcher);
        }
        holder.title.setText(newsFeed.getRecette().getLabel());
        if (newsFeed.getFavoris() == 1) {
            holder.favoris.setImageResource(R.drawable.favoris_full);
        } else {
            holder.favoris.setImageResource(R.drawable.favoris_empty);
        }
        holder.favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getFavoris() == 1) {
                    holder.favoris.setImageResource(R.drawable.favoris_empty);
                    list.get(position).setFavoris(0);
                    removeFromFavoris(newsFeed.getFavorisId());
                } else {
                    holder.favoris.setImageResource(R.drawable.favoris_full);
                    list.get(position).setFavoris(1);
                    addToFavoris(newsFeed.getRecette().getId_Recette(), position);
                }
            }
        });
        if (!newsFeed.getRecette().getLink().equals("")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsFeed.getRecette().getLink()));
                    context.startActivity(browserIntent);
                }
            });
        } else {
            holder.itemView.setOnClickListener(null);
        }
    }

    private void addToFavoris(int recetteId, final int position) {
        Call<ResponseBody> call = Utils.getRetrofitServices().addFavorisToUser(Utils.token,userId, recetteId);
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
        Log.d("data", userId + " " + favorisId);
        Call<ResponseBody> call = Utils.getRetrofitServices().deleteUserFavoris(Utils.token,userId, favorisId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("responseCode", response.code() + "");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        private CircleImageView img;
        private TextView descripton;
        private TextView category;
        private TextView title;
        private ImageView favoris;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipper);
            img = (CircleImageView) itemView.findViewById(R.id.recette_image);
            descripton = (TextView) itemView.findViewById(R.id.recette_description);
            category = (TextView) itemView.findViewById(R.id.recette_category);
            title = (TextView) itemView.findViewById(R.id.recette_title);
            favoris = (ImageView) itemView.findViewById(R.id.recette_favoris);
        }
    }
}
