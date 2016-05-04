package tn.iac.mobiledevelopment.mekelti.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.iac.mobiledevelopment.mekelti.Model.RecetteProposed;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;
import tn.iac.mobiledevelopment.mekelti.Widget.CircleImageView;

/**
 * Created by S4M37 on 02/05/2016.
 */
public class ProposedRecyclerViewAdapter extends RecyclerSwipeAdapter<ProposedRecyclerViewAdapter.ViewHolder> {

    private List<RecetteProposed> list;
    private Context context;
    private int userId;

    public ProposedRecyclerViewAdapter(Context context, List<RecetteProposed> list, int userId) {
        this.list = list;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.proposed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RecetteProposed recetteProposed = list.get(position);
        holder.title.setText(recetteProposed.getRecette().getLabel());
        holder.descripton.setText(recetteProposed.getRecette().getDescription());
        holder.category.setText(recetteProposed.getRecette().getType());
        if (!recetteProposed.getRecette().getImg().equals("")) {
            holder.img.setImageUrl(recetteProposed.getRecette().getImg());
        } else {
            holder.img.setImageResource(R.drawable.ic_launcher);
        }
        if (holder.img.getDrawable() == null) {
            holder.img.setImageResource(R.drawable.ic_launcher);
        }
        switch (recetteProposed.getValid()) {
            case 0:
                holder.imageValid.setImageResource(R.drawable.invalid);
                break;
            case 1:
                holder.imageValid.setImageResource(R.drawable.valid);
                break;
            case 2:
                holder.imageValid.setImageResource(R.drawable.refuser);
                break;
        }
        holder.title.setText(recetteProposed.getRecette().getLabel());

        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.itemView.findViewById(R.id.wrapper));
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                deleteProposed(recetteProposed.getId_Proposed());
                mItemManger.removeShownLayouts(holder.swipeLayout);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                mItemManger.closeAllItems();
                mItemManger.bind(holder.itemView, position);
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
    }

    private void deleteProposed(int proposedId) {
        Call<ResponseBody> call = Utils.getRetrofitServices().deleteProposed(Utils.token, userId, proposedId);
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

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeLayout swipeLayout;
        private CircleImageView img;
        private TextView descripton;
        private TextView category;
        private TextView title;
        private ImageView imageValid;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.recette_image);
            descripton = (TextView) itemView.findViewById(R.id.recette_description);
            category = (TextView) itemView.findViewById(R.id.recette_category);
            title = (TextView) itemView.findViewById(R.id.recette_title);
            imageValid = (ImageView) itemView.findViewById(R.id.recette_valid);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipper);

        }
    }

    private void removeFromFavoris(int favorisId) {
        Call<ResponseBody> call = Utils.getRetrofitServices().deleteUserFavoris(Utils.token, userId, favorisId);
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
}
