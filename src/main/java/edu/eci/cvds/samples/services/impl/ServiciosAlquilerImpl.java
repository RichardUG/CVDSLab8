package edu.eci.cvds.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.*;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import java.sql.Date;
import java.util.List;

@Singleton
public class ServiciosAlquilerImpl implements ServiciosAlquiler {
    @Inject
    private ClienteDAO clienteDAO;
    @Inject
    private ItemDAO itemDAO;
    @Inject
    private TipoItemDAO tipoItemDAO;
    @Inject
    private ItemRentadoDAO itemRentadoDAO;


    @Override
    public int valorMultaRetrasoxDia(int itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Cliente consultarCliente(int docu) throws ExcepcionServiciosAlquiler {
        try {
            return clienteDAO.load(docu);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar clientes",ex);
        }
    }

    @Override
    public List<ItemRentado> consultarItemsCliente(long idcliente) throws ExcepcionServiciosAlquiler {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
        try {
            return clienteDAO.consultarClientes();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar clientes",ex);
        }
    }

    @Override
    public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            return itemDAO.load(id);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el item "+id,ex);
        }
    }

    @Override
    public List<Item> consultarItemsDisponibles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            return tipoItemDAO.load(id);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el tipo de item "+id,ex);
        }
    }

    @Override
    public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
        try {
            return tipoItemDAO.loadTiposItems();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar los tipos de items ",ex);
        }
    }

    @Override
    public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registrarCliente(Cliente c) throws ExcepcionServiciosAlquiler {
        try {
            clienteDAO.save(c);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al registrar el cliente",ex);
        }
    }

    @Override
    public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
        try {
            itemDAO.save(i);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al registrar el item "+i,ex);
        }
    }

    @Override
    public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registrarTipoItem(TipoItem tipoItem) throws ExcepcionServiciosAlquiler {

    }
}