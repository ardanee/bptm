package gt.com.sandiego.bptmovil.app.ClasesUtilidades;

/**
 * Created by angelmendoza on 29/05/17.
 */

public class Globals {

    private static Globals instance;

    private Globals(){}

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    private	String NAMESPACE = "http://app.sandiego.com.gt/ws";
    private	String WSURL = "http://app.sandiego.com.gt/ws/wsproductoterminado.asmx";
    private	String SOAP_METHOD = "http://app.sandiego.com.gt/ws/";
    private	String Respuesta = "";
    private	String METHOD_NAME = "";
    //private	String SOAP_METHOD = "http://216.230.141.102/ws/";

    private	String TAG = "SD";
    private String Token= "";
    private String ErrorMensaje="";
    private String TokenAPI="";
    private String INFO_FILE= "https://dl.dropbox.com/s/csj5i7gz8ou60d5/version.txt";
    private String NameApi = "bptmovil.apk";
    //variables de version
    private String Version="";
    private String VersionServidor="";
    //-----------------------------
    private String API_URL_FCM ="https://fcm.googleapis.com/fcm/send";
    private String AUTH_KEY_FCM = "AIzaSyBIYTW-t2HlOwP70yCX-R7N0pMCvIe4vRg";

    private String Usuario = "",Identificacion ="",IdUsuario="",IdRol="",Iniciales="",Nombre="";
    private String IdUsuarioBuscar="";
    private boolean Recordar = false;

    private String URLFIREBASE="gs://seguridadsd-638eb.appspot.com/Imagenes/";

    public String getURLFIREBASE() {
        return URLFIREBASE;
    }

    public String getAUTH_KEY_FCM() {
        return AUTH_KEY_FCM;
    }

    public String getAPI_URL_FCM() {
        return API_URL_FCM;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getIdentificacion() {
        return Identificacion;
    }

    public void setIdentificacion(String identificacion) {
        Identificacion = identificacion;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getIdRol() {
        return IdRol;
    }

    public void setIdRol(String idRol) {
        IdRol = idRol;
    }

    public String getIniciales() {
        return Iniciales;
    }

    public void setIniciales(String iniciales) {
        Iniciales = iniciales;
    }

    public boolean isRecordar() {
        return Recordar;
    }

    public void setRecordar(boolean recordar) {
        Recordar = recordar;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getIdUsuarioBuscar() {
        return IdUsuarioBuscar;
    }

    public void setIdUsuarioBuscar(String idUsuarioBuscar) {
        IdUsuarioBuscar = idUsuarioBuscar;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getNAMESPACE() {
        return NAMESPACE;
    }

    public String getWSURL() {
        return WSURL;
    }

    public String getSOAP_METHOD() {
        return SOAP_METHOD;
    }

    public String getMETHOD_NAME() {
        return METHOD_NAME;
    }

    public void setMETHOD_NAME(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
    }

    public String getTAG() {
        return TAG;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(String respuesta) {
        Respuesta = respuesta;
    }

    public String getErrorMensaje() {
        return ErrorMensaje;
    }

    public String getTokenAPI() {
        return TokenAPI;
    }

    public String getINFO_FILE() {
        return INFO_FILE;
    }

    public String getNameApi() {
        return NameApi;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getVersionServidor() {
        return VersionServidor;
    }

    public void setVersionServidor(String versionServidor) {
        VersionServidor = versionServidor;
    }


}
