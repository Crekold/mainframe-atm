package bo.edu.ucb.sis213.pl;
import java.util.Date;

public class Transaccion {
    private Date fecha;
    private String tipoOperacion;
    private double cantidad;

    public Transaccion(Date fecha, String tipoOperacion, double cantidad) {
        this.fecha = fecha;
        this.tipoOperacion = tipoOperacion;
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public double getCantidad() {
        return cantidad;
    }
}
