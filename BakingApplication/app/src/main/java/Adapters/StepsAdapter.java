package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmed.bakingapplication.R;
import com.bumptech.glide.Glide;
import com.github.vipulasri.timelineview.TimelineView;


import java.util.List;

import Model.Steps;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

        private static final String TAG = StepsAdapter.class.getSimpleName();
        private final Context context;
        private final List<Steps> stepsList;
        private final onStepClick onStepClickListener;

        public StepsAdapter(Context mContext, List<Steps> mStepsList, onStepClick listener) {
            this.context = mContext;
            this.stepsList = mStepsList;
            this.onStepClickListener = listener;
        }

        @NonNull
        @Override
        public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.steps_list_item, viewGroup, false);
            final StepsViewHolder stepsViewHolder = new StepsViewHolder(view,viewType);

            stepsViewHolder.stepsCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStepClickListener.onStepClickListener(stepsList.get(stepsViewHolder.getAdapterPosition()));
                }
            });
            return stepsViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
            String url = stepsList.get(position).getVideoURL();

            if (url.equals("")) {
                holder.steps_image.setImageResource(R.drawable.ic_clipboard_text);


            } else {
                holder.steps_image.setImageResource(R.drawable.ic_video);
            }

            String thumbnailUrl = stepsList.get(position).getThumbnailURL();
            if (thumbnailUrl.equals("")) {
                holder.steps_image.setVisibility(View.GONE);
            } else {
                Glide.with(context)
                        .asBitmap()
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(holder.steps_image);
            }
            String shortDescriptionText = stepsList.get(position).getShortDescription();
            String longDescriptionText = stepsList.get(position).getDescription();
            holder.shortDescription.setText(shortDescriptionText);
            holder.description.setText(longDescriptionText);

        }

        @Override
        public int getItemCount() {
              if(stepsList==null){
                  return 0;
              }else{
                return stepsList.size();}

        }

        public interface onStepClick {
            void onStepClickListener(Steps step);
        }

        public class StepsViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.steps_image)
            ImageView steps_image;

            @BindView(R.id.shortDescription)
            TextView shortDescription;

            @BindView(R.id.stepsCardView)
            CardView stepsCardView;

            @BindView(R.id.stepDescription)
            TextView description;

            @BindView(R.id.time_marker)
            TimelineView timelineView;

            public StepsViewHolder(View itemView, int viewItem) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                timelineView.initLine(viewItem);
            }
        }
        }



