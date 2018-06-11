package com.quartzbit.myzakaat.net.invokers;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import com.quartzbit.myzakaat.model.BasicBean;
import com.quartzbit.myzakaat.net.ServiceNames;
import com.quartzbit.myzakaat.net.WebConnector;
import com.quartzbit.myzakaat.net.parsers.LocationNameParser;
import com.quartzbit.myzakaat.net.utils.WSConstants;


public class LocationNameInvoker extends BaseInvoker {

    public LocationNameInvoker() {
        super();
    }

    public LocationNameInvoker(HashMap<String, String> urlParams,
                               JSONObject postData) {
        super(urlParams, postData);
    }

    public BasicBean invokeLocationNameWS() {

        WebConnector webConnector;

        webConnector = new WebConnector(new StringBuilder(ServiceNames.LOCATION_NAME),
                WSConstants.PROTOCOL_HTTP, urlParams, null);

        //webConnector= new WebConnector(new StringBuilder(ServiceNames.MODELS), WSConstants.PROTOCOL_HTTP, null);
//    String wsResponseString=webConnector.connectToPOST_service();
        String wsResponseString = webConnector.connectToGET_service(true);
        Log.i("API", ">>>>>>>>>>> API response: " + wsResponseString);
        BasicBean basicBean = null;
        String address = null;
        if (wsResponseString.equals("")) {
			/*registerBean=new RegisterBean();
			registerBean.setWebError(true);*/
            return basicBean = null;
        } else {
            basicBean = new BasicBean();
            LocationNameParser dummyParser = new LocationNameParser();
            address = dummyParser.parseLocationNameResponse(wsResponseString);
            basicBean.setStatus("Success");
            basicBean.setAddress(address);
            return basicBean;
        }
    }
}

