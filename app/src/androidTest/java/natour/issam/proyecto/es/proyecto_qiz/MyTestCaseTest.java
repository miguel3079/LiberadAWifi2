package natour.issam.proyecto.es.proyecto_qiz;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseUser;
/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class MyTestCaseTest extends ActivityInstrumentationTestCase2<Login> {
    //Variables
    private ImageView entrar;
    private EditText etext1;
    private EditText etext2;
    private static final String Login = "Miguel";
    private static final String Password = "Miguel";
    private Login actividad;
    private String ResultadoObtenido;
    private String ResultadoEsperado;

    public MyTestCaseTest() {
        super(Login.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //recojo la actividad actual
        actividad = getActivity();
        //almaceno el nombre de la actividad para poder comprobarla mas adelante
        ResultadoObtenido = actividad.getClass().getName();
        //Instanciamos los edittext y imageview
        etext1 = (EditText) actividad.findViewById(R.id.editText);
        etext2 = (EditText) actividad.findViewById(R.id.editText2);
        entrar = (ImageView) actividad.findViewById(R.id.imageViewLogin);

    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testAddValues() {
        // Comprobamos si el usuario esta logeado o no
        if (ParseUser.getCurrentUser()!=null) {
            ParseUser.logOut();
        }
        //introducco
        TouchUtils.tapView(this, etext1);
        getInstrumentation().sendStringSync(Login);
        // Introducimos los valores
        TouchUtils.tapView(this, etext2);
        getInstrumentation().sendStringSync(Password);
        // Comprueban logeando
        TouchUtils.clickView(this, entrar);
        //Guardamos el nombre de la actividad siguiete si a logeado si es asi la comprobamos
        ResultadoEsperado = getActivity().getClass().getName();
        assertTrue("Login result should be...", ResultadoEsperado.equals(ResultadoObtenido));
    }
}