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
	 * Nachbedingung: das neue Songs Objekt enthaelt keine Songs
	 */
	public Songs() {
	}

	/**
	 * NOTE: Erstelle eine neue Song Sammlung die auf den selben Daten wie
	 * <code>base</code> arbeitet. Es sind jedoch nur Elemente sichtbar, die von
	 * den Selektoren selektiert werden.
	 * 
	 * Vorbedingung: base und selectors sind ungleich null. selectors enthaelt
	 * keine Elemente gleich null.
	 * 
	 * Nachbedinung: das neue Songs Objekt arbeitet auf den selben Elementen wie
	 * base. Es sind jedoch nur Elemente sichtbar, die mit den selectors
	 * selektiert werden.
	 */
	private Songs(Songs base, List<Selector<Song>> selectors) {
		super(base, selectors);
	}

	/**
	 * NOTE: Liefert eine Selektion der in diesem Objekt gespeicherten Songs.
	 * Mit den uebergebenen Selektoren kann bestimmt werden, welche Songs
	 * selektiert werden. Aenderungen in der zurueckgegebenen Selektion wirken
	 * sich direkt auf das Original aus.
	 * 
	 * Vorbedingung: selectors ist ungleich null und enthaelt keine Elemente
	 * gleich null.
	 * 
	 * Nachbedinung: das zurueckgegebene Songs Objekt arbeitet auf den selben
	 * Elementen wie this. Es sind jedoch nur Elemente sichtbar, die mit den
	 * selectors selektiert werden.
	 */
	public Songs select(List<Selector<Song>> selectors) {
		return new Songs(this, selectors);
	}

	/**
	 * Nachbedingung: der Rueckgabewert ist eine neu initialisierte Liste aus
	 * Songvarianten, bestehend aus allen Varianten der selektierten Songs.
	 */
	public List<SongVariante> getSongVarianten() {
		return getSongVarianten(new ArrayList<Selector<Variante>>());
	}

	/**
	 * Liefert eine Selektion der in diesem Objekt gespeicherten SongVarianten.
	 * Mit den uebergebenen Selektoren kann bestimmt werden, welche Songs
	 * selektiert werden. Aenderungen in der zurueckgegebenen Selektion wirken
	 * sich direkt auf das Original aus.
	 * 
	 * Vorbedinung: selectors ist ungleich null
	 * 
	 * Nachbedingung: der Rueckgabewert ist eine neu initialisierte Liste aus
	 * Songvarianten, bestehend aus den selektierten Varianten von den
	 * selektierten Songs.
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
	 * Vorbedingung: variante und selectors sind ungleich null. selectors
	 * enthaelt keine Elemente gleich null
	 * 
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
