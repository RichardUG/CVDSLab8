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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    public int valorMultaRetrasoxDia(int itemId) throws ExcepcionServiciosAlquiler {
        try {
            return (int) itemDAO.load(itemId).getTarifaxDia();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("El item no esta registrado",ex);
        }
    }

    @Override
    public Cliente consultarCliente(int docu) throws ExcepcionServiciosAlquiler {
        try {
            Cliente cliente =clienteDAO.load(docu);
            if (cliente==null) throw new ExcepcionServiciosAlquiler("El cliente no esta registrado");
            else return cliente;

        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el cliente con identificacion "+docu,ex);
        }
    }

    @Override
    public List<ItemRentado> consultarItemsCliente(long idcliente) throws ExcepcionServiciosAlquiler {
        Cliente cliente = consultarCliente((int) idcliente);
        if (cliente==null)throw new ExcepcionServiciosAlquiler("el cliente no esta registrado");
        else return consultarCliente((int) idcliente).getRentados();
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
    public List<Item> consultarItemsDisponibles() throws ExcepcionServiciosAlquiler {
        try {
            return itemDAO.consultarItemsDisponibles();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar los items ",ex);
        }
    }

    @Override
    public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
        List<Cliente> clientes = consultarClientes();
        for (Cliente cliente:clientes) {
            ArrayList<ItemRentado> rentados = cliente.getRentados();
            for (ItemRentado item :rentados) {
                if( item.getItem().getId()==iditem) {
                    LocalDate fechaFin=item.getFechafinrenta().toLocalDate();
                    LocalDate fechaEntrego=fechaDevolucion.toLocalDate();
                    long diasRetraso = ChronoUnit.DAYS.between(fechaFin, fechaEntrego);
                    if(diasRetraso<0){
                        return 0;
                    }
                    return diasRetraso*valorMultaRetrasoxDia(item.getItem().getId());
                }
            }
        }
        throw  new ExcepcionServiciosAlquiler("El item"+iditem+"no se encuentra rentado");
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
        LocalDate actual=date.toLocalDate();
        LocalDate entrega=actual.plusDays(numdias);
        if(numdias<1)throw  new ExcepcionServiciosAlquiler("el numero de dias debe ser mayor o igual a 1");
        if(consultarCliente((int) docu)==null)throw new ExcepcionServiciosAlquiler("El cliente no esta registrado");
        if(consultarItem(item.getId())==null)throw new ExcepcionServiciosAlquiler("El item no esta registrado");
        for (ItemRentado itemRentado:consultarCliente((int) docu).getRentados()) {
            if (itemRentado.getItem().getId() == item.getId())
                throw new ExcepcionServiciosAlquiler("Este item con id: " + item.getId() + " ya se encuentra rentado");
        }
        try {
            clienteDAO.agregarItemRentado(docu,item.getId(),date,java.sql.Date.valueOf(entrega));
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al agregar el item"
                    +item+" a los items rentados del cliente"+docu,e);
        }
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
        Item item=consultarItem(iditem);
        if (item==null) throw new ExcepcionServiciosAlquiler("El item no existe");
        else if(numdias<1) throw new ExcepcionServiciosAlquiler("el numero de dias debe ser mayor o igual a 1");
        else return consultarItem(iditem).getTarifaxDia()*numdias;
    }

    @Override
    public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
        try {
            itemDAO.actualizarTarifaItem(id,tarifa);
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al actualizar  el item "+id,e);
        }
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
        try {
            clienteDAO.vetarCliente(docu,estado?1:0);
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al actualizar la informacion del cliente"+docu,e);
        }
    }

    @Override
    public void registrarTipoItem(TipoItem tipoItem) throws ExcepcionServiciosAlquiler {
        try {
            tipoItemDAO.save(tipoItem);
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al insertar el tipo de Item "+tipoItem.getID(),e);
        }
    }
}