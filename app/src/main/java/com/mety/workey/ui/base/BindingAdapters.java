package com.mety.workey.ui.base;

import android.widget.ImageView;
import android.widget.TextView;

import com.mety.workey.R;
import com.mety.workey.data.converters.Converters;

import java.util.Date;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("setDeadlineTextAndColor")
    public static void setDeadlineTextAndColor(TextView textView, Date date) {
        if (date == null) {
            textView.setText(R.string.select);
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.textColor));
        } else {
            textView.setText(Converters.dateToString(date, Converters.DAY_MONTH_YEAR_HOUR_MINUTE));
            Date currentDate = new Date();
            if (currentDate.getTime() >= date.getTime()) {
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.design_default_color_error));
            } else {
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.secondaryColor));
            }
        }
    }

    @BindingAdapter("setDeadlineColor")
    public static void setDeadlineColor(ImageView imageView, Date date) {
        if (date != null) {
            Date currentDate = new Date();
            if (currentDate.getTime() >= date.getTime()) {
                imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.design_default_color_error));
            } else {
                imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.secondaryColor));
            }
        } else {
            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.textColor));
        }
    }

}
