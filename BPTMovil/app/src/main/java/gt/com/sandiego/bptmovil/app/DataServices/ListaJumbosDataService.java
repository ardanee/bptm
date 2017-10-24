package gt.com.sandiego.bptmovil.app.DataServices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Functions;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Globals;

/**
 * Created by lramirez on 17/10/2017.
 */

public class ListaJumbosDataService {
    Globals vars = Globals.getInstance();
    Functions util = new Functions();

    public String GetJumboAFecha(String correlativo, String xToken) throws Exception {
        //crate Request
        String methodName = "GetJumboAFecha";
        String respuesta = "";
        SoapObject request = new SoapObject(vars.getNAMESPACE(), methodName);
        //Property which holds input parameters

        PropertyInfo usuarioInfo = new PropertyInfo();
        PropertyInfo identificadorInfo = new PropertyInfo();
        PropertyInfo tokenInfo = new PropertyInfo();
        PropertyInfo correlativoInfo = new PropertyInfo();
        //Set Name

        usuarioInfo.setName("Usuario");
        identificadorInfo.setName("Identificacion");
        tokenInfo.setName("Token");
        correlativoInfo.setName("Correlativo");
        //Set Value

        usuarioInfo.setValue(vars.getUsuario());
        identificadorInfo.setValue(util.md5(vars.getIdentificacion()));
        tokenInfo.setValue(xToken);
        correlativoInfo.setValue(correlativo);
        //Set DataType

        usuarioInfo.setType(String.class);
        identificadorInfo.setType(String.class);
        tokenInfo.setType(String.class);
        correlativoInfo.setType(String.class);
        //Add the Proprty to Request object

        request.addProperty(usuarioInfo);
        request.addProperty(identificadorInfo);
        request.addProperty(tokenInfo);
        request.addProperty(correlativoInfo);
        //Crea Envelope

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object

        envelope.setOutputSoapObject(request);
        //Create HTTP call object

        HttpTransportSE androidHttpTransport = new HttpTransportSE(vars.getWSURL());
        try {
            //Invoke Web Service
            androidHttpTransport.call(vars.getSOAP_METHOD() + methodName, envelope);
            //Get the Response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            respuesta = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(vars.getTAG(), "GetJumboAFecha", e);
        }
        return respuesta;
    }

}
