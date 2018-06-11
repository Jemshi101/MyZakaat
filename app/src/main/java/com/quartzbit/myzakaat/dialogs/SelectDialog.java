package com.quartzbit.myzakaat.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.config.Config;
import com.quartzbit.myzakaat.config.TypefaceCache;
import com.quartzbit.myzakaat.model.BasicDataBean;
import com.quartzbit.myzakaat.util.AppConstants;
import com.quartzbit.myzakaat.util.FileOp;

/**
 * Created by Jemsheer K D on 05 December, 2016.
 * Package com.quartzbit.myzakaat.dialogs
 * Project Dearest
 */

public class SelectDialog {

    private final Activity mContext;
    private String subID;
    private String ID;
    private Typeface typeface;
    private Vibrator mVibrator;
    private String action;
    private SelectDialogActionListener selectDialogActionListener;
    private Dialog dialogSelect;
    private ListView lvSelect;
    private String[] choiceList;
    private boolean isRelationship;
    private SelectAdapter adapter;
    private TextView txtTitle;
    private BasicDataBean basicDataBean;

    public SelectDialog(Activity mContext, String action) {
        this.mContext = mContext;
        this.action = action;

        init(mContext);
    }

    public SelectDialog(Activity mContext, String action, String ID) {
        this.mContext = mContext;
        this.action = action;
        this.ID = ID;

        init(mContext);
    }

    public SelectDialog(Activity mContext, String action, String ID, String subID) {
        this.mContext = mContext;
        this.action = action;
        this.ID = ID;
        this.subID = subID;

        init(mContext);
    }

    private void init(Context mContext) {
        try {
            typeface = TypefaceCache.getInstance().getTypeface(mContext.getApplicationContext(), "Roboto-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        if (Config.getInstance().getBasicDataBean() != null) {
            basicDataBean = Config.getInstance().getBasicDataBean();
        } else {
            basicDataBean = new FileOp(mContext.getApplicationContext()).readBasicDataBean();
        }
        if(basicDataBean==null){
            return;
        }

        setSelectDialog();
    }


    public void setTitle(String title) {
        dialogSelect.setTitle(title);
        txtTitle.setText(title);
    }

    public void show() {
        populate();
        dialogSelect.show();
    }

    private void setSelectDialog() {

        dialogSelect = new Dialog(mContext, R.style.ThemeDialogCustom_NoBackground);
        dialogSelect.setContentView(R.layout.dialog_select);
        dialogSelect.setTitle(R.string.title_select);

        lvSelect = (ListView) dialogSelect.findViewById(R.id.lv_dialog_select);
        txtTitle = (TextView) dialogSelect.findViewById(R.id.txt_dialog_select_title);

    }

    private void populate() {

        if (AppConstants.ACTION_CHOOSE_PREFIX.equals(action)) {
            choiceList = mContext.getResources().getStringArray(R.array.prefix);
            System.out.println(Arrays.toString(choiceList));
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } else if (AppConstants.ACTION_CHOOSE_SUFFIX.equals(action)) {
            choiceList = mContext.getResources().getStringArray(R.array.suffix);
            System.out.println(Arrays.toString(choiceList));
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        }else if (AppConstants.ACTION_CHOOSE_CAUSE_OF_DEATH.equals(action)) {
            choiceList = mContext.getResources().getStringArray(R.array.cause_of_death);
            System.out.println(Arrays.toString(choiceList));
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } else if (AppConstants.ACTION_CHOOSE_AGE.equals(action)) {
            choiceList = new String[75 - 18];
            int num = 75 - 18;
            for (int i = 0; i < num; i++) {
                choiceList[i] = "" + (i + 18);
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        }else if (AppConstants.ACTION_CHOOSE_RELIGION.equals(action)) {
            choiceList = new String[basicDataBean.getReligions().size()];
            for (int i = 0; i < basicDataBean.getReligions().size(); i++) {
                choiceList[i] = basicDataBean.getReligions().get(i).getName();
            }
            System.out.println(Arrays.toString(choiceList));
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        }/* else if (AppConstants.ACTION_CHOOSE_DAY.equals(action)) {
            choiceList = mContext.getResources().getStringArray(R.array.day);
            System.out.println(Arrays.toString(choiceList));
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } else if (AppConstants.ACTION_CHOOSE_MONTH.equals(action)) {
            choiceList = mContext.getResources().getStringArray(R.array.month);
            System.out.println(Arrays.toString(choiceList));
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } else if (AppConstants.ACTION_CHOOSE_YEAR.equals(action)) {
            choiceList = new String[Calendar.getInstance().get(Calendar.YEAR) - AppConstants.YEAR_START - 18 + 1];
            int num = Calendar.getInstance().get(Calendar.YEAR) - AppConstants.YEAR_START - 18;

            for (int i = 0, j = num; i <= num; i++, j--) {
                choiceList[j] = "" + (i + AppConstants.YEAR_START);
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        }*//*else if (AppConstants.ACTION_CHOOSE_QUANTITY.equals(action)) {
            UserInterestBean userInterestBean = (UserInterestBean) getIntent().getSerializableExtra("category");
            interestList = userInterestBean.getInterest();
            choiceList = new String[interestList.size()];
            for (int i = 0; i < interestList.size(); i++) {
                choiceList[i] = interestList.get(i).getHeight();
            }
            lvSelect.setAdapter(new SelectAdapter());
        }*/ else if (AppConstants.ACTION_CHOOSE_COUNTRY.equals(action)) {
            choiceList = new String[basicDataBean.getCountries().size()];
            for (int i = 0; i < basicDataBean.getCountries().size(); i++) {
                choiceList[i] = basicDataBean.getCountries().get(i).getName();
            }
            System.out.println(Arrays.toString(choiceList));
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } /*else if (AppConstants.ACTION_CHOOSE_STATE.equals(action)) {
            if (basicDataBean.getCountry(ID).getStates() != null) {
                choiceList = new String[basicDataBean.getCountry(ID).getStates().size()];
                for (int i = 0; i < basicDataBean.getCountry(ID).getStates().size(); i++) {
                    choiceList[i] = basicDataBean.getCountry(ID).getStates().get(i).getName();
                }
                System.out.println(Arrays.toString(choiceList));
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } else if (AppConstants.ACTION_CHOOSE_DISTRICT.equals(action)) {
            if (basicDataBean.getState(ID, subID).getDistricts() != null) {
                choiceList = new String[basicDataBean.getState(ID, subID).getDistricts().size()];
                for (int i = 0; i < basicDataBean.getState(ID, subID).getDistricts().size(); i++) {
                    choiceList[i] = basicDataBean.getState(ID, subID).getDistricts().get(i).getName();
                }
                System.out.println(Arrays.toString(choiceList));
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } */ else if (AppConstants.ACTION_CHOOSE_STATE.equals(action)) {
            if (basicDataBean.getStateList(ID) != null) {
                choiceList = new String[basicDataBean.getStateList(ID).size()];
                for (int i = 0; i < basicDataBean.getStateList(ID).size(); i++) {
                    choiceList[i] = basicDataBean.getStateList(ID).get(i).getName();
                }
                System.out.println(Arrays.toString(choiceList));
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } /*else if (AppConstants.ACTION_CHOOSE_DISTRICT.equals(action)) {
            if (basicDataBean.getDistrictList(ID) != null) {
                choiceList = new String[basicDataBean.getDistrictList(ID).size()];
                for (int i = 0; i < basicDataBean.getDistrictList(ID).size(); i++) {
                    choiceList[i] = basicDataBean.getDistrictList(ID).get(i).getName();
                }
                System.out.println(Arrays.toString(choiceList));
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        }*/

    }

    private class SelectAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        @Override
        public int getCount() {
            try {
                if (AppConstants.ACTION_CHOOSE_YEAR.equals(action)) {
                    int num = Calendar.getInstance().get(Calendar.YEAR) - AppConstants.YEAR_START - 18 + 1;
                    return num;
                } else
                    return choiceList.length;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        public Object getItem(int arg0) {

            return null;
        }

        @Override
        public long getItemId(int arg0) {

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            if (inflater == null)
                inflater = mContext.getLayoutInflater();
            View lytItem;

            ViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {
                // inflate the layout
                lytItem = inflater.inflate(R.layout.item_select, null);
                holder = new ViewHolder();

                holder.txtName = (TextView) lytItem.findViewById(R.id.txt_item_select_name);

                lytItem.setTag(/*R.id.TAG_POST,*/ holder);
            } else {
                lytItem = convertView;
            }
            holder = (ViewHolder) lytItem.getTag(/*R.id.TAG_POST*/);

            if (AppConstants.ACTION_CHOOSE_YEAR.equals(action)) {
                holder.txtName.setText(choiceList[position]);
            } else {
                holder.txtName.setText(choiceList[position]);
            }

            lytItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    mVibrator.vibrate(25);

                    selectDialogActionListener.actionCompletedSuccessfully(position, choiceList[position]);
                    dialogSelect.dismiss();
                }

                /*else{
                    String categoryID = interestList.get(position).getId();
                    Intent intent = new Intent();
                    intent.putExtra("id", categoryID);
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }*/
            });

            return lytItem;
        }
    }

    private static class ViewHolder {
        private TextView txtName;
    }


    public interface SelectDialogActionListener {
        void actionCompletedSuccessfully(int position, String value);

        void actionFailed(String errorMsg);
    }

    public SelectDialogActionListener getSelectDialogActionListener() {
        return selectDialogActionListener;
    }


    public void setSelectDialogActionListener(SelectDialogActionListener selectDialogActionListener) {
        this.selectDialogActionListener = selectDialogActionListener;
    }

}
