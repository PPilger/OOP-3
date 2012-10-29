import java.util.ArrayList;
import java.util.List;

/**
 * Eine Sammlung von Songs.
 * 
 * @author Peter Pilgerstorfer * 
 */
public class Songs extends Selection<Song> {
	private static final long serialVersionUID = 1L;

	/**
	 * FEHLER: hier passiert nichts
	 */
	public Songs() {
	}

	/**
	 * NOTE: Erstelle eine neue Song Sammlung die auf den selben Daten wie
	 * <code>base</code> arbeitet. Es sind jedoch nur Elemente sichtbar, die von
	 * den Selektoren selektiert werden.
	 * 
	 * @param base
	 * @param selectors
	 * Vorbedingung: Liste darf nicht NULL sein, oder Elemente enthalten die NULL sind
	 */
	private Songs(Songs base, List<Selector<Song>> selectors) {
		super(base, selectors);
	}

	/**
	 * NOTE: Liefert eine Selektion der in diesem Objekt gespeicherten Songs. Mit den
	 * uebergebenen Selektoren kann bestimmt werden, welche Songs selektiert
	 * werden. Aenderungen in der zurueckgegebenen Selektion wirken sich direkt
	 * auf das Original aus.
	 * 
	 * Vorbedingung: Parameter darf nicht NULL sein 
	 * @param selectors
	 * @return
	 */
	public Songs select(List<Selector<Song>> selectors) {
		return new Songs(this, selectors);
	}

	/**
	 * 
	 * @return
	 * Nachbedingung: liefert instanzierte befuellte Liste zurueck
	 */
	public List<SongVariante> getSongVarianten() {
		return getSongVarianten(new ArrayList<Selector<Variante>>());
	}

	/**
	 * Liefert eine Selektion der in diesem Objekt gespeicherten SongVarianten. Mit den
	 * uebergebenen Selektoren kann bestimmt werden, welche Songs selektiert 
	 * werden. Aenderungen in der zurueckgegebenen Selektion wirken sich direkt auf
	 * das Original aus.
	 * 
	 * Vorbedinung: Parameter darf nicht NULL sein
	 * Nachbedingung: Es existiert eine Liste mit selektierten SongVarianten
	 * @param selectors
	 * @return Eine selektierte Liste mit SongVariante-Objekte
	 */
	
  /* * 
	 * @param selectors
	 * @return
	 * Nachbedingung: liefert befuellte instanzierte Liste zurueck
	 */
	public List<SongVariante> getSongVarianten(
			List<Selector<Variante>> selectors) {
		List<SongVariante> songVarianten = new ArrayList<SongVariante>();
		for (Song song : this) {
			for (Variante variante : song.getVarianten()) {
				if (select(variante, selectors)) {
					songVarianten.add(new SongVariante(song, variante));
				}
			}
		}
		return songVarianten;
	}

	/**
	 * Vorbedingung: Parameter darf nicht NULL sein, oder Elemente die NULL sind enthalten
	 * @param variante
	 * @return true, wenn alle Selektoren die Variante selektieren, false
	 *         anderenfalls.
	 */
	private boolean select(Variante variante, List<Selector<Variante>> selectors) {
		for (Selector<Variante> selector : selectors) {
			if (!selector.select(variante)) {
				return false;
			}
		}
		return true;
	}
}
