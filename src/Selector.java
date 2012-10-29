/**
 * NOTE: wird benoetigt um Objekte vom Typ T nach kriterien zu filtern
 * @author VHD
 *
 * @param <T>
 * Vorbedinung: es duerfen keinerlei NULL Werte uebergeben werden
 */
public interface Selector<T> {
	public boolean select(T item);
}
