/**
 * NOTE: wird benoetigt um Objekte vom Typ T nach kriterien zu filtern
 * 
 * GOOD: Der Selektor ist auf verschiedenste Objekte anwendbar aufgrund der gegebenen Generiztität.
 * 		 Dadurch wird Codewiederholung vermieden und der Code Zentral abgerufen.
 * 
 * @author VHD
 * @param <T>
 */
public interface Selector<T> {
	/**
	 * Vorbedingung: item ist ungleich NULL
	 */
	public boolean select(T item);
}
