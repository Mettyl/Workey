package com.mety.workey.ui.homeFragment;

import com.mety.workey.BR;
import com.mety.workey.ui.base.ListItem;

import java.util.Date;

import androidx.databinding.Bindable;

public class DateHeader extends ListItem {

    private Date headerName;

    public DateHeader(Date date) {
        this.headerName = date;
    }

    @Bindable
    public Date getHeaderName() {
        return headerName;
    }

    public void setHeaderName(Date headerName) {
        this.headerName = headerName;
        notifyPropertyChanged(BR.headerName);
    }

    @Override
    public boolean isHeader() {
        return true;
    }
}
