 package gt.com.sandiego.bptmovil.app.DataServices;

import android.net.Uri;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.InputStream;

import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Functions;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Globals;

 /**
 * Created by angelmendoza on 29/05/17.
 */

public class LoginDataService {

    Globals vars = Globals.getInstance();
    Functions util = new Functions();

    public void getLogin(String xUsuario, String xIdentificacion, String xToken) throws Exception{
        //crate Request

        SoapObject request = new SoapObject(vars.getNAMESPACE(),vars.getMETHOD_NAME());
        //Property which holds input parameters

        PropertyInfo usuarioInfo = new PropertyInfo();
        PropertyInfo identificadorInfo = new PropertyInfo();
        PropertyInfo tokenInfo = new PropertyInfo();
        //Set Name

        usuarioInfo.setName("Usuario");
        identificadorInfo.setName("Identificacion");
        tokenInfo.setName("Token");
        //Set Value

        usuarioInfo.setValue(xUsuario);
        identificadorInfo.setValue(util.md5(xIdentificacion));
        tokenInfo.setValue(xToken);
        //Set DataType

        usuarioInfo.setType(String.class);
        identificadorInfo.setType(String.class);
        tokenInfo.setType(String.class);
        //Add the Proprty to Request object

        request.addProperty(usuarioInfo);
        request.addProperty(identificadorInfo);
        request.addProperty(tokenInfo);
        //Crea Envelope

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object

        envelope.setOutputSoapObject(request);
        //Create HTTP call object

        HttpTransportSE androidHttpTransport = new HttpTransportSE(vars.getWSURL());
        try
        {
            //Invole Web Service
            androidHttpTransport.call(vars.getSOAP_METHOD()+ vars.getMETHOD_NAME(), envelope);
            //Get the Response
            SoapPrimitive response = (SoapPrimitive)envelope.getResponse();

            vars.setRespuesta(response.toString());

        }catch (Exception e){
            e.printStackTrace();
            Log.d(vars.getTAG(), "Login", e);
        }

    }
}
