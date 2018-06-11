package com.quartzbit.myzakaat.net.invokers;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import com.quartzbit.myzakaat.model.PolyPointBean;
import com.quartzbit.myzakaat.net.ServiceNames;
import com.quartzbit.myzakaat.net.WebConnector;
import com.quartzbit.myzakaat.net.parsers.PolyPointParser;
import com.quartzbit.myzakaat.net.utils.WSConstants;

/**
 * Created by Jemsheer K D on 09 May, 2017.
 * Package com.quartzbit.myzakaat.net.invokers
 * Project Dearest
 */

public class PolyPointInvoker extends BaseInvoker {

    public PolyPointInvoker() {
        super();
    }

    public PolyPointInvoker(HashMap<String, String> urlParams,
                            JSONObject postData) {
        super(urlParams, postData);
    }

    public PolyPointBean invokePolyPointWS() {

        WebConnector webConnector;

        webConnector = new WebConnector(new StringBuilder(ServiceNames.POLY_POINTS), WSConstants.PROTOCOL_HTTP, urlParams, null);

        //webConnector= new WebConnector(new StringBuilder(ServiceNames.MODELS), WSConstants.PROTOCOL_HTTP, null);
//    String wsResponseString=webConnector.connectToPOST_service();
        String wsResponseString = webConnector.connectToGET_service(false);
        Log.i("API", ">>>>>>>>>>> API response: " + wsResponseString);
        PolyPointBean polyPointBean = null;
        if (wsResponseString.equals("")) {
            /*registerBean=new RegisterBean();
            registerBean.setWebError(true);*/
            return polyPointBean = null;
        } else {
            polyPointBean = new PolyPointBean();
            PolyPointParser polyPointParser = new PolyPointParser();
            polyPointBean = polyPointParser.parsePolyPointResponse(wsResponseString);
            return polyPointBean;
        }
    }
}
