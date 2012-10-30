/**
 * NOTE: wird benoetigt um Objekte vom Typ T nach kriterien zu filtern
 * 
 * @author VHD
 * 
 * @param <T>
 */
public interface Selector<T> {
	/**
	 * Vorbedingung: item ist ungleich NULL
	 */
	public boolean select(T item);
}
