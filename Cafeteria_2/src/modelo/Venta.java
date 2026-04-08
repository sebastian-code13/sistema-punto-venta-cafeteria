package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Venta {
    private int id;
    private int clienteId;
    private int empleadoId;
    private Timestamp fecha;
    private BigDecimal total;
    private String tipoPago;

    // Constructor vacío
    public Venta() {
    }

    // Constructor usado desde PanelVentas
public Venta(int clienteId, int empleadoId, BigDecimal total, String tipoPago) {
    this.clienteId = clienteId;
    this.empleadoId = empleadoId;
    this.total = total;
    this.tipoPago = tipoPago;
}


    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public String gettipoPago(){
        return tipoPago;
    }
    
    public void settipoPago(String tipoPago){
        this.tipoPago = tipoPago; 
    }
    
}
