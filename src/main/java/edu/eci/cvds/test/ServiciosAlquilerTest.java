package edu.eci.cvds.test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquilerFactory;
import org.apache.ibatis.ognl.ParseException;
import org.apache.ibatis.session.SqlSession;
import org.junit.*;

public class ServiciosAlquilerTest {

    @Inject
    private SqlSession sqlSession;

    ServiciosAlquiler serviciosAlquiler;

    public ServiciosAlquilerTest() {
        serviciosAlquiler = ServiciosAlquilerFactory.getInstance().getServiciosAlquilerTesting();
    }

    @Before
    public void setUp() {
    }

    @Test
    public void emptyDB() {
        for(int i = 0; i < 100; i += 10) {
            boolean r = false;
            try {
                Cliente cliente = serviciosAlquiler.consultarCliente(2159581);
            } catch(ExcepcionServiciosAlquiler e) {
                r = true;
            } catch(IndexOutOfBoundsException e) {
                r = true;
            }
            // Validate no Client was found;
            Assert.assertTrue(r);
        };
    }


    @Test
    public void ConsultarCostoDeUnItemAgregado() throws ExcepcionServiciosAlquiler{
        try {
            Item item = new Item(new TipoItem(44, "test" ),6,
                    "bicicleta", "roja Pai", new SimpleDateFormat("yyyy/MM/dd").parse("2022/12/24"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarItem(item);
            Assert.assertEquals(100*5, serviciosAlquiler.consultarCostoAlquiler(6, 5));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }



    @Test
    public void ConsultarCliente(){
        try {
            ArrayList<ItemRentado> itemRentados = new ArrayList<ItemRentado>();
            Cliente cliente = new Cliente("Ana Silva",1192793544,"3015645037","cra 70c #2-20 sur","ana.silva-b@mail.escuelaing.edu.co",false,itemRentados);
            serviciosAlquiler.registrarCliente(cliente);
            Assert.assertEquals("Ana Silva", serviciosAlquiler.consultarCliente(1192793544).getNombre());
        } catch (Exception e) {
            Assert.fail("No pasa la prueba");
        }
    }

    @Test
    public void ConsultarTarifaxDia() throws ParseException, ExcepcionServiciosAlquiler {
        try {
            /* Cliente cliente = new Cliente("Nombre",111,"telefono","direccion","email",false,null); */
            Item item = new Item(new TipoItem(44, "test" ),6,
                    "bicicleta", "roja Pai", new SimpleDateFormat("yyyy/MM/dd").parse("2022/12/24"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarItem(item);
            Assert.assertEquals(100, serviciosAlquiler.valorMultaRetrasoxDia(6));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void ConsultarItem(){
        try {
            Item item = new Item(new TipoItem(150, "prueba" ),150,
                    "test", "un test", new SimpleDateFormat("yyyy/MM/dd").parse("2022/7/4"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarItem(item);
            Assert.assertEquals(150, serviciosAlquiler.consultarItem(150).getId());
        } catch (Exception e) {
            Assert.fail("No pasa la prueba");
        }
    }

    @Test
    public void ActualizarTarifaItem(){
        try {
            Item item = new Item(new TipoItem(150, "prueba" ),150,
                    "prueba", "prueba Pa", new SimpleDateFormat("yyyy/MM/dd").parse("2020/10/4"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarItem(item);
            serviciosAlquiler.actualizarTarifaItem(150, 200);
            Assert.assertEquals(200, serviciosAlquiler.consultarItem(2).getTarifaxDia());
        } catch (Exception e) {
            Assert.fail("No pasa la prueba");
        }
    }

    @Test
    public void RegistrarItemCliente() throws ExcepcionServiciosAlquiler {
        try {
            Item item = new Item(new TipoItem(14, "xd" ),1010,
                    "tefelono", "tefelono", new SimpleDateFormat("yyyy/MM/dd").parse("2022/11/04"),
                    100,"prueba","prueba");
            Cliente cliente = new Cliente("prueba", 2000, "prueba", "prueba","prueba");
            serviciosAlquiler.registrarCliente(cliente);
            serviciosAlquiler.registrarAlquilerCliente(Date.valueOf(LocalDate.parse("2022/11/04")) , serviciosAlquiler.consultarCliente(2000).getDocumento() , item , 4 );
            Assert.assertEquals(Date.valueOf(LocalDate.parse("2022/11/04")), serviciosAlquiler.consultarCliente(2000).getRentados().get(0).getFechainiciorenta());
        } catch (Exception e) {
            Assert.fail("No pasa la prueba");
        }
    }


}