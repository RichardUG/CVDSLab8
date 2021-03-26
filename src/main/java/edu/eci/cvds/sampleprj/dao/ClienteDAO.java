package edu.eci.cvds.sampleprj.dao;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;

import java.util.List;
import java.sql.Date;

public interface ClienteDAO {
    public Cliente load(int id) throws PersistenceException;
    List<ItemRentado> loadItemsCliente(int idCliente) throws PersistenceException;
    public void agregarItemRentado(long idcli, int id, Date fechainicio, Date fechafin) throws PersistenceException;
    public List<Cliente> consultarClientes() throws PersistenceException;
    public void save(Cliente cli) throws PersistenceException;
}