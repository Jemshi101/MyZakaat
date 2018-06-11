package com.quartzbit.myzakaat.net.invokers;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import com.quartzbit.myzakaat.model.BasicBean;
import com.quartzbit.myzakaat.net.ServiceNames;
import com.quartzbit.myzakaat.net.WebConnector;
import com.quartzbit.myzakaat.net.parsers.BasicParser;
import com.quartzbit.myzakaat.net.utils.WSConstants;

/**
 * Created by Jemsheer K D on 04 May, 2017.
 * Package com.quartzbit.myzakaat.net.invokers
 * Project Dearest
 */

public class UpdateFCMTokenInvoker extends BaseInvoker {

    public UpdateFCMTokenInvoker() {
        super();
    }

    public UpdateFCMTokenInvoker(HashMap<String, String> urlParams,
                                 JSONObject postData) {
        super(urlParams, postData);
    }

    public BasicBean invokeUpdateFCMTokenWS() {

        Log.i("API", ">>>>>>>> API POSTDATA : " + postData);

        WebConnector webConnector;

        webConnector = new WebConnector(new StringBuilder(ServiceNames.FCM_UPDATE), WSConstants.PROTOCOL_HTTP, null, postData);

        //		webConnector= new WebConnector(new StringBuilder(ServiceNames.AUTH_EMAIL), WSConstants.PROTOCOL_HTTP, postData,null);
        //webConnector= new WebConnector(new StringBuilder(ServiceNames.MODELS), WSConstants.PROTOCOL_HTTP, null);
        String wsResponseString = webConnector.connectToPOST_service();
        //	String wsResponseString=webConnector.connectToGET_service();
        Log.i("API", ">>>>>>>>>>> API response: " + wsResponseString);
        BasicBean basicBean = null;
        if (wsResponseString.equals("")) {
            /*registerBean=new RegisterBean();
            registerBean.setWebError(true);*/
            return basicBean = null;
        } else {
            basicBean = new BasicBean();
            BasicParser basicParser = new BasicParser();
            basicBean = basicParser.parseBasicResponse(wsResponseString);
            return basicBean;
        }
    }
}
