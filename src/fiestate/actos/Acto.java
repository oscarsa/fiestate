package fiestate.actos;

public class Acto
{
    private String hora;
    private String titulo;
    private String descripcion;
    private String tipoActo;
 
    public Acto(String hora, String titulo, String descripcion, String tipoActo){
        this.hora = hora;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoActo = tipoActo;
    }
    
    public String getTipoActo()
    {
    	return tipoActo;
    }
 
    public String getHora(){
        return hora;
    }
 
    public String getTitulo(){
        return titulo;
    }
 
    public String getDescripcion(){
        return descripcion;
    }
}