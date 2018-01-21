package com.tarasbarabash.newsreader.Adapter;

import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tarasbarabash.newsreader.Model.ArticleResponse;
import com.tarasbarabash.newsreader.Model.ArticleWrapper;
import com.tarasbarabash.newsreader.databinding.ArticleItemBinding;

import java.util.Date;

/**
 * Created by Taras
 * 21.01.2018, 19:40.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private ArticleWrapper mCursor;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(View view, String url);
    }

    public ArticleAdapter(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ArticleItemBinding binding = ArticleItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ArticleResponse.Article article = mCursor.getArticle(position);
        holder.bind(article);
        holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onClick(view, article.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ArticleItemBinding mBinding;

        public ViewHolder(ArticleItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ArticleResponse.Article article) {
            mBinding.setArticle(article);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == null) return null;
        Cursor oldCursor = mCursor;
        mCursor = new ArticleWrapper(newCursor);
        notifyDataSetChanged();
        return oldCursor;
    }

    @BindingAdapter("articleImage")
    public static void loadImage(ImageView imageView, String url) {
        if (url == null) return;
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(android.R.color.white)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .into(imageView);
    }

    @BindingAdapter("relativeDate")
    public static void getDate(TextView textView, Date date) {
        long time = date.getTime();
        textView.setText(DateUtils.getRelativeTimeSpanString(time));
    }
}
