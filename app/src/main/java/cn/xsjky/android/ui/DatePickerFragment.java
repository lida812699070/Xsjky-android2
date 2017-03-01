package cn.xsjky.android.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by OK on 2016/4/29.
 */
public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    private DateCallBack dateCallBack;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (dateCallBack!=null){
            dateCallBack.getDate(year+"-"+(month+1)+"-"+day);
        }
    }

    public void setDateCallBack(DateCallBack dateCallBack) {
        this.dateCallBack = dateCallBack;
    }

    interface DateCallBack {
        void getDate(String date);
    }
}