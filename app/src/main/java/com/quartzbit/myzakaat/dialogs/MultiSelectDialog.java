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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.config.TypefaceCache;
import com.quartzbit.myzakaat.model.SelectItemBean;
import com.quartzbit.myzakaat.model.SelectResultBean;
import com.quartzbit.myzakaat.util.AppConstants;


/**
 * Created by Jemsheer K D on 16 December, 2016.
 * Package com.quartzbit.myzakaat.dialogs
 * Project Wakilishwa
 */

public class MultiSelectDialog {

    private static final String TAG = "MultiDialog";
    private final Activity mContext;
    private List<SelectResultBean> selectedItemList;
    private List<String> subIDs;
    private List<String> IDs;
    private Typeface typeface;
    private Vibrator mVibrator;
    private String action;
    private MultiSelectDialogActionListener multiSelectDialogActionListener;
    private Dialog dialogSelect;
    private ListView lvSelect;
    private List<SelectItemBean> choiceList;
    private SelectAdapter adapter;
    private TextView txtTitle;
    private Button btnSave;
//    private ArrayList<Integer> selectedPositionList;

    public MultiSelectDialog(Activity mContext, String action) {
        this.mContext = mContext;
        this.action = action;

        init(mContext);
    }

    public MultiSelectDialog(Activity mContext, String action, List<String> IDs) {
        this.mContext = mContext;
        this.action = action;
        this.IDs = IDs;

        init(mContext);
    }

    public MultiSelectDialog(Activity mContext, String action, List<String> IDs, List<String> subIDs) {
        this.mContext = mContext;
        this.action = action;
        this.IDs = IDs;
        this.subIDs = subIDs;

        init(mContext);
    }

    public MultiSelectDialog(Activity mContext, List<SelectResultBean> selectedItemList, String action) {
        this.mContext = mContext;
        this.action = action;
        this.selectedItemList = selectedItemList;

        init(mContext);
    }

    public MultiSelectDialog(Activity mContext, List<SelectResultBean> selectedItemList, String action, List<String> IDs) {
        this.mContext = mContext;
        this.selectedItemList = selectedItemList;
        this.action = action;
        this.IDs = IDs;

        init(mContext);
    }

    public MultiSelectDialog(Activity mContext, List<SelectResultBean> selectedItemList,
                             String action, List<String> IDs, List<String> subIDs) {
        this.mContext = mContext;
        this.selectedItemList = selectedItemList;
        this.action = action;
        this.IDs = IDs;
        this.subIDs = subIDs;

        init(mContext);
    }


    private void init(Context mContext) {
        try {
            typeface = TypefaceCache.getInstance().getTypeface(mContext.getApplicationContext(), "Roboto-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

//        selectedPositionList=new ArrayList<Integer>();
        if (selectedItemList == null)
            selectedItemList = new ArrayList<>();

        setMultiSelectDialog();
    }


    public void setTitle(String title) {
        dialogSelect.setTitle(title);
        txtTitle.setText(title);
    }

    public void show() {
        populate();
        dialogSelect.show();
    }

    private void setMultiSelectDialog() {

        dialogSelect = new Dialog(mContext, R.style.ThemeDialogCustom_NoBackground);
        dialogSelect.setContentView(R.layout.dialog_multi_select);
        dialogSelect.setTitle(R.string.title_select);

        lvSelect = (ListView) dialogSelect.findViewById(R.id.lv_dialog_multi_select);
        txtTitle = (TextView) dialogSelect.findViewById(R.id.txt_dialog_multi_select_title);
        btnSave = (Button) dialogSelect.findViewById(R.id.btn_dialog_multi_select_save);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                mVibrator.vibrate(25);

                multiSelectDialogActionListener.actionCompletedSuccessfully(selectedItemList);
                dialogSelect.dismiss();
            }
        });

    }

    private void populate() {
        SelectItemBean bean;
        /*if (AppConstants.ACTION_CHOOSE_HAVING_DOSHAM.equals(action)) {
            List<String> list = Arrays.asList(mContext.getResources().getStringArray(R.array.dosham));
            choiceList = new ArrayList<>();
            bean = new SelectItemBean();
            bean.setId(String.valueOf(0));
            bean.setValue(mContext.getApplicationContext().getString(R.string.btn_any));
            choiceList.add(bean);
            for (int i = 0; i < list.size(); i++) {
                bean = new SelectItemBean();
                bean.setId(String.valueOf(i + 1));
                bean.setValue(list.get(i));
                choiceList.add(bean);
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } else*/
       /* if (AppConstants.ACTION_CHOOSE_PRACTICE_TYPE.equals(action)) {
            choiceList = new ArrayList<>();
            for (PracticeTypeBean practiceTypeBean : basicDataBean.getPracticeTypes()) {
                bean = new SelectItemBean();
                bean.setId(String.valueOf(practiceTypeBean.getId()));
                bean.setValue(practiceTypeBean.getName());
                choiceList.add(bean);
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } else if (AppConstants.ACTION_CHOOSE_COUNTRY.equals(action)) {
            choiceList = new ArrayList<>();
            for (CountryBean countryBean : basicDataBean.getCountries()) {
                bean = new SelectItemBean();
                bean.setId(String.valueOf(countryBean.getId()));
                bean.setValue(countryBean.getName());
                choiceList.add(bean);
            }
            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        }*//* else if (AppConstants.ACTION_CHOOSE_STATE.equals(action)) {
            choiceList = new ArrayList<>();
            for (String id : IDs) {
                if (basicDataBean.getCountry(id).getStates() != null) {
                    for (StateBean stateBean : basicDataBean.getCountry(id).getStates()) {
                        bean = new SelectItemBean();
                        bean.setId(stateBean.getId());
                        bean.setValue(stateBean.getName());
                        choiceList.add(bean);
                    }
                }
            }

            if (adapter == null)
                adapter = new MultiSelectDialog.SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        }*//* else if (AppConstants.ACTION_CHOOSE_STATE.equals(action)) {
            choiceList = new ArrayList<>();
            if (basicDataBean.getStates() != null) {
                for (String id : IDs) {
                    for (StateBean stateBean : basicDataBean.getStateList(Integer.parseInt(id))) {
                        bean = new SelectItemBean();
                        bean.setId(String.valueOf(stateBean.getId()));
                        bean.setValue(stateBean.getName());
                        choiceList.add(bean);
                    }
                }
            }

            if (adapter == null)
                adapter = new SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        }*/ /*else if (AppConstants.ACTION_CHOOSE_DISTRICT.equals(action)) {
            choiceList = new ArrayList<>();
            for (String id : IDs) {
                if (basicDataBean.getState(id, subID) != null && basicDataBean.getState(id, subID).getDistricts() != null) {
                    for (CityBean districtBean : basicDataBean.getState(id, subID).getDistricts()) {
                        bean = new SelectItemBean();
                        bean.setId(districtBean.getId());
                        bean.setValue(districtBean.getName());
                        choiceList.add(bean);
                    }
                }
            }
            if (adapter == null)
                adapter = new MultiSelectDialog.SelectAdapter();
            else
                adapter.notifyDataSetChanged();
            lvSelect.setAdapter(adapter);
        } */ /*else if (AppConstants.ACTION_CHOOSE_DISTRICT.equals(action)) {
            choiceList = new ArrayList<>();
            if (basicDataBean.getDistricts() != null) {
                for (String id : IDs) {
                    for (DistrictBean districtBean : basicDataBean.getDistrictList(Integer.parseInt(id))) {
                        bean = new SelectItemBean();
                        bean.setId(String.valueOf(districtBean.getId()));
                        bean.setValue(districtBean.getName());
                        choiceList.add(bean);
                    }
                }
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
            if (AppConstants.ACTION_CHOOSE_YEAR.equals(action)) {
                int num = Calendar.getInstance().get(Calendar.YEAR) - AppConstants.YEAR_START - 18 + 1;
                return num;
            } else
                return choiceList.size();
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

            final SelectItemBean itemBean = choiceList.get(position);

            ViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {
                // inflate the layout
                lytItem = inflater.inflate(R.layout.item_multi_select, null);
                holder = new ViewHolder();

                holder.cbSelect = (CheckBox) lytItem.findViewById(R.id.cb_item_multi_select_name);

                lytItem.setTag(/*R.id.TAG_POST,*/ holder);
            } else {
                lytItem = convertView;
            }
            holder = (ViewHolder) lytItem.getTag(/*R.id.TAG_POST*/);

         /*   if (AppConstants.ACTION_CHOOSE_YEAR.equals(action)) {
                holder.cbSelect.setText(choiceList[position]);
            } else {
                holder.cbSelect.setText(choiceList[position]);
            }*/
            if (AppConstants.ACTION_CHOOSE_YEAR.equals(action)) {
                holder.cbSelect.setText(itemBean.getValue());
            } else {
                holder.cbSelect.setText(itemBean.getValue());
            }

         /*   if(selectedPositionList.contains(position))
                holder.cbSelect.setChecked(true);
            else
                holder.cbSelect.setChecked(false);
*/

            if (isSelectedListContains(itemBean.getValue()))
                holder.cbSelect.setChecked(true);
            else
                holder.cbSelect.setChecked(false);


            final ViewHolder finalHolder = holder;
            holder.cbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    mVibrator.vibrate(25);

                    boolean isChecked = finalHolder.cbSelect.isChecked();
                    if (isChecked) {
                        if (!itemBean.getValue().equalsIgnoreCase(mContext.getApplicationContext().getString(R.string.btn_any))) {
                            if (!isSelectedListContains(itemBean.getValue())) {
                                SelectResultBean bean = new SelectResultBean();
                                bean.setPosition(position);
                                bean.setId(itemBean.getId());
                                bean.setValue(itemBean.getValue());
                                selectedItemList.add(bean);
                                if (isSelectedListContains(mContext.getApplicationContext().getString(R.string.btn_any))) {
                                    removeSelectedItem(mContext.getApplicationContext().getString(R.string.btn_any));
                                    notifyDataSetChanged();
                                }
                            } else {
                                SelectResultBean bean = new SelectResultBean();
                                bean.setPosition(position);
                                bean.setId(itemBean.getId());
                                bean.setValue(itemBean.getValue());
                                removeSelectedItem(itemBean.getValue());
                                selectedItemList.add(bean);
                            }
                        } else {
                            removeAllSelectedItems();
                            SelectResultBean bean = new SelectResultBean();
                            bean.setPosition(position);
                            bean.setId(itemBean.getId());
                            bean.setValue(itemBean.getValue());
                            selectedItemList.add(bean);
                            notifyDataSetChanged();
                        }
                    } else {
                        if (isSelectedListContains(itemBean.getValue())) {
                            removeSelectedItem(itemBean.getValue());
                        }
                    }
                }
            });

           /* holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean icChecked) {
                    compoundButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    if(!selectedPositionList.contains(position)){
                        selectedPositionList.add(position);
                    }

                }
            });*/


/*            lytItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    mVibrator.vibrate(25);

                    multiSelectDialogActionListener.actionCompletedSuccessfully(selectedPositionList);
                    dialogSelect.dismiss();
                }

                *//*else{
                    String categoryID = interestList.get(position).getId();
                    Intent intent = new Intent();
                    intent.putExtra("id", categoryID);
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }*//*
            });*/

            return lytItem;
        }


        private void removeAllSelectedItems() {
            selectedItemList = new ArrayList<>();
            notifyDataSetChanged();
        }

        private void removeSelectedItem(String item) {
            for (int i = 0; i < selectedItemList.size(); i++) {
                if (selectedItemList.get(i).getValue().equalsIgnoreCase(item)) {
                    selectedItemList.remove(i);
                    return;
                }
            }
        }

        private boolean isSelectedListContains(String item) {
            for (int i = 0; i < selectedItemList.size(); i++) {
                if (selectedItemList.get(i).getValue().equalsIgnoreCase(item)) {
                    return true;
                }
            }
            return false;
        }
    }


    private static class ViewHolder {
        private CheckBox cbSelect;
    }


    public interface MultiSelectDialogActionListener {
        void actionCompletedSuccessfully(List<SelectResultBean> selectedItemList);

        void actionFailed(String errorMsg);
    }

    public MultiSelectDialogActionListener getMultiSelectDialogActionListener() {
        return multiSelectDialogActionListener;
    }


    public void setMultiSelectDialogActionListener(MultiSelectDialogActionListener multiSelectDialogActionListener) {
        this.multiSelectDialogActionListener = multiSelectDialogActionListener;
    }
}
