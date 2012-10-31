/**
 * NOTE: wird benoetigt um Objekte vom Typ T nach kriterien zu filtern
 * 
 * GOOD: Dynamisches Binden. Der Selector ermoeglicht es das Arbeiten mit den
 * Selectoren von der Selector-Logik zu trennen.
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
