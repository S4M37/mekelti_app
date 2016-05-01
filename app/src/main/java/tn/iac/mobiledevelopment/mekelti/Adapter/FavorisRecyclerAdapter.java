package tn.iac.mobiledevelopment.mekelti.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.iac.mobiledevelopment.mekelti.Model.UserFavoris;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;
import tn.iac.mobiledevelopment.mekelti.Widget.CircleImageView;

/**
 * Created by S4M37 on 01/05/2016.
 */
public class FavorisRecyclerAdapter extends RecyclerSwipeAdapter<FavorisRecyclerAdapter.ViewHolder> {

    private List<UserFavoris> list;
    private Context context;
    private int userId;

    public FavorisRecyclerAdapter(Context context, List<UserFavoris> list, int userId) {
        this.list = list;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favoris_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserFavoris userFavoris = list.get(position);
        holder.title.setText(userFavoris.getLabel());
        holder.descripton.setText(userFavoris.getDescription());
        holder.category.setText(userFavoris.getType());
        if (!userFavoris.getImg().equals("")) {
            holder.img.setImageUrl(userFavoris.getImg());
        } else {
            holder.img.setImageResource(R.drawable.ic_launcher);

        }
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.itemView.findViewById(R.id.wrapper));
        holder.title.setText(userFavoris.getLabel());
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                removeFromFavoris(userFavoris.getId_Favoris());
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        private CircleImageView img;
        private TextView descripton;
        private TextView category;
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipper);
            img = (CircleImageView) itemView.findViewById(R.id.recette_image);
            descripton = (TextView) itemView.findViewById(R.id.recette_description);
            category = (TextView) itemView.findViewById(R.id.recette_category);
            title = (TextView) itemView.findViewById(R.id.recette_title);
        }
    }

    private void removeFromFavoris(int favorisId) {
        Call<ResponseBody> call = Utils.getRetrofitServices().deleteUserFavoris(userId, favorisId);
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
