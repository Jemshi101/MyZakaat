package com.quartzbit.myzakaat.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jemsheer K D on 03 December, 2016.
 * Package com.Wakilishwa.model
 * Project Wakilishwa
 */

public class BasicDataBean extends BaseBean {

    static final long serialVersionUID = 1L;
    private static final String TAG = "BDBean";

    private String versionID;
    private List<CountryBean> countries;
    private List<StateBean> states;
    private List<ReligionBean> religions;


    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public List<CountryBean> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryBean> countries) {
        this.countries = countries;
    }

    public List<StateBean> getStates() {
        return states;
    }

    public void setStates(List<StateBean> states) {
        this.states = states;
    }

    public List<ReligionBean> getReligions() {
        return religions;
    }

    public void setReligions(List<ReligionBean> religions) {
        this.religions = religions;
    }

    public ReligionBean getReligion(String id) {
        for (ReligionBean bean : religions) {
            if (bean.getId().equalsIgnoreCase(id)) {
                return bean;
            }
        }
        return null;
    }

    public ReligionBean getReligionFromName(String name) {
        for (ReligionBean bean : religions) {
            if (bean.getName().equalsIgnoreCase(name)) {
                return bean;
            }
        }
        return null;
    }

    public CountryBean getCountry(String id) {
        for (CountryBean bean : countries) {
            if (bean.getId().equalsIgnoreCase(id)) {
                return bean;
            }
        }
        return null;
    }

    public CountryBean getCountryFromName(String name) {
        for (CountryBean bean : countries) {
            if (bean.getName().equalsIgnoreCase(name)) {
                return bean;
            }
        }
        return null;
    }

/*    public StateBean getState(String countryID, String stateID) {
        CountryBean countryBean = getCountry(countryID);
        try {
            for (int i = 0; i < countryBean.getStates().size(); i++) {
                if (countryBean.getStates().get(i).getId().equalsIgnoreCase(stateID)) {
                    return countryBean.getStates().get(i);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }*/

    public StateBean getState(String stateID) {
        if (states != null) {
            for (StateBean bean : states) {
                if (bean.getId().equalsIgnoreCase(stateID)) {
                    return bean;
                }
            }
        }
        return null;
    }

/*    public StateBean getStateFromName(String countryID, String name) {
        CountryBean countryBean = getCountry(countryID);
        try {
            for (int i = 0; i < countryBean.getStates().size(); i++) {
                if (countryBean.getStates().get(i).getName().equalsIgnoreCase(name)) {
                    return countryBean.getStates().get(i);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }*/

    public StateBean getStateFromName(String countryID, String name) {
        if (states != null) {
            for (StateBean bean : states) {
                if (bean.getName().equalsIgnoreCase(name) && bean.getCountryID().equalsIgnoreCase(countryID)) {
                    Log.i(TAG, "getStateFromName: Name : " + bean.getName() + " ID : " + bean.getId());
                    return bean;
                } else {
                    Log.i(TAG, "getStateFromName: NOT SELECTED Name : " + bean.getName() + " ID : " + bean.getId());
                }
            }
        }
        return null;
    }

    public StateBean getStateFromName(String name) {
        if (states != null) {
            for (StateBean bean : states) {
                if (bean.getName().equalsIgnoreCase(name)) {
                    Log.i(TAG, "getStateFromName: Name : " + bean.getName() + " ID : " + bean.getId());
                    return bean;
                } else {
                    Log.i(TAG, "getStateFromName: NOT SELECTED Name : " + bean.getName() + " ID : " + bean.getId());
                }
            }
        }
        return null;
    }

    public void setStates(String countryID, List<StateBean> states) {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getId().equalsIgnoreCase(countryID)) {
                countries.get(i).setStates(states);
                break;
            }
        }
    }


    public List<StateBean> getStateList(String countryID) {
        List<StateBean> stateList = new ArrayList<>();
        if (states != null) {
            for (StateBean bean : states) {
                if (bean.getCountryID().equalsIgnoreCase(countryID)) {
                    stateList.add(bean);
                }
            }
        }
        return stateList;
    }

    public void update(BasicDataBean basicDataBeanWS) {

        if (!basicDataBeanWS.getCountries().equals(countries)) {
            countries = basicDataBeanWS.getCountries();
            states = null;
        }
    }

    public void updateReligions(List<ReligionBean> religionList) {

        if (religions != null) {
            for (int i = 0; i < religions.size(); i++) {
                removeStates(religions.get(i).getId());
                religions.remove(i);
                i--;
            }
        } else {
            religions = new ArrayList<>();
        }

        if (religionList != null) {
            religions.addAll(religionList);
        }
    }

    public void updateCountries(List<CountryBean> countryList) {

        if (countries != null) {
            for (int i = 0; i < countries.size(); i++) {
                removeStates(countries.get(i).getId());
                countries.remove(i);
                i--;
            }
        } else {
            countries = new ArrayList<>();
        }

        if (countryList != null) {
            countries.addAll(countryList);
        }
    }

    public void updateStates(String countryID, List<StateBean> stateList) {

        if (states != null) {
            for (int i = 0; i < states.size(); i++) {
                if (states.get(i).getCountryID().equalsIgnoreCase(countryID)) {
                    states.remove(i);
                    i--;
                }
            }
        } else {
            states = new ArrayList<>();
        }

        if (stateList != null) {
            states.addAll(stateList);
        }
    }


    public void removeStates(String countryID) {
        if (states != null) {
            for (int i = 0; i < states.size(); i++) {
                if (states.get(i).getCountryID().equalsIgnoreCase(countryID)) {
                    states.remove(i);
                    i--;
                }
            }
        }
    }

}
